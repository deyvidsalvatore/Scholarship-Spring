package deyvid.silva.scholarship.controller.v1;

import deyvid.silva.scholarship.dto.request.StudentRequest;
import deyvid.silva.scholarship.dto.response.StudentResponse;
import deyvid.silva.scholarship.dto.response.StudentSquadResponse;
import deyvid.silva.scholarship.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scholarship/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllStudents());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudentById(id));
    }

    @GetMapping(value = "/{id}/squads", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StudentSquadResponse>> getSquadsByStudentId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getSquadsByStudentId(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentResponse> saveStudent(@RequestBody @Valid StudentRequest studentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.saveStudent(studentRequest));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Integer id, @RequestBody StudentRequest studentRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.updateStudent(id, studentRequest));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteStudent(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.deleteStudent(id));
    }
}
