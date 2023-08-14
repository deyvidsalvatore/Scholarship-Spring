package deyvid.silva.scholarship.service;

import deyvid.silva.scholarship.dto.request.InstructorRequest;
import deyvid.silva.scholarship.dto.response.InstructorResponse;
import deyvid.silva.scholarship.entity.Instructor;
import deyvid.silva.scholarship.exceptions.ResourceNotFoundException;
import deyvid.silva.scholarship.repository.InstructorRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class InstructorService {

    private final ModelMapper modelMapper;

    private final InstructorRepository instructorRepository;

    public InstructorService(ModelMapper modelMapper, InstructorRepository instructorRepository) {
        this.modelMapper = modelMapper;
        this.instructorRepository = instructorRepository;
    }

    public List<InstructorResponse> getAllInstructors() {
        return instructorRepository.findAll()
                .stream()
                .map(this::instructorDtoResponse)
                .collect(Collectors.toList());
    }

    public InstructorResponse getInstructorById(@Valid @Positive(message = "id must be greater than 0") Integer id) {
        var Instructor = instructorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Instructor with "+ id + " was not found"));
        return instructorDtoResponse(Instructor);
    }

    public InstructorResponse saveInstructor(@Valid InstructorRequest instructorRequest) {
        var instructor = Instructor.builder()
                .firstName(instructorRequest.getFirstName())
                .lastName(instructorRequest.getLastName())
                .email(instructorRequest.getEmail())
                .build();
        return instructorDtoResponse(instructorRepository.save(instructor));
    }

    public InstructorResponse updateInstructor(@Valid @Positive(message = "id must be greater than 0") Integer id,
                                                 InstructorRequest instructorRequest) {
        var instructor = instructorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Instructor with "+ id + " was not found"));

        instructor = Instructor.builder()
                .id(id)
                .firstName(isNotBlankOrNotNull(instructorRequest.getFirstName())? instructorRequest.getFirstName() : instructor.getFirstName())
                .lastName(isNotBlankOrNotNull(instructorRequest.getLastName()) ? instructorRequest.getLastName() : instructor.getLastName())
                .email(isNotBlankOrNotNull(instructorRequest.getEmail()) ? instructorRequest.getEmail() : instructor.getEmail())
                .build();
        return instructorDtoResponse(instructorRepository.save(instructor));
    }

    public String deleteInstructor(@Valid @Positive(message = "id must be greater than 0") Integer id) {
        var instructor = instructorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Instructor with "+ id + " was not found"));
        instructorRepository.delete(instructor);
        return String.format("Instructor with ID %d was deleted", id);
    }

    private InstructorResponse instructorDtoResponse(Instructor instructor) {
        return modelMapper.map(instructor, InstructorResponse.class);
    }

    private boolean isNotBlankOrNotNull(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
