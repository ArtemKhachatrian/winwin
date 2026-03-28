package travel.winwin.authapi.infrastructure.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import travel.winwin.authapi.application.port.ProcessingLogRepository;
import travel.winwin.authapi.domain.model.ProcessingLog;
import travel.winwin.authapi.infrastructure.persistance.entity.ProcessingLogEntity;
import travel.winwin.authapi.infrastructure.persistance.mapper.ProcessingLogMapper;

@Repository
@RequiredArgsConstructor
public class ProcessingLogRepositoryImpl implements ProcessingLogRepository {
    private final JpaProcessingLogRepository processingLogRepository;
    private final ProcessingLogMapper processingLogMapper;

    @Override
    public void save(ProcessingLog processingLog) {
        ProcessingLogEntity entity = processingLogMapper.toEntity(processingLog);
        processingLogRepository.save(entity);
    }
}
