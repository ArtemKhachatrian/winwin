package travel.winwin.authapi.infrastructure.persistance.mapper;

import org.mapstruct.Mapper;
import travel.winwin.authapi.domain.model.ProcessingLog;
import travel.winwin.authapi.infrastructure.persistance.entity.ProcessingLogEntity;

@Mapper(componentModel = "spring")
public interface ProcessingLogMapper {

    ProcessingLogEntity toEntity(ProcessingLog processingLog);
}
