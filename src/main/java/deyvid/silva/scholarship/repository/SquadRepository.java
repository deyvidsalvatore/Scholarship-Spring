package deyvid.silva.scholarship.repository;

import deyvid.silva.scholarship.entity.Squad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SquadRepository extends JpaRepository<Squad, Integer> {
}
