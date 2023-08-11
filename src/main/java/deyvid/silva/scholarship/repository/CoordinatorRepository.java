package deyvid.silva.scholarship.repository;

import deyvid.silva.scholarship.entity.Coordinator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinatorRepository extends JpaRepository<Coordinator, Integer> {
}
