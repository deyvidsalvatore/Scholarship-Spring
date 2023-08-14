package deyvid.silva.scholarship.service;

import deyvid.silva.scholarship.dto.request.StudentRequest;
import deyvid.silva.scholarship.dto.response.SquadResponse;
import deyvid.silva.scholarship.dto.response.StudentResponse;
import deyvid.silva.scholarship.dto.response.StudentSquadResponse;
import deyvid.silva.scholarship.entity.Squad;
import deyvid.silva.scholarship.entity.Student;
import deyvid.silva.scholarship.exceptions.ResourceNotFoundException;
import deyvid.silva.scholarship.repository.StudentRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class StudentService {

    private final ModelMapper modelMapper;

    private final StudentRepository studentRepository;

    public StudentService(ModelMapper modelMapper, StudentRepository studentRepository) {
        this.modelMapper = modelMapper;
        this.studentRepository = studentRepository;
    }

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::studentDtoResponse)
                .collect(Collectors.toList());
    }

    public StudentResponse getStudentById(@Valid @Positive(message = "id must be greater than 0") Integer id) {
        var studentFound = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student with ID " + id + " was not found"));
        return studentDtoResponse(studentFound);
    }

    public StudentResponse saveStudent(@Valid StudentRequest studentRequest) {
        var student = Student.builder()
                .firstName(studentRequest.getFirstName())
                .lastName(studentRequest.getLastName())
                .email(studentRequest.getEmail())
                .build();
        return studentDtoResponse(studentRepository.save(student));
    }

    public StudentResponse updateStudent(@Valid @Positive(message = "id must be greater than 0") Integer id,
                                         StudentRequest studentRequest) {
        var student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student with ID " + id + " was not found"));
        student = Student.builder()
                .id(id)
                .firstName(isNotBlankOrNotNull(studentRequest.getFirstName()) ? studentRequest.getFirstName() : student.getFirstName())
                .lastName(isNotBlankOrNotNull(studentRequest.getLastName()) ? studentRequest.getLastName() : student.getLastName())
                .email(isNotBlankOrNotNull(studentRequest.getEmail()) ? studentRequest.getEmail() : student.getEmail())
                .build();
        return studentDtoResponse(studentRepository.save(student));
    }

    public String deleteStudent(@Valid @Positive(message = "id must be greater than 0") Integer id) {
        var student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student with ID " + id + " was not found"));
        studentRepository.delete(student);
        return String.format("Student with ID %d was deleted", id);
    }

    public List<StudentSquadResponse> getSquadsByStudentId(@Valid @Positive(message = "id must be greater than 0") Integer id) {
        List<Squad> squads = studentRepository.findSquadsByStudentId(id);
        return squads.stream()
                .map(this::createStudentSquadResponse)
                .collect(Collectors.toList());
    }

    private StudentResponse studentDtoResponse(Student student) {
        return modelMapper.map(student, StudentResponse.class);
    }

    private StudentSquadResponse createStudentSquadResponse(Squad squad) {
        StudentSquadResponse response = new StudentSquadResponse();
        response.setId(squad.getId());
        response.setSquadName(squad.getSquadName());
        return response;
    }

    private boolean isNotBlankOrNotNull(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
