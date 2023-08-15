package deyvid.silva.scholarship.services;

import deyvid.silva.scholarship.dto.response.SquadResponse;
import deyvid.silva.scholarship.dto.response.StudentSquadResponse;
import deyvid.silva.scholarship.entity.Squad;
import deyvid.silva.scholarship.exceptions.ResourceNotFoundException;
import deyvid.silva.scholarship.repository.SquadRepository;
import deyvid.silva.scholarship.repository.StudentRepository;
import deyvid.silva.scholarship.service.SquadService;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SquadServiceTest {

    @Mock
    private SquadRepository squadRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SquadService squadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSquadById_ValidId_ReturnsSquadResponse() {
        // Arrange
        Integer squadId = 1;
        Squad squad = new Squad(squadId, "Squad A", new ArrayList<>(), new ArrayList<>());
        when(squadRepository.findById(squadId)).thenReturn(Optional.of(squad));

        SquadResponse mockSquadResponse = new SquadResponse();
        mockSquadResponse.setSquadName("Squad A");

        when(modelMapper.map(squad, SquadResponse.class)).thenReturn(mockSquadResponse);

        // Act
        SquadResponse squadResponse = squadService.getSquadById(squadId);

        // Assert
        assertEquals("Squad A", squadResponse.getSquadName());

        verify(modelMapper, times(1)).map(squad, SquadResponse.class);
    }

    @Test
    void testGetSquadById_InvalidId_ThrowsResourceNotFoundException() {
        // Arrange
        Integer invalidSquadId = 999;
        when(squadRepository.findById(invalidSquadId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> squadService.getSquadById(invalidSquadId));
    }

}
