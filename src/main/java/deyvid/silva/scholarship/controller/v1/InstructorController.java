package deyvid.silva.scholarship.controller.v1;

import deyvid.silva.scholarship.dto.request.InstructorRequest;
import deyvid.silva.scholarship.dto.response.InstructorResponse;
import deyvid.silva.scholarship.service.InstructorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("scholarship/v1/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InstructorResponse>> getAllCoordinators() {
        return ResponseEntity.status(HttpStatus.OK).body(instructorService.getAllInstructors());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstructorResponse> getCoordinatorById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(instructorService.getInstructorById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstructorResponse> saveCoordinator(@RequestBody @Valid InstructorRequest instructorRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(instructorService.saveInstructor(instructorRequest));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstructorResponse> updateCoordinator(@PathVariable Integer id, @RequestBody InstructorRequest instructorRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(instructorService.updateInstructor(id, instructorRequest));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteCoordinator(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(instructorService.deleteInstructor(id));
    }
}
