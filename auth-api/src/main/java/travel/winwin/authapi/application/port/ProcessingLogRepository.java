package travel.winwin.authapi.application.port;

import travel.winwin.authapi.domain.model.ProcessingLog;

public interface ProcessingLogRepository {

    void save(ProcessingLog processingLog);
}
