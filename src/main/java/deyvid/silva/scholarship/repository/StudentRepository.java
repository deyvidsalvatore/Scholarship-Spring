package deyvid.silva.scholarship.repository;

import deyvid.silva.scholarship.entity.Squad;
import deyvid.silva.scholarship.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query("SELECT s.squads FROM Student s WHERE s.id = :studentId")
    List<Squad> findSquadsByStudentId(@Param("studentId") Integer studentId);
}
