package deyvid.silva.scholarship.dto.request;

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
public class SquadRequest {

    @NotNull(message = "firstName can't be null")
    @NotBlank(message = "firstName can't be empty")
    @Size(min = 3, max = 20, message = "squadName (min 3) (max 20)")
    private String squadName;

    private List<Integer> students;
}
