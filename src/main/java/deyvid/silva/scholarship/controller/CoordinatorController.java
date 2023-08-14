package deyvid.silva.scholarship.controller;

import deyvid.silva.scholarship.dto.request.CoordinatorRequest;
import deyvid.silva.scholarship.dto.response.CoordinatorResponse;
import deyvid.silva.scholarship.service.CoordinatorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/scholarship/v1/coordinator")
public class CoordinatorController {

    private final CoordinatorService coordinatorService;

    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CoordinatorResponse>> getAllCoordinators() {
        return ResponseEntity.status(HttpStatus.OK).body(coordinatorService.getAllCoordinators());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CoordinatorResponse> getCoordinatorById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(coordinatorService.getCoordinatorById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CoordinatorResponse> saveCoordinator(@RequestBody @Valid CoordinatorRequest coordinatorRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(coordinatorService.saveCoordinator(coordinatorRequest));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CoordinatorResponse> updateCoordinator(@PathVariable Integer id, @RequestBody CoordinatorRequest coordinatorRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(coordinatorService.updateCoordinator(id, coordinatorRequest));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteCoordinator(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(coordinatorService.deleteCoordinator(id));
    }
}
