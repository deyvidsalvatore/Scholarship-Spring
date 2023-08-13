package deyvid.silva.scholarship.service;

import deyvid.silva.scholarship.dto.request.ScrumMasterRequest;
import deyvid.silva.scholarship.dto.response.ScrumMasterResponse;
import deyvid.silva.scholarship.entity.ScrumMaster;
import deyvid.silva.scholarship.exceptions.ResourceNotFoundException;
import deyvid.silva.scholarship.repository.ScrumMasterRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class ScrumMasterService {

    private final ModelMapper modelMapper;

    private final ScrumMasterRepository scrumMasterRepository;

    public ScrumMasterService(ModelMapper modelMapper, ScrumMasterRepository scrumMasterRepository) {
        this.modelMapper = modelMapper;
        this.scrumMasterRepository = scrumMasterRepository;
    }

    public List<ScrumMasterResponse> getAllScrumMasters() {
        return scrumMasterRepository.findAll()
                .stream()
                .map(this::scrumMasterDtoResponse)
                .collect(Collectors.toList());
    }

    public ScrumMasterResponse getScrumMasterById(@Valid @Positive(message = "id must be greater than 0") Integer id) {
        var scrumMaster = scrumMasterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Scrum Master with "+ id + " was not found"));
        return scrumMasterDtoResponse(scrumMaster);
    }

    public ScrumMasterResponse saveScrumMaster(@Valid ScrumMasterRequest scrumMasterRequest) {
        var scrumMaster = ScrumMaster.builder()
                .firstName(scrumMasterRequest.getFirstName())
                .lastName(scrumMasterRequest.getLastName())
                .email(scrumMasterRequest.getEmail())
                .build();
        return scrumMasterDtoResponse(scrumMasterRepository.save(scrumMaster));
    }

    public ScrumMasterResponse updateScrumMaster(@Valid @Positive(message = "id must be greater than 0") Integer id,
                                                 ScrumMasterRequest scrumMasterRequest) {
        var scrumMaster = scrumMasterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Scrum Master with "+ id + " was not found"));
        scrumMaster = ScrumMaster.builder()
                .id(id)
                .firstName(isNotBlankOrNotNull(scrumMasterRequest.getFirstName()) ? scrumMasterRequest.getFirstName() : scrumMaster.getFirstName())
                .lastName(isNotBlankOrNotNull(scrumMasterRequest.getLastName()) ? scrumMasterRequest.getLastName() : scrumMaster.getLastName())
                .email(isNotBlankOrNotNull(scrumMasterRequest.getEmail()) ? scrumMasterRequest.getEmail() : scrumMaster.getEmail())
                .build();
        return scrumMasterDtoResponse(scrumMasterRepository.save(scrumMaster));
    }

    public String deleteScrumMaster(@Valid @Positive(message = "id must be greater than 0") Integer id) {
        var scrumMaster = scrumMasterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Scrum Master with "+ id + " was not found"));
        scrumMasterRepository.delete(scrumMaster);
        return String.format("Scrum Master with ID %d was deleted", id);
    }

    private ScrumMasterResponse scrumMasterDtoResponse(ScrumMaster scrumMaster) {
        return modelMapper.map(scrumMaster, ScrumMasterResponse.class);
    }

    private boolean isNotBlankOrNotNull(String value) { return value != null && !value.trim().isEmpty(); }

}
