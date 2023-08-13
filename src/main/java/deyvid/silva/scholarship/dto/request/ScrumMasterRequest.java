package deyvid.silva.scholarship.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrumMasterRequest {

    @NotNull(message = "firstName can't be null")
    @NotBlank(message = "firstName can't be blank")
    @Size(max = 30, message = "firstName (max 30 characters)")
    private String firstName;

    @NotNull(message = "firstName can't be null")
    @NotBlank(message = "lastName can't be blank")
    @Size(max = 30, message = "lastName (max 30 characters)")
    private String lastName;

    @NotBlank(message = "email can't be blank")
    @NotBlank(message = "email can't be empty")
    @Size(max = 150, message = "email (max 150 characters)")
    @Email(message = "Add a valid email Eg.: user@domain.com")
    private String email;
}
