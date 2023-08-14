package deyvid.silva.scholarship.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class SquadResponse {
    private Long id;
    private String squadName;
    @JsonIgnore
    private ClassResponse studentClass;
    private List<StudentResponse> students;
}
