package deyvid.silva.scholarship.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import deyvid.silva.scholarship.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassRequest {

    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be empty")
    @Size(min = 3, max = 30, message = "name (min 3) (max 30)")
    private String name;

    private String status;

    @JsonProperty(value = "coordinator")
    private Integer coordinatorId;

    @JsonProperty(value = "scrumMaster")
    private Integer scrumMasterId;

    @JsonProperty(value = "instructors")
    private List<Integer> instructorIds;

    @JsonProperty(value = "squads")
    private List<Integer> squadIds;

}
