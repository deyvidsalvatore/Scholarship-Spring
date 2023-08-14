package deyvid.silva.scholarship.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ClassResponse {
    private Integer id;
    private String name;
    private String status;
    private CoordinatorResponse coordinator;
    private ScrumMasterResponse scrumMaster;
    private List<SquadResponse> squads;
    private List<InstructorResponse> instructors;
    private Integer totalStudents;
}
