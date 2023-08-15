package deyvid.silva.scholarship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import deyvid.silva.scholarship.controller.v1.CoordinatorController;
import deyvid.silva.scholarship.dto.request.CoordinatorRequest;
import deyvid.silva.scholarship.dto.response.CoordinatorResponse;
import deyvid.silva.scholarship.service.CoordinatorService;
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

@WebMvcTest(CoordinatorController.class)
class CoordinatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoordinatorService coordinatorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllCoordinators() throws Exception {

        List<CoordinatorResponse> coordinators = new ArrayList<>();
        coordinators.add(new CoordinatorResponse());
        when(coordinatorService.getAllCoordinators()).thenReturn(coordinators);

        mockMvc.perform(get("/scholarship/v1/coordinator")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetCoordinatorById() throws Exception {

        CoordinatorResponse coordinator = new CoordinatorResponse();
        when(coordinatorService.getCoordinatorById(1)).thenReturn(coordinator);

        mockMvc.perform(get("/scholarship/v1/coordinator/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testSaveCoordinator() throws Exception {

        CoordinatorRequest request = new CoordinatorRequest();
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setEmail("alice@email.com");

        CoordinatorResponse response = new CoordinatorResponse();
        when(coordinatorService.saveCoordinator(request)).thenReturn(response);

        mockMvc.perform(post("/scholarship/v1/coordinator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testUpdateCoordinator() throws Exception {

        int coordinatorId = 1;
        CoordinatorRequest request = new CoordinatorRequest();
        CoordinatorResponse response = new CoordinatorResponse();
        when(coordinatorService.updateCoordinator(eq(coordinatorId), any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/scholarship/v1/coordinator/{id}", coordinatorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testDeleteCoordinator() throws Exception {

        int coordinatorId = 1;
        String deleteMessage = "Coordinator deleted.";
        when(coordinatorService.deleteCoordinator(coordinatorId)).thenReturn(deleteMessage);

        mockMvc.perform(MockMvcRequestBuilders.delete("/scholarship/v1/coordinator/{id}", coordinatorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(deleteMessage));
    }

}
