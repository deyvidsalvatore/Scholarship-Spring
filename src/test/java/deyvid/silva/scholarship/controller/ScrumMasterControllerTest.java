package deyvid.silva.scholarship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import deyvid.silva.scholarship.controller.v1.ScrumMasterController;
import deyvid.silva.scholarship.dto.request.ScrumMasterRequest;
import deyvid.silva.scholarship.dto.response.ScrumMasterResponse;
import deyvid.silva.scholarship.service.ScrumMasterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScrumMasterController.class)
class ScrumMasterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScrumMasterService scrumMasterService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllScrumMasters() throws Exception {
        List<ScrumMasterResponse> scrumMasters = new ArrayList<>();
        scrumMasters.add(new ScrumMasterResponse());
        when(scrumMasterService.getAllScrumMasters()).thenReturn(scrumMasters);

        mockMvc.perform(get("/scholarship/v1/scrum-master")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetScrumMasterById() throws Exception {
        ScrumMasterResponse scrumMaster = new ScrumMasterResponse();
        when(scrumMasterService.getScrumMasterById(1)).thenReturn(scrumMaster);

        mockMvc.perform(get("/scholarship/v1/scrum-master/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testSaveScrumMaster() throws Exception {
        ScrumMasterRequest request = new ScrumMasterRequest();
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setEmail("alice@email.com");

        ScrumMasterResponse response = new ScrumMasterResponse();
        when(scrumMasterService.saveScrumMaster(request)).thenReturn(response);

        mockMvc.perform(post("/scholarship/v1/scrum-master")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testUpdateScrumMaster() throws Exception {
        int scrumMasterId = 1;
        ScrumMasterRequest request = new ScrumMasterRequest();
        ScrumMasterResponse response = new ScrumMasterResponse();
        when(scrumMasterService.updateScrumMaster(eq(scrumMasterId), any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/scholarship/v1/scrum-master/{id}", scrumMasterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testDeleteScrumMaster() throws Exception {
        int scrumMasterId = 1;
        String deleteMessage = "Scrum Master deleted.";
        when(scrumMasterService.deleteScrumMaster(scrumMasterId)).thenReturn(deleteMessage);

        mockMvc.perform(MockMvcRequestBuilders.delete("/scholarship/v1/scrum-master/{id}", scrumMasterId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(deleteMessage));
    }

}
