package deyvid.silva.scholarship.controller.v2;

import deyvid.silva.scholarship.dto.response.ClassResponse;
import deyvid.silva.scholarship.entity.Class;
import deyvid.silva.scholarship.exceptions.ResourceNotFoundException;
import deyvid.silva.scholarship.repository.ClassRepository;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scholarship/v2/class")
public class ClassControllerV2 {

    private final ClassRepository classRepository;

    private final ModelMapper modelMapper;

    public ClassControllerV2(ClassRepository classRepository, ModelMapper modelMapper) {
        this.classRepository = classRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/coordinator")
    public List<ClassResponse> getClassesByCoordinator(@RequestParam Integer coordinatorId) {
        List<Class> classes = classRepository.findByCoordinator_Id(coordinatorId);
        return classes.stream()
                .map(this::createClassResponseWithTotalStudents)
                .collect(Collectors.toList());
    }

    @GetMapping("/instructor")
    public List<ClassResponse> getClassesByInstructor(@RequestParam Integer instructorId) {
        List<Class> classes = classRepository.findByInstructors_Id(instructorId);
        return classes.stream()
                .map(this::createClassResponseWithTotalStudents)
                .collect(Collectors.toList());
    }

    @GetMapping("/scrum-master")
    public List<ClassResponse> getClassesByScrumMaster(@RequestParam Integer scrumMasterId) {
        List<Class> classes = classRepository.findByScrumMaster_Id(scrumMasterId);
        return classes.stream()
                .map(this::createClassResponseWithTotalStudents)
                .collect(Collectors.toList());
    }

    private ClassResponse createClassResponseWithTotalStudents(Class classEntity) {
        ClassResponse classResponse = modelMapper.map(classEntity, ClassResponse.class);
        int totalStudents = getTotalStudentsInClass(classEntity.getId());
        classResponse.setTotalStudents(totalStudents);
        return classResponse;
    }

    public int getTotalStudentsInClass(Integer id) {
        Class classEntity = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class with ID " + id + " was not found"));

        return classEntity.getSquads().stream()
                .mapToInt(squad -> squad.getStudents().size())
                .sum();
    }

}
