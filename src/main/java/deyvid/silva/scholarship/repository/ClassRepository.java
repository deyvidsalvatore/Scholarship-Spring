package deyvid.silva.scholarship.repository;

import deyvid.silva.scholarship.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Integer> {
    List<Class> findByCoordinator_Id(Integer coordinatorId);
    List<Class> findByInstructors_Id(Integer instructorId);
    List<Class> findByScrumMaster_Id(Integer scrumMasterId);
}
