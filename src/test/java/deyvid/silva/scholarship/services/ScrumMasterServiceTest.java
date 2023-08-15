package deyvid.silva.scholarship.services;

import deyvid.silva.scholarship.dto.request.ScrumMasterRequest;
import deyvid.silva.scholarship.dto.response.ScrumMasterResponse;
import deyvid.silva.scholarship.entity.ScrumMaster;
import deyvid.silva.scholarship.repository.ScrumMasterRepository;
import deyvid.silva.scholarship.service.ScrumMasterService;
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

class ScrumMasterServiceTest {

    @Mock
    private ScrumMasterRepository scrumMasterRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ScrumMasterService scrumMasterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllScrumMasters() {
        List<ScrumMaster> scrumMasters = new ArrayList<>();
        scrumMasters.add(new ScrumMaster(1, "John", "Dorie", "john@email.com", null));
        scrumMasters.add(new ScrumMaster(2, "June", "Dorie", "june@email.com", null));
        when(scrumMasterRepository.findAll()).thenReturn(scrumMasters);

        when(modelMapper.map(any(ScrumMaster.class), eq(ScrumMasterResponse.class)))
                .thenAnswer(invocation -> {
                    ScrumMaster scrumMaster = invocation.getArgument(0);
                    ScrumMasterResponse response = new ScrumMasterResponse();
                    response.setFirstName(scrumMaster.getFirstName());
                    response.setLastName(scrumMaster.getLastName());
                    return response;
                });

        List<ScrumMasterResponse> scrumMasterResponses = scrumMasterService.getAllScrumMasters();

        assertEquals(2, scrumMasterResponses.size());
        assertEquals("John", scrumMasterResponses.get(0).getFirstName());
        assertEquals("June", scrumMasterResponses.get(1).getFirstName());

        verify(modelMapper, times(2)).map(any(ScrumMaster.class), eq(ScrumMasterResponse.class));
    }

    @Test
    void testGetScrumMasterById_ValidId_ReturnsScrumMasterResponse() {
        Integer scrumMasterId = 1;
        ScrumMaster scrumMaster = new ScrumMaster(scrumMasterId, "John", "Dorie", "john@email.com", null);
        when(scrumMasterRepository.findById(scrumMasterId)).thenReturn(Optional.of(scrumMaster));

        ScrumMasterResponse mockScrumMasterResponse = new ScrumMasterResponse();
        mockScrumMasterResponse.setFirstName("John");

        when(modelMapper.map(scrumMaster, ScrumMasterResponse.class)).thenReturn(mockScrumMasterResponse);

        ScrumMasterResponse scrumMasterResponse = scrumMasterService.getScrumMasterById(scrumMasterId);

        assertEquals("John", scrumMasterResponse.getFirstName());

        verify(modelMapper, times(1)).map(scrumMaster, ScrumMasterResponse.class);
    }

    @Test
    void testSaveScrumMaster_ValidScrumMasterRequest_ReturnsScrumMasterResponse() {
        ScrumMasterRequest scrumMasterRequest = new ScrumMasterRequest();
        scrumMasterRequest.setFirstName("John");
        scrumMasterRequest.setLastName("Dorie");
        scrumMasterRequest.setEmail("john@email.com");

        ScrumMaster savedScrumMaster = new ScrumMaster();
        savedScrumMaster.setId(1);
        savedScrumMaster.setFirstName("John");
        savedScrumMaster.setLastName("Dorie");
        savedScrumMaster.setEmail("john@email.com");

        when(scrumMasterRepository.save(any(ScrumMaster.class))).thenReturn(savedScrumMaster);

        ScrumMasterResponse mockScrumMasterResponse = new ScrumMasterResponse();
        mockScrumMasterResponse.setFirstName("John");

        when(modelMapper.map(savedScrumMaster, ScrumMasterResponse.class)).thenReturn(mockScrumMasterResponse);

        ScrumMasterResponse scrumMasterResponse = scrumMasterService.saveScrumMaster(scrumMasterRequest);

        assertEquals("John", scrumMasterResponse.getFirstName());

        verify(scrumMasterRepository, times(1)).save(any(ScrumMaster.class));
        verify(modelMapper, times(1)).map(savedScrumMaster, ScrumMasterResponse.class);
    }

    @Test
    void testUpdateScrumMaster_ValidScrumMasterRequest_ReturnsUpdatedScrumMasterResponse() {
        Integer scrumMasterId = 1;
        ScrumMasterRequest scrumMasterRequest = new ScrumMasterRequest();
        scrumMasterRequest.setFirstName("June");
        scrumMasterRequest.setLastName("Dorie");
        scrumMasterRequest.setEmail("june@email.com");

        ScrumMaster existingScrumMaster = new ScrumMaster();
        existingScrumMaster.setId(scrumMasterId);
        existingScrumMaster.setFirstName("John");
        existingScrumMaster.setLastName("Dorie");
        existingScrumMaster.setEmail("john@email.com");

        ScrumMaster updatedScrumMaster = new ScrumMaster();
        updatedScrumMaster.setId(scrumMasterId);
        updatedScrumMaster.setFirstName("June");
        updatedScrumMaster.setLastName("Dorie");
        updatedScrumMaster.setEmail("june@email.com");

        when(scrumMasterRepository.findById(scrumMasterId)).thenReturn(Optional.of(existingScrumMaster));
        when(scrumMasterRepository.save(any(ScrumMaster.class))).thenReturn(updatedScrumMaster);

        ScrumMasterResponse mockUpdatedScrumMasterResponse = new ScrumMasterResponse();
        mockUpdatedScrumMasterResponse.setFirstName("June");

        when(modelMapper.map(updatedScrumMaster, ScrumMasterResponse.class)).thenReturn(mockUpdatedScrumMasterResponse);

        ScrumMasterResponse updatedScrumMasterResponse = scrumMasterService.updateScrumMaster(scrumMasterId, scrumMasterRequest);

        assertEquals("June", updatedScrumMasterResponse.getFirstName());

        verify(scrumMasterRepository, times(1)).findById(scrumMasterId);
        verify(scrumMasterRepository, times(1)).save(any(ScrumMaster.class));
        verify(modelMapper, times(1)).map(updatedScrumMaster, ScrumMasterResponse.class);
    }

    @Test
    void testDeleteScrumMaster_ValidId_ReturnsSuccessMessage() {
        Integer scrumMasterId = 1;

        ScrumMaster scrumMasterToDelete = new ScrumMaster();
        scrumMasterToDelete.setId(scrumMasterId);
        scrumMasterToDelete.setFirstName("John");
        scrumMasterToDelete.setLastName("Dorie");
        scrumMasterToDelete.setEmail("john@email.com");

        when(scrumMasterRepository.findById(scrumMasterId)).thenReturn(Optional.of(scrumMasterToDelete));

        String resultMessage = scrumMasterService.deleteScrumMaster(scrumMasterId);

        assertEquals("Scrum Master with ID 1 was deleted", resultMessage);

        verify(scrumMasterRepository, times(1)).findById(scrumMasterId);
        verify(scrumMasterRepository, times(1)).delete(scrumMasterToDelete);
    }
}

