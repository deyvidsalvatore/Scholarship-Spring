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
        // Arrange
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

        // Act
        List<StudentResponse> studentResponses = studentService.getAllStudents();

        // Assert
        assertEquals(2, studentResponses.size());
        assertEquals("John", studentResponses.get(0).getFirstName());
        assertEquals("June", studentResponses.get(1).getFirstName());

        verify(modelMapper, times(2)).map(any(Student.class), eq(StudentResponse.class));
    }

    @Test
    void testGetStudentById_ValidId_ReturnsStudentResponse() {
        // Arrange
        Integer studentId = 99;
        Student student = new Student(studentId, "John", "Dorie", "john@email.com", null);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        StudentResponse mockStudentResponse = new StudentResponse();
        mockStudentResponse.setFirstName("John");

        when(modelMapper.map(student, StudentResponse.class)).thenReturn(mockStudentResponse);

        // Act
        StudentResponse studentResponse = studentService.getStudentById(studentId);

        // Assert
        assertEquals("John", studentResponse.getFirstName());

        verify(modelMapper, times(1)).map(student, StudentResponse.class);
    }

    @Test
    void testGetStudentById_InvalidId_ThrowsResourceNotFoundException() {
        // Arrange
        Integer invalidStudentId = 999;
        when(studentRepository.findById(invalidStudentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(invalidStudentId));
    }

    @Test
    void testSaveStudent_ValidStudentRequest_ReturnsStudentResponse() {
        // Arrange
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName("John");
        studentRequest.setLastName("Dorie");
        studentRequest.setEmail("john@email.com");

        // Create a mock Student instance that would be saved
        Student savedStudent = new Student();
        savedStudent.setId(1);
        savedStudent.setFirstName("John");
        savedStudent.setLastName("Dorie");
        savedStudent.setEmail("john@email.com");

        // Mock the behavior of studentRepository.save
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        // Mock the behavior of modelMapper
        StudentResponse mockStudentResponse = new StudentResponse();
        mockStudentResponse.setFirstName("John");
        // Set other properties of mockStudentResponse

        when(modelMapper.map(savedStudent, StudentResponse.class)).thenReturn(mockStudentResponse);

        // Act
        StudentResponse studentResponse = studentService.saveStudent(studentRequest);

        // Assert
        assertEquals("John", studentResponse.getFirstName());

        verify(studentRepository, times(1)).save(any(Student.class));
        verify(modelMapper, times(1)).map(savedStudent, StudentResponse.class);
    }

    @Test
    void testUpdateStudent_ValidStudentRequest_ReturnsUpdatedStudentResponse() {
        // Arrange
        Integer studentId = 1;
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName("UpdatedFirstName");
        studentRequest.setLastName("UpdatedLastName");
        studentRequest.setEmail("updated@email.com");

        // Create a mock Student instance for the existing student
        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setFirstName("John");
        existingStudent.setLastName("Dorie");
        existingStudent.setEmail("john@email.com");

        // Create a mock Student instance for the updated student
        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setFirstName("UpdatedFirstName");
        updatedStudent.setLastName("UpdatedLastName");
        updatedStudent.setEmail("updated@email.com");

        // Mock the behavior of studentRepository.findById
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));

        // Mock the behavior of studentRepository.save
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        // Mock the behavior of modelMapper
        StudentResponse mockUpdatedStudentResponse = new StudentResponse();
        mockUpdatedStudentResponse.setFirstName("UpdatedFirstName");
        // Set other properties of mockUpdatedStudentResponse

        when(modelMapper.map(updatedStudent, StudentResponse.class)).thenReturn(mockUpdatedStudentResponse);

        // Act
        StudentResponse updatedStudentResponse = studentService.updateStudent(studentId, studentRequest);

        // Assert
        assertEquals("UpdatedFirstName", updatedStudentResponse.getFirstName());

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).save(any(Student.class));
        verify(modelMapper, times(1)).map(updatedStudent, StudentResponse.class);
    }

    @Test
    void testDeleteStudent_ValidId_ReturnsSuccessMessage() {
        // Arrange
        Integer studentId = 1;

        // Create a mock Student instance that would be deleted
        Student studentToDelete = new Student();
        studentToDelete.setId(studentId);
        studentToDelete.setFirstName("John");
        studentToDelete.setLastName("Dorie");
        studentToDelete.setEmail("john@email.com");

        // Mock the behavior of studentRepository.findById
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentToDelete));

        // Act
        String resultMessage = studentService.deleteStudent(studentId);

        // Assert
        assertEquals("Student with ID 1 was deleted", resultMessage);

        // Verify interaction with studentRepository
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).delete(studentToDelete);
    }


}
