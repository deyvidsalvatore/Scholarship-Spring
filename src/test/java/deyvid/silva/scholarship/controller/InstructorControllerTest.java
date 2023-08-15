package deyvid.silva.scholarship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import deyvid.silva.scholarship.controller.v1.InstructorController;
import deyvid.silva.scholarship.dto.request.InstructorRequest;
import deyvid.silva.scholarship.dto.response.InstructorResponse;
import deyvid.silva.scholarship.service.InstructorService;
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

@WebMvcTest(InstructorController.class)
public class InstructorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstructorService instructorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllInstructors() throws Exception {
        List<InstructorResponse> instructors = new ArrayList<>();
        instructors.add(new InstructorResponse());
        when(instructorService.getAllInstructors()).thenReturn(instructors);

        mockMvc.perform(get("/scholarship/v1/instructors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetInstructorById() throws Exception {
        InstructorResponse instructor = new InstructorResponse();
        when(instructorService.getInstructorById(1)).thenReturn(instructor);

        mockMvc.perform(get("/scholarship/v1/instructors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testSaveInstructor() throws Exception {
        InstructorRequest request = new InstructorRequest();
        request.setFirstName("Bob");
        request.setLastName("Johnson");
        request.setEmail("bob@email.com");

        InstructorResponse response = new InstructorResponse();
        when(instructorService.saveInstructor(request)).thenReturn(response);

        mockMvc.perform(post("/scholarship/v1/instructors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testUpdateInstructor() throws Exception {
        int instructorId = 1;
        InstructorRequest request = new InstructorRequest();
        InstructorResponse response = new InstructorResponse();
        when(instructorService.updateInstructor(eq(instructorId), any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/scholarship/v1/instructors/{id}", instructorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testDeleteInstructor() throws Exception {
        int instructorId = 1;
        String deleteMessage = "Instructor deleted.";
        when(instructorService.deleteInstructor(instructorId)).thenReturn(deleteMessage);

        mockMvc.perform(MockMvcRequestBuilders.delete("/scholarship/v1/instructors/{id}", instructorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(deleteMessage));
    }

}
