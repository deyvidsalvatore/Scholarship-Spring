package deyvid.silva.scholarship.service;

import deyvid.silva.scholarship.dto.request.SquadRequest;
import deyvid.silva.scholarship.dto.response.SquadResponse;
import deyvid.silva.scholarship.dto.response.StudentSquadResponse;
import deyvid.silva.scholarship.entity.Squad;
import deyvid.silva.scholarship.entity.Student;
import deyvid.silva.scholarship.exceptions.ArgumentException;
import deyvid.silva.scholarship.exceptions.ResourceNotFoundException;
import deyvid.silva.scholarship.repository.SquadRepository;
import deyvid.silva.scholarship.repository.StudentRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class SquadService {

    private final ModelMapper modelMapper;

    private final SquadRepository squadRepository;

    private final StudentRepository studentRepository;

    public SquadService(ModelMapper modelMapper, SquadRepository squadRepository, StudentRepository studentRepository) {
        this.modelMapper = modelMapper;
        this.squadRepository = squadRepository;
        this.studentRepository = studentRepository;
    }

    public List<StudentSquadResponse> getAllSquads() {
        List<Squad> squads = squadRepository.findAll();
        return squads.stream()
                .map(this::createStudentSquadResponse)
                .collect(Collectors.toList());
    }

    public SquadResponse getSquadById(Integer id) {
        var squad = findSquadById(id);
        return squadDtoResponse(squad);
    }

    @Transactional
    public SquadResponse createSquad(@Valid SquadRequest squadRequest) {
        List<Integer> studentIds = squadRequest.getStudents();

        validateNumberOfStudents(studentIds);

        Set<Integer> uniqueStudentIds = new HashSet<>();
        List<Student> students = new ArrayList<>();

        for (Integer studentId : studentIds) {
            if (uniqueStudentIds.add(studentId)) {
                students.add(getStudentById(studentId));
            }
        }

        Squad squad = Squad.builder()
                .squadName(squadRequest.getSquadName())
                .students(students)
                .build();

        Squad savedSquad = squadRepository.save(squad);
        return squadDtoResponse(savedSquad);
    }

    @Transactional
    public SquadResponse updateSquad(Integer id, @Valid SquadRequest squadRequest) {
        List<Integer> studentIds = squadRequest.getStudents();

        validateNumberOfStudents(studentIds);

        Set<Integer> uniqueStudentIds = new HashSet<>();
        List<Student> students = new ArrayList<>();

        for (Integer studentId : studentIds) {
            if (uniqueStudentIds.add(studentId)) {
                students.add(getStudentById(studentId));
            }
        }

        Squad squad = findSquadById(id);
        squad.setSquadName(squadRequest.getSquadName());
        squad.setStudents(students);

        Squad updatedSquad = squadRepository.save(squad);
        return squadDtoResponse(updatedSquad);
    }


    @Transactional
    public String deleteSquad(Integer id) {
        Squad squad = findSquadById(id);
        squadRepository.delete(squad);
        return String.format("Squad with ID %d was deleted", id);
    }

    private Squad findSquadById(Integer id) {
        return squadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Squad with ID " + id + " was not found"));
    }

    private StudentSquadResponse createStudentSquadResponse(Squad squad) {
        StudentSquadResponse response = new StudentSquadResponse();
        response.setId(squad.getId());
        response.setSquadName(squad.getSquadName());
        return response;
    }

    public long getNumberOfStudentsInSquad(Integer squadId) {
        Squad squad = findSquadById(squadId);
        return squad.getStudents().size();
    }


    private SquadResponse squadDtoResponse(Squad squad) {
        return modelMapper.map(squad, SquadResponse.class);
    }

    private void validateNumberOfStudents(List<Integer> studentIds) {
        int minStudents = 3, maxStudents = 5;
        if (studentIds.size() < minStudents || studentIds.size() > maxStudents) {
            throw new ArgumentException("Number of students should be between " + minStudents + " and " + maxStudents);
        }
    }
    private Student getStudentById(Integer studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student with ID " + studentId + " was not found"));
    }

}
