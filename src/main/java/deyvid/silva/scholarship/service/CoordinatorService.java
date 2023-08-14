package deyvid.silva.scholarship.service;

import deyvid.silva.scholarship.dto.request.CoordinatorRequest;
import deyvid.silva.scholarship.dto.response.CoordinatorResponse;
import deyvid.silva.scholarship.entity.Coordinator;
import deyvid.silva.scholarship.exceptions.ResourceNotFoundException;
import deyvid.silva.scholarship.repository.CoordinatorRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class CoordinatorService {

    private final ModelMapper modelMapper;

    private final CoordinatorRepository coordinatorRepository;

    public CoordinatorService(ModelMapper modelMapper, CoordinatorRepository coordinatorRepository) {
        this.modelMapper = modelMapper;
        this.coordinatorRepository = coordinatorRepository;
    }

    public List<CoordinatorResponse> getAllCoordinators() {
        return coordinatorRepository.findAll()
                .stream()
                .map(this::coordinatorDtoResponse)
                .collect(Collectors.toList());
    }

    public CoordinatorResponse getCoordinatorById(@Valid @Positive(message = "id must be greater than 0") Integer id) {
        var coordinator = coordinatorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Coordinator with "+ id + " was not found"));
        return coordinatorDtoResponse(coordinator);
    }

    public CoordinatorResponse saveCoordinator(@Valid CoordinatorRequest CoordinatorRequest) {
        var coordinator = Coordinator.builder()
                .firstName(CoordinatorRequest.getFirstName())
                .lastName(CoordinatorRequest.getLastName())
                .email(CoordinatorRequest.getEmail())
                .build();
        return coordinatorDtoResponse(coordinatorRepository.save(coordinator));
    }

    public CoordinatorResponse updateCoordinator(@Valid @Positive(message = "id must be greater than 0") Integer id,
                                                 CoordinatorRequest coordinatorRequest) {
        var coordinator = coordinatorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Coordinator with "+ id + " was not found"));
        coordinator = Coordinator.builder()
                .id(id)
                .firstName(isNotBlankOrNotNull(coordinatorRequest.getFirstName()) ? coordinatorRequest.getFirstName() : coordinator.getFirstName())
                .lastName(isNotBlankOrNotNull(coordinatorRequest.getLastName()) ? coordinatorRequest.getLastName() : coordinator.getLastName())
                .email(isNotBlankOrNotNull(coordinatorRequest.getEmail()) ? coordinatorRequest.getEmail() : coordinator.getEmail())
                .build();
        return coordinatorDtoResponse(coordinatorRepository.save(coordinator));
    }

    public String deleteCoordinator(@Valid @Positive(message = "id must be greater than 0") Integer id) {
        var coordinator = coordinatorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Coordinator with "+ id + " was not found"));
        coordinatorRepository.delete(coordinator);
        return String.format("Coordinator with ID %d was deleted", id);
    }

    private CoordinatorResponse coordinatorDtoResponse(Coordinator coordinator) {
        return modelMapper.map(coordinator, CoordinatorResponse.class);
    }

    private boolean isNotBlankOrNotNull(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
