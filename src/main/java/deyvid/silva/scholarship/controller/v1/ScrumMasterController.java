package deyvid.silva.scholarship.controller.v1;

import deyvid.silva.scholarship.dto.request.ScrumMasterRequest;
import deyvid.silva.scholarship.dto.response.ScrumMasterResponse;
import deyvid.silva.scholarship.service.ScrumMasterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scholarship/v1/scrum-master")
public class ScrumMasterController {

    private final ScrumMasterService scrumMasterService;

    public ScrumMasterController(ScrumMasterService scrumMasterService) {
        this.scrumMasterService = scrumMasterService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ScrumMasterResponse>> getAllScrumMasters() {
        return ResponseEntity.status(HttpStatus.OK).body(scrumMasterService.getAllScrumMasters());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScrumMasterResponse> getScrumMasterById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(scrumMasterService.getScrumMasterById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScrumMasterResponse> saveScrumMaster(@RequestBody @Valid ScrumMasterRequest scrumMasterRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scrumMasterService.saveScrumMaster(scrumMasterRequest));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScrumMasterResponse> updateScrumMaster(@PathVariable Integer id, @RequestBody ScrumMasterRequest scrumMasterRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(scrumMasterService.updateScrumMaster(id, scrumMasterRequest));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteScrumMaster(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(scrumMasterService.deleteScrumMaster(id));
    }
}
