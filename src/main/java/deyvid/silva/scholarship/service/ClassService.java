package deyvid.silva.scholarship.service;

import deyvid.silva.scholarship.dto.request.ClassRequest;
import deyvid.silva.scholarship.dto.response.ClassResponse;
import deyvid.silva.scholarship.entity.*;
import deyvid.silva.scholarship.entity.Class;
import deyvid.silva.scholarship.enums.Status;
import deyvid.silva.scholarship.exceptions.ArgumentException;
import deyvid.silva.scholarship.exceptions.ResourceNotFoundException;
import deyvid.silva.scholarship.repository.ClassRepository;
import deyvid.silva.scholarship.repository.CoordinatorRepository;
import deyvid.silva.scholarship.repository.InstructorRepository;
import deyvid.silva.scholarship.repository.ScrumMasterRepository;
import deyvid.silva.scholarship.repository.SquadRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class ClassService {

    private final ClassRepository classRepository;
    private final CoordinatorRepository coordinatorRepository;
    private final InstructorRepository instructorRepository;
    private final ScrumMasterRepository scrumMasterRepository;
    private final ModelMapper modelMapper;
    private final SquadRepository squadRepository;

    public ClassService(ClassRepository classRepository, CoordinatorRepository coordinatorRepository, InstructorRepository instructorRepository, ScrumMasterRepository scrumMasterRepository, ModelMapper modelMapper, SquadRepository squadRepository) {
        this.classRepository = classRepository;
        this.coordinatorRepository = coordinatorRepository;
        this.instructorRepository = instructorRepository;
        this.scrumMasterRepository = scrumMasterRepository;
        this.modelMapper = modelMapper;
        this.squadRepository = squadRepository;
    }

    public List<ClassResponse> getAllClasses() {
        List<Class> classes = classRepository.findAll();
        return classes.stream()
                .map(this::createClassResponseWithTotalStudents)
                .collect(Collectors.toList());
    }

    public ClassResponse getClassById(Integer id) {
        Class classEntity = classRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Class with ID " + id + " was not found"));
        return createClassResponseWithTotalStudents(classEntity);
    }

    @Transactional
    public ClassResponse saveClass(ClassRequest classRequest) {
        checkForDuplicateSquadsAndInstructors(classRequest);

        Coordinator coordinator = coordinatorRepository.findById(classRequest.getCoordinatorId())
                .orElseThrow(() -> new ResourceNotFoundException("Coordinator with ID " + classRequest.getCoordinatorId() + " was not found"));

        ScrumMaster scrumMaster = scrumMasterRepository.findById(classRequest.getScrumMasterId())
                .orElseThrow(() -> new ResourceNotFoundException("Scrum Master with ID " + classRequest.getScrumMasterId() + " was not found"));

        List<Instructor> instructors = instructorRepository.findAllById(classRequest.getInstructorIds());

        List<Squad> squads = squadRepository.findAllById(classRequest.getSquadIds());

        Class classEntity = new Class();
        classEntity.setName(classRequest.getName());
        classEntity.setStatus(Status.Waiting);
        classEntity.setCoordinator(coordinator);
        classEntity.setScrumMaster(scrumMaster);
        classEntity.setInstructors(instructors);
        classEntity.setSquads(squads);

        return createClassResponseWithTotalStudents(classRepository.save(classEntity));
    }

    @Transactional
    public ClassResponse updateClass(Integer classId, ClassRequest classRequest) {
        var classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class with ID " + classId + " was not found"));

        if (classEntity.getStatus() == Status.Waiting) {
            checkForDuplicateSquadsAndInstructors(classRequest);

            Coordinator coordinator = coordinatorRepository.findById(classRequest.getCoordinatorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Coordinator with ID " + classRequest.getCoordinatorId() + " was not found"));

            ScrumMaster scrumMaster = scrumMasterRepository.findById(classRequest.getScrumMasterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Scrum Master with ID " + classRequest.getScrumMasterId() + " was not found"));

            List<Instructor> instructors = instructorRepository.findAllById(classRequest.getInstructorIds());

            List<Squad> squads = squadRepository.findAllById(classRequest.getSquadIds());

            classEntity.setName(classRequest.getName());
            classEntity.setCoordinator(coordinator);
            classEntity.setScrumMaster(scrumMaster);
            classEntity.setInstructors(instructors);
            classEntity.setSquads(squads);

            classEntity = classRepository.save(classEntity);
            return createClassResponseWithTotalStudents(classEntity);
        } else {
            throw new ArgumentException("Cannot update class with ID " + classId + " because its status is not Waiting.");
        }
    }

    @Transactional
    public ClassResponse startClass(Integer classId) {
        var classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class with ID " + classId + " was not found"));

        validateStartClassRequirements(classId, classEntity);

        classEntity.setId(classId);
        classEntity.setStatus(Status.Started);
        classEntity = classRepository.save(classEntity);

        return createClassResponseWithTotalStudents(classEntity);
    }

    @Transactional
    public ClassResponse finishClass(Integer classId) {
        var classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class with ID " + classId + " was not found"));

        if (classEntity.getStatus() == Status.Started) {
            classEntity.setStatus(Status.Finished);
            classEntity = classRepository.save(classEntity);
            return createClassResponseWithTotalStudents(classEntity);
        } else if (classEntity.getStatus() == Status.Waiting) {
            throw new ArgumentException("Class is still waiting and cannot be finished.");
        } else {
            throw new ArgumentException("Class is already finished.");
        }
    }

    @Transactional
    public String deleteClass(Integer id) {
        var classEntity = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class with ID " + id + " was not found"));

        if (classEntity.getStatus() == Status.Waiting || classEntity.getStatus() == Status.Finished) {
            classRepository.delete(classEntity);
            return "Class with ID " + id + " has been deleted.";
        } else {
            throw new ArgumentException("Cannot delete class with ID " + id + " because its status is not Waiting or Finished.");
        }
    }

    private void validateStartClassRequirements(Integer id, Class classEntity) {
        int minStudents = 15, maxStudents = 30;
        int requiredInstructors = 3;

        int totalStudents = getTotalStudentsInClass(classEntity.getId());

        if (totalStudents < minStudents) {
            throw new ArgumentException("Cannot start class with ID " + id + ". The class must have at least " + minStudents + " students to start.");
        }

        if (totalStudents > maxStudents) {
            throw new ArgumentException("Cannot start class with ID " + id + ". The class can have a maximum of " + maxStudents + " students to start. Please modify squads accordingly.");
        }

        int numInstructors = classEntity.getInstructors().size();

        if (numInstructors < requiredInstructors) {
            throw new ArgumentException("Cannot start class with ID " + id + ". The class must have at least " + requiredInstructors + " instructors to start.");
        }

        if (classEntity.getCoordinator() == null) {
            throw new ArgumentException("Cannot start class with ID " + id + ". The class must have a coordinator to start.");
        }

        if (classEntity.getScrumMaster() == null) {
            throw new ArgumentException("Cannot start class with ID " + id + ". The class must have a scrum master to start.");
        }
    }


    private ClassResponse createClassResponseWithTotalStudents(Class classEntity) {
        ClassResponse classResponse = modelMapper.map(classEntity, ClassResponse.class);
        int totalStudents = getTotalStudentsInClass(classEntity.getId());
        classResponse.setTotalStudents(totalStudents);
        return classResponse;
    }

    private void checkForDuplicateSquadsAndInstructors(ClassRequest classRequest) {
        Set<Integer> uniqueSquadIds = new HashSet<>();
        Set<Integer> uniqueInstructorIds = new HashSet<>();

        for (Integer squadId : classRequest.getSquadIds()) {
            if (!uniqueSquadIds.add(squadId)) {
                throw new ArgumentException("Duplicate squad ID found: " + squadId);
            }
        }

        for (Integer instructorId : classRequest.getInstructorIds()) {
            if (!uniqueInstructorIds.add(instructorId)) {
                throw new ArgumentException("Duplicate instructor ID found: " + instructorId);
            }
        }
    }
    private void checkForDuplicateStudents(ClassRequest classRequest) {
        Set<Integer> existingStudentIds = new HashSet<>();
        Set<Integer> duplicateStudentIds = new HashSet<>();

        List<Squad> squads = classRequest.getSquadIds().stream()
                .map(squadId -> squadRepository.findById(squadId)
                        .orElseThrow(() -> new ResourceNotFoundException("Squad with ID " + squadId + " was not found")))
                .toList();

        for (Squad squad : squads) {
            for (Student student : squad.getStudents()) {
                if (existingStudentIds.contains(student.getId())) {
                    duplicateStudentIds.add(student.getId());
                }
                existingStudentIds.add(student.getId());
            }
        }

        for (Squad squad : squads) {
            for (Student student : squad.getStudents()) {
                if (duplicateStudentIds.contains(student.getId())) {
                    throw new ArgumentException("Student with ID " + student.getId() + " is already assigned to another squad in the same class.");
                }
            }
        }
    }

    public int getTotalStudentsInClass(Integer id) {
        Class classEntity = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class with ID " + id + " was not found"));

        return classEntity.getSquads().stream()
                .mapToInt(squad -> squad.getStudents().size())
                .sum();
    }

}
