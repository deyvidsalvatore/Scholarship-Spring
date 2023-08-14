package deyvid.silva.scholarship.entity;

import deyvid.silva.scholarship.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @ManyToMany
    @JoinTable(
            name = "class_squad",
            joinColumns = @JoinColumn(name = "squad_id"),
            inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private List<Squad> squads;

    @ManyToMany
    @JoinTable(
            name = "class_instructor",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "instructor_id")
    )
    private List<Instructor> instructors;

}
