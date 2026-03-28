package travel.winwin.authapi.infrastructure.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import travel.winwin.authapi.infrastructure.persistance.entity.ProcessingLogEntity;

import java.util.UUID;

@Repository
public interface JpaProcessingLogRepository extends JpaRepository<ProcessingLogEntity, UUID> {

}
