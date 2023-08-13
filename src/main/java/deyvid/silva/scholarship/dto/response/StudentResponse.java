package deyvid.silva.scholarship.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class StudentResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private List<SquadResponse> squads;
}
