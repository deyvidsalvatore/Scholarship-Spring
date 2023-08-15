package deyvid.silva.scholarship.services;

import deyvid.silva.scholarship.dto.request.StudentRequest;
import deyvid.silva.scholarship.dto.response.StudentResponse;
import deyvid.silva.scholarship.entity.Student;
import deyvid.silva.scholarship.exceptions.ResourceNotFoundException;
import deyvid.silva.scholarship.repository.StudentRepository;
import deyvid.silva.scholarship.service.StudentService;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "John", "Dorie", "john@email.com", null));
        students.add(new Student(2, "June", "Dorie", "june@email.com", null));
        when(studentRepository.findAll()).thenReturn(students);

        when(modelMapper.map(any(Student.class), eq(StudentResponse.class)))
                .thenAnswer(invocation -> {
                    Student student = invocation.getArgument(0);
                    StudentResponse response = new StudentResponse();
                    response.setFirstName(student.getFirstName());
                    response.setLastName(student.getLastName());
                    return response;
                });

        List<StudentResponse> studentResponses = studentService.getAllStudents();

        assertEquals(2, studentResponses.size());
        assertEquals("John", studentResponses.get(0).getFirstName());
        assertEquals("June", studentResponses.get(1).getFirstName());

        verify(modelMapper, times(2)).map(any(Student.class), eq(StudentResponse.class));
    }

    @Test
    void testGetStudentById_ValidId_ReturnsStudentResponse() {
        Integer studentId = 99;
        Student student = new Student(studentId, "John", "Dorie", "john@email.com", null);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        StudentResponse mockStudentResponse = new StudentResponse();
        mockStudentResponse.setFirstName("John");

        when(modelMapper.map(student, StudentResponse.class)).thenReturn(mockStudentResponse);

        StudentResponse studentResponse = studentService.getStudentById(studentId);

        assertEquals("John", studentResponse.getFirstName());

        verify(modelMapper, times(1)).map(student, StudentResponse.class);
    }

    @Test
    void testGetStudentById_InvalidId_ThrowsResourceNotFoundException() {
        Integer invalidStudentId = 999;
        when(studentRepository.findById(invalidStudentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(invalidStudentId));
    }

    @Test
    void testSaveStudent_ValidStudentRequest_ReturnsStudentResponse() {
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName("John");
        studentRequest.setLastName("Dorie");
        studentRequest.setEmail("john@email.com");

        Student savedStudent = new Student();
        savedStudent.setId(1);
        savedStudent.setFirstName("John");
        savedStudent.setLastName("Dorie");
        savedStudent.setEmail("john@email.com");

        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        StudentResponse mockStudentResponse = new StudentResponse();
        mockStudentResponse.setFirstName("John");

        when(modelMapper.map(savedStudent, StudentResponse.class)).thenReturn(mockStudentResponse);

        StudentResponse studentResponse = studentService.saveStudent(studentRequest);

        assertEquals("John", studentResponse.getFirstName());

        verify(studentRepository, times(1)).save(any(Student.class));
        verify(modelMapper, times(1)).map(savedStudent, StudentResponse.class);
    }

    @Test
    void testUpdateStudent_ValidStudentRequest_ReturnsUpdatedStudentResponse() {
        Integer studentId = 1;
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName("UpdatedFirstName");
        studentRequest.setLastName("UpdatedLastName");
        studentRequest.setEmail("updated@email.com");

        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setFirstName("John");
        existingStudent.setLastName("Dorie");
        existingStudent.setEmail("john@email.com");

        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setFirstName("UpdatedFirstName");
        updatedStudent.setLastName("UpdatedLastName");
        updatedStudent.setEmail("updated@email.com");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));

        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        StudentResponse mockUpdatedStudentResponse = new StudentResponse();
        mockUpdatedStudentResponse.setFirstName("UpdatedFirstName");

        when(modelMapper.map(updatedStudent, StudentResponse.class)).thenReturn(mockUpdatedStudentResponse);

        StudentResponse updatedStudentResponse = studentService.updateStudent(studentId, studentRequest);

        assertEquals("UpdatedFirstName", updatedStudentResponse.getFirstName());

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).save(any(Student.class));
        verify(modelMapper, times(1)).map(updatedStudent, StudentResponse.class);
    }

    @Test
    void testDeleteStudent_ValidId_ReturnsSuccessMessage() {
        Integer studentId = 1;

        Student studentToDelete = new Student();
        studentToDelete.setId(studentId);
        studentToDelete.setFirstName("John");
        studentToDelete.setLastName("Dorie");
        studentToDelete.setEmail("john@email.com");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentToDelete));

        String resultMessage = studentService.deleteStudent(studentId);

        assertEquals("Student with ID 1 was deleted", resultMessage);

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).delete(studentToDelete);
    }


}
