package deyvid.silva.scholarship.services;

import deyvid.silva.scholarship.dto.request.CoordinatorRequest;
import deyvid.silva.scholarship.dto.response.CoordinatorResponse;
import deyvid.silva.scholarship.entity.Coordinator;
import deyvid.silva.scholarship.repository.CoordinatorRepository;
import deyvid.silva.scholarship.service.CoordinatorService;
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

class CoordinatorServiceTest {

    @Mock
    private CoordinatorRepository coordinatorRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CoordinatorService coordinatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCoordinators() {
        // Arrange
        List<Coordinator> coordinators = new ArrayList<>();
        coordinators.add(new Coordinator(1, "John", "Dorie", "john@email.com", null));
        coordinators.add(new Coordinator(2, "June", "Dorie", "June@email.com", null));
        when(coordinatorRepository.findAll()).thenReturn(coordinators);

        when(modelMapper.map(any(Coordinator.class), eq(CoordinatorResponse.class)))
                .thenAnswer(invocation -> {
                    Coordinator coordinator = invocation.getArgument(0);
                    CoordinatorResponse response = new CoordinatorResponse();
                    response.setFirstName(coordinator.getFirstName());
                    response.setLastName(coordinator.getLastName());
                    return response;
                });

        // Act
        List<CoordinatorResponse> coordinatorResponses = coordinatorService.getAllCoordinators();

        // Assert
        assertEquals(2, coordinatorResponses.size());
        assertEquals("John", coordinatorResponses.get(0).getFirstName());
        assertEquals("June", coordinatorResponses.get(1).getFirstName());

        verify(modelMapper, times(2)).map(any(Coordinator.class), eq(CoordinatorResponse.class));
    }

    @Test
    void testGetCoordinatorById_ValidId_ReturnsCoordinatorResponse() {
        // Arrange
        Integer coordinatorId = 1;
        Coordinator coordinator = new Coordinator(coordinatorId, "John", "Dorie", "john@email.com", null);
        when(coordinatorRepository.findById(coordinatorId)).thenReturn(Optional.of(coordinator));

        CoordinatorResponse mockCoordinatorResponse = new CoordinatorResponse();
        mockCoordinatorResponse.setFirstName("John");

        when(modelMapper.map(coordinator, CoordinatorResponse.class)).thenReturn(mockCoordinatorResponse);

        // Act
        CoordinatorResponse coordinatorResponse = coordinatorService.getCoordinatorById(coordinatorId);

        // Assert
        assertEquals("John", coordinatorResponse.getFirstName());

        verify(modelMapper, times(1)).map(coordinator, CoordinatorResponse.class);
    }

    @Test
    void testSaveCoordinator_ValidCoordinatorRequest_ReturnsCoordinatorResponse() {
        // Arrange
        CoordinatorRequest coordinatorRequest = new CoordinatorRequest();
        coordinatorRequest.setFirstName("John");
        coordinatorRequest.setLastName("Dorie");
        coordinatorRequest.setEmail("john@email.com");

        Coordinator savedCoordinator = new Coordinator();
        savedCoordinator.setId(1);
        savedCoordinator.setFirstName("John");
        savedCoordinator.setLastName("Dorie");
        savedCoordinator.setEmail("john@email.com");

        when(coordinatorRepository.save(any(Coordinator.class))).thenReturn(savedCoordinator);

        CoordinatorResponse mockCoordinatorResponse = new CoordinatorResponse();
        mockCoordinatorResponse.setFirstName("John");
        // Set other properties of mockCoordinatorResponse

        when(modelMapper.map(savedCoordinator, CoordinatorResponse.class)).thenReturn(mockCoordinatorResponse);

        // Act
        CoordinatorResponse coordinatorResponse = coordinatorService.saveCoordinator(coordinatorRequest);

        // Assert
        assertEquals("John", coordinatorResponse.getFirstName());

        verify(coordinatorRepository, times(1)).save(any(Coordinator.class));
        verify(modelMapper, times(1)).map(savedCoordinator, CoordinatorResponse.class);
    }

    @Test
    void testUpdateCoordinator_ValidCoordinatorRequest_ReturnsUpdatedCoordinatorResponse() {
        // Arrange
        Integer coordinatorId = 1;
        CoordinatorRequest coordinatorRequest = new CoordinatorRequest();
        coordinatorRequest.setFirstName("June");
        coordinatorRequest.setLastName("Dorie");
        coordinatorRequest.setEmail("June@email.com");

        Coordinator existingCoordinator = new Coordinator();
        existingCoordinator.setId(coordinatorId);
        existingCoordinator.setFirstName("John");
        existingCoordinator.setLastName("Dorie");
        existingCoordinator.setEmail("john@email.com");

        Coordinator updatedCoordinator = new Coordinator();
        updatedCoordinator.setId(coordinatorId);
        updatedCoordinator.setFirstName("June");
        updatedCoordinator.setLastName("Dorie");
        updatedCoordinator.setEmail("June@email.com");

        when(coordinatorRepository.findById(coordinatorId)).thenReturn(Optional.of(existingCoordinator));
        when(coordinatorRepository.save(any(Coordinator.class))).thenReturn(updatedCoordinator);

        CoordinatorResponse mockUpdatedCoordinatorResponse = new CoordinatorResponse();
        mockUpdatedCoordinatorResponse.setFirstName("June");
        // Set other properties of mockUpdatedCoordinatorResponse

        when(modelMapper.map(updatedCoordinator, CoordinatorResponse.class)).thenReturn(mockUpdatedCoordinatorResponse);

        // Act
        CoordinatorResponse updatedCoordinatorResponse = coordinatorService.updateCoordinator(coordinatorId, coordinatorRequest);

        // Assert
        assertEquals("June", updatedCoordinatorResponse.getFirstName());

        verify(coordinatorRepository, times(1)).findById(coordinatorId);
        verify(coordinatorRepository, times(1)).save(any(Coordinator.class));
        verify(modelMapper, times(1)).map(updatedCoordinator, CoordinatorResponse.class);
    }

    @Test
    void testDeleteCoordinator_ValidId_ReturnsSuccessMessage() {
        // Arrange
        Integer coordinatorId = 1;

        Coordinator coordinatorToDelete = new Coordinator();
        coordinatorToDelete.setId(coordinatorId);
        coordinatorToDelete.setFirstName("John");
        coordinatorToDelete.setLastName("Dorie");
        coordinatorToDelete.setEmail("john@email.com");

        when(coordinatorRepository.findById(coordinatorId)).thenReturn(Optional.of(coordinatorToDelete));

        // Act
        String resultMessage = coordinatorService.deleteCoordinator(coordinatorId);

        // Assert
        assertEquals("Coordinator with ID 1 was deleted", resultMessage);

        verify(coordinatorRepository, times(1)).findById(coordinatorId);
        verify(coordinatorRepository, times(1)).delete(coordinatorToDelete);
    }
}
