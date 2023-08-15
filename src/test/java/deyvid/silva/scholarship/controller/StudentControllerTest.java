package deyvid.silva.scholarship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import deyvid.silva.scholarship.controller.v1.StudentController;
import deyvid.silva.scholarship.dto.request.StudentRequest;
import deyvid.silva.scholarship.dto.response.StudentResponse;
import deyvid.silva.scholarship.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllStudents() throws Exception {
        List<StudentResponse> students = new ArrayList<>();
        students.add(new StudentResponse());
        when(studentService.getAllStudents()).thenReturn(students);
        
        mockMvc.perform(get("/scholarship/v1/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetStudentById() throws Exception {
        StudentResponse student = new StudentResponse();
        when(studentService.getStudentById(1)).thenReturn(student);
        
        mockMvc.perform(get("/scholarship/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }
    

    @Test
    void testSaveStudent() throws Exception {
        StudentRequest request = new StudentRequest();
        request.setFirstName("John");
        request.setLastName("Dorie");
        request.setEmail("john@email.com");

        StudentResponse response = new StudentResponse();
        when(studentService.saveStudent(request)).thenReturn(response);
        
        mockMvc.perform(post("/scholarship/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testUpdateStudent() throws Exception {
        int studentId = 1;
        StudentRequest request = new StudentRequest();
        StudentResponse response = new StudentResponse();
        when(studentService.updateStudent(eq(studentId), any())).thenReturn(response);
        
        mockMvc.perform(MockMvcRequestBuilders.put("/scholarship/v1/students/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testDeleteStudent() throws Exception {
        int studentId = 1;
        String deleteMessage = "Student deleted.";
        when(studentService.deleteStudent(studentId)).thenReturn(deleteMessage);
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/scholarship/v1/students/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(deleteMessage));
    }

}
