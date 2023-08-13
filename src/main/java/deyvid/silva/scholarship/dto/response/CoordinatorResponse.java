package deyvid.silva.scholarship.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import deyvid.silva.scholarship.entity.Class;
import lombok.Data;

import java.util.List;

@Data
public class CoordinatorResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private List<Class> coordinatorClasses;
}
