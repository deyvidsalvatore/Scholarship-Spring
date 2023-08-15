package deyvid.silva.scholarship.services;

import deyvid.silva.scholarship.dto.response.ClassResponse;
import deyvid.silva.scholarship.entity.Class;
import deyvid.silva.scholarship.entity.Coordinator;
import deyvid.silva.scholarship.entity.ScrumMaster;
import deyvid.silva.scholarship.enums.Status;
import deyvid.silva.scholarship.exceptions.ResourceNotFoundException;
import deyvid.silva.scholarship.repository.*;
import deyvid.silva.scholarship.service.ClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClassServiceTest {

    @Mock
    private ClassRepository classRepository;

    @Mock
    private CoordinatorRepository coordinatorRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private ScrumMasterRepository scrumMasterRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SquadRepository squadRepository;

    @InjectMocks
    private ClassService classService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetClassById_ValidId_ReturnsClassResponse() {
        Class classEntity = createMockClass(1);
        when(classRepository.findById(1)).thenReturn(Optional.of(classEntity));
        when(modelMapper.map(classEntity, ClassResponse.class)).thenReturn(new ClassResponse());
        
        ClassResponse classResponse = classService.getClassById(1);
        
        assertNotNull(classResponse);
        verify(modelMapper, times(1)).map(classEntity, ClassResponse.class);
    }

    @Test
    void testGetClassById_InvalidId_ThrowsResourceNotFoundException() {
        when(classRepository.findById(999)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> classService.getClassById(999));
    }
    

    private Class createMockClass(int id) {
        Class classEntity = new Class();
        classEntity.setId(id);
        classEntity.setName("Test Class " + id);
        classEntity.setStatus(Status.Waiting);
        classEntity.setCoordinator(new Coordinator());
        classEntity.setScrumMaster(new ScrumMaster());
        classEntity.setInstructors(new ArrayList<>());
        classEntity.setSquads(new ArrayList<>());
        return classEntity;
    }
}
