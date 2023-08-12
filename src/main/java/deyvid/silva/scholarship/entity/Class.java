package deyvid.silva.scholarship.entity;

import deyvid.silva.scholarship.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne
    @JoinColumn(name = "coordinator_id")
    private Coordinator coordinator;

    @OneToOne
    @JoinColumn(name = "scrum_master_id")
    private ScrumMaster scrumMaster;

    @OneToMany(mappedBy = "studentClass")
    private List<Squad> squads;

    @ManyToMany
    @JoinTable(
            name = "class_instructor",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "instructor_id")
    )
    private List<Instructor> instructors;

}
