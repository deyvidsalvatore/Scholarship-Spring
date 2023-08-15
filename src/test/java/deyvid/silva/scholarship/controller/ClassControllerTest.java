package deyvid.silva.scholarship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import deyvid.silva.scholarship.controller.v1.ClassController;
import deyvid.silva.scholarship.dto.request.ClassRequest;
import deyvid.silva.scholarship.dto.response.ClassResponse;
import deyvid.silva.scholarship.enums.Status;
import deyvid.silva.scholarship.service.ClassService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClassController.class)
public class ClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClassService classService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSaveClass() throws Exception {

        ClassRequest request = new ClassRequest();
        request.setName("Sample Class");
        request.setStatus(String.valueOf(Status.Waiting));
        request.setCoordinatorId(1);
        request.setScrumMasterId(2);
        request.setInstructorIds(Arrays.asList(3, 4));
        request.setSquadIds(Arrays.asList(5, 6));

        ClassResponse response = new ClassResponse();

        when(classService.saveClass(request)).thenReturn(response);


        mockMvc.perform(post("/scholarship/v1/class")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testUpdateClass() throws Exception {

        int classId = 1;
        ClassRequest request = new ClassRequest();
        request.setName("Updated Class");
        request.setStatus(String.valueOf(Status.Waiting));
        request.setCoordinatorId(7);
        request.setScrumMasterId(8);
        request.setInstructorIds(Arrays.asList(9, 10));
        request.setSquadIds(Arrays.asList(11, 12));

        ClassResponse response = new ClassResponse();


        when(classService.updateClass(eq(classId), any())).thenReturn(response);


        mockMvc.perform(MockMvcRequestBuilders.put("/scholarship/v1/class/{id}", classId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testStartClass() throws Exception {

        int classId = 1;
        ClassResponse response = new ClassResponse();
        when(classService.startClass(classId)).thenReturn(response);


        mockMvc.perform(post("/scholarship/v1/class/{id}/start", classId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testFinishClass() throws Exception {

        int classId = 1;
        ClassResponse response = new ClassResponse();
        when(classService.finishClass(classId)).thenReturn(response);


        mockMvc.perform(post("/scholarship/v1/class/{id}/finish", classId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testDeleteClass() throws Exception {

        int classId = 1;
        String deleteMessage = "Class deleted.";
        when(classService.deleteClass(classId)).thenReturn(deleteMessage);


        mockMvc.perform(MockMvcRequestBuilders.delete("/scholarship/v1/class/{id}", classId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
