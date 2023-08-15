package deyvid.silva.scholarship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import deyvid.silva.scholarship.controller.v1.SquadController;
import deyvid.silva.scholarship.dto.request.SquadRequest;
import deyvid.silva.scholarship.dto.response.SquadResponse;
import deyvid.silva.scholarship.dto.response.StudentSquadResponse;
import deyvid.silva.scholarship.service.SquadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SquadController.class)
class SquadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SquadService squadService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllSquads() throws Exception {

        List<StudentSquadResponse> squads = new ArrayList<>();
        squads.add(new StudentSquadResponse());
        when(squadService.getAllSquads()).thenReturn(squads);

        mockMvc.perform(get("/scholarship/v1/squads")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetSquadById() throws Exception {

        SquadResponse squad = new SquadResponse();
        when(squadService.getSquadById(1)).thenReturn(squad);

        mockMvc.perform(get("/scholarship/v1/squads/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testSaveSquad() throws Exception {

        SquadRequest request = new SquadRequest();
        request.setSquadName("Alpha Squad");
        request.setStudents(Arrays.asList(1, 2, 3));

        SquadResponse response = new SquadResponse();
        when(squadService.createSquad(request)).thenReturn(response);

        mockMvc.perform(post("/scholarship/v1/squads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testUpdateSquad() throws Exception {

        int squadId = 1;
        SquadRequest request = new SquadRequest();
        request.setSquadName("Updated Squad");
        request.setStudents(Arrays.asList(4, 5, 6));

        SquadResponse response = new SquadResponse();
        when(squadService.updateSquad(eq(squadId), any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/scholarship/v1/squads/{id}", squadId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testDeleteSquad() throws Exception {

        int squadId = 1;
        String deleteMessage = "Squad deleted.";
        when(squadService.deleteSquad(squadId)).thenReturn(deleteMessage);


        mockMvc.perform(MockMvcRequestBuilders.delete("/scholarship/v1/squads/{id}", squadId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(deleteMessage));
    }

}