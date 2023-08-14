package deyvid.silva.scholarship.controller.v1;

import deyvid.silva.scholarship.dto.request.ClassRequest;
import deyvid.silva.scholarship.dto.response.ClassResponse;
import deyvid.silva.scholarship.service.ClassService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scholarship/v1/class")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    public ResponseEntity<List<ClassResponse>> getAllClasses() {
        return ResponseEntity.status(HttpStatus.OK).body(classService.getAllClasses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassResponse> getClassById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(classService.getClassById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassResponse> saveClass(@RequestBody @Valid ClassRequest classRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(classService.saveClass(classRequest));
    }

    @PostMapping(value= "/{id}/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassResponse> startClass(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(classService.startClass(id));
    }

    @PostMapping(value = "/{id}/finish", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassResponse> finishClass(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(classService.finishClass(id));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassResponse> updateClass(@PathVariable Integer id, @RequestBody @Valid ClassRequest classRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(classService.updateClass(id, classRequest));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteClass(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(classService.deleteClass(id));
    }
}
