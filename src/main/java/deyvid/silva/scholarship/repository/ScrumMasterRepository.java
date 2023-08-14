package deyvid.silva.scholarship.repository;

import deyvid.silva.scholarship.entity.ScrumMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrumMasterRepository extends JpaRepository<ScrumMaster, Integer> {
}
