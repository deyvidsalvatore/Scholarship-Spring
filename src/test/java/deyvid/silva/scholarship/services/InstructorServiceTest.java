package deyvid.silva.scholarship.services;

import deyvid.silva.scholarship.dto.request.InstructorRequest;
import deyvid.silva.scholarship.dto.response.InstructorResponse;
import deyvid.silva.scholarship.entity.Instructor;
import deyvid.silva.scholarship.repository.InstructorRepository;
import deyvid.silva.scholarship.service.InstructorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private InstructorService instructorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllInstructors() {
        List<Instructor> instructors = new ArrayList<>();
        instructors.add(new Instructor(1, "John", "Dorie", "john@email.com", null));
        instructors.add(new Instructor(2, "June", "Dorie", "June@email.com", null));
        when(instructorRepository.findAll()).thenReturn(instructors);

        when(modelMapper.map(any(Instructor.class), eq(InstructorResponse.class)))
                .thenAnswer(invocation -> {
                    Instructor instructor = invocation.getArgument(0);
                    InstructorResponse response = new InstructorResponse();
                    response.setFirstName(instructor.getFirstName());
                    response.setLastName(instructor.getLastName());
                    return response;
                });

        List<InstructorResponse> instructorResponses = instructorService.getAllInstructors();

        assertEquals(2, instructorResponses.size());
        assertEquals("John", instructorResponses.get(0).getFirstName());
        assertEquals("June", instructorResponses.get(1).getFirstName());

        verify(modelMapper, times(2)).map(any(Instructor.class), eq(InstructorResponse.class));
    }

    @Test
    void testGetInstructorById_ValidId_ReturnsInstructorResponse() {
        Integer instructorId = 1;
        Instructor instructor = new Instructor(instructorId, "John", "Dorie", "john@email.com", null);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        InstructorResponse mockInstructorResponse = new InstructorResponse();
        mockInstructorResponse.setFirstName("John");

        when(modelMapper.map(instructor, InstructorResponse.class)).thenReturn(mockInstructorResponse);

        InstructorResponse instructorResponse = instructorService.getInstructorById(instructorId);

        assertEquals("John", instructorResponse.getFirstName());

        verify(modelMapper, times(1)).map(instructor, InstructorResponse.class);
    }

    @Test
    void testSaveInstructor_ValidInstructorRequest_ReturnsInstructorResponse() {
        InstructorRequest instructorRequest = new InstructorRequest();
        instructorRequest.setFirstName("John");
        instructorRequest.setLastName("Dorie");
        instructorRequest.setEmail("john@email.com");

        Instructor savedInstructor = new Instructor();
        savedInstructor.setId(1);
        savedInstructor.setFirstName("John");
        savedInstructor.setLastName("Dorie");
        savedInstructor.setEmail("john@email.com");

        when(instructorRepository.save(any(Instructor.class))).thenReturn(savedInstructor);

        InstructorResponse mockInstructorResponse = new InstructorResponse();
        mockInstructorResponse.setFirstName("John");

        when(modelMapper.map(savedInstructor, InstructorResponse.class)).thenReturn(mockInstructorResponse);

        InstructorResponse instructorResponse = instructorService.saveInstructor(instructorRequest);

        assertEquals("John", instructorResponse.getFirstName());

        verify(instructorRepository, times(1)).save(any(Instructor.class));
        verify(modelMapper, times(1)).map(savedInstructor, InstructorResponse.class);
    }

    @Test
    void testUpdateInstructor_ValidInstructorRequest_ReturnsUpdatedInstructorResponse() {
        Integer instructorId = 1;
        InstructorRequest instructorRequest = new InstructorRequest();
        instructorRequest.setFirstName("June");
        instructorRequest.setLastName("Dorie");
        instructorRequest.setEmail("June@email.com");

        Instructor existingInstructor = new Instructor();
        existingInstructor.setId(instructorId);
        existingInstructor.setFirstName("John");
        existingInstructor.setLastName("Dorie");
        existingInstructor.setEmail("john@email.com");

        Instructor updatedInstructor = new Instructor();
        updatedInstructor.setId(instructorId);
        updatedInstructor.setFirstName("June");
        updatedInstructor.setLastName("Dorie");
        updatedInstructor.setEmail("June@email.com");

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(existingInstructor));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(updatedInstructor);

        InstructorResponse mockUpdatedInstructorResponse = new InstructorResponse();
        mockUpdatedInstructorResponse.setFirstName("June");

        when(modelMapper.map(updatedInstructor, InstructorResponse.class)).thenReturn(mockUpdatedInstructorResponse);

        InstructorResponse updatedInstructorResponse = instructorService.updateInstructor(instructorId, instructorRequest);

        assertEquals("June", updatedInstructorResponse.getFirstName());

        verify(instructorRepository, times(1)).findById(instructorId);
        verify(instructorRepository, times(1)).save(any(Instructor.class));
        verify(modelMapper, times(1)).map(updatedInstructor, InstructorResponse.class);
    }

    @Test
    void testDeleteInstructor_ValidId_ReturnsSuccessMessage() {
        Integer instructorId = 1;

        Instructor instructorToDelete = new Instructor();
        instructorToDelete.setId(instructorId);
        instructorToDelete.setFirstName("John");
        instructorToDelete.setLastName("Dorie");
        instructorToDelete.setEmail("john@email.com");

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructorToDelete));

        String resultMessage = instructorService.deleteInstructor(instructorId);

        assertEquals("Instructor with ID 1 was deleted", resultMessage);

        verify(instructorRepository, times(1)).findById(instructorId);
        verify(instructorRepository, times(1)).delete(instructorToDelete);
    }
}
