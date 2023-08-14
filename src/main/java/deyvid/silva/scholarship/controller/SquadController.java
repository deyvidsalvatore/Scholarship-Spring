package deyvid.silva.scholarship.controller;

import deyvid.silva.scholarship.dto.request.SquadRequest;
import deyvid.silva.scholarship.dto.response.SquadResponse;
import deyvid.silva.scholarship.dto.response.StudentSquadResponse;
import deyvid.silva.scholarship.service.SquadService;
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
@RequestMapping("/scholarship/v1/squads")
public class SquadController {

    private final SquadService squadService;

    public SquadController(SquadService squadService) {
        this.squadService = squadService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StudentSquadResponse>> getAllSquads() {
        return ResponseEntity.status(HttpStatus.OK).body(squadService.getAllSquads());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SquadResponse> getSquadById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(squadService.getSquadById(id));
    }
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SquadResponse> saveSquad(@RequestBody @Valid SquadRequest squadRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(squadService.createSquad(squadRequest));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SquadResponse> updateSquad(@PathVariable Integer id, @RequestBody @Valid SquadRequest squadRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(squadService.updateSquad(id, squadRequest));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteSquad(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(squadService.deleteSquad(id));
    }
}
