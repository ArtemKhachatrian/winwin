package travel.winwin.authapi.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import travel.winwin.authapi.application.port.ProcessingLogRepository;
import travel.winwin.authapi.domain.model.ProcessingLog;
import travel.winwin.authapi.infrastructure.client.DataTransformationClient;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProcessUseCase {
    private final ProcessingLogRepository processingLogRepository;
    private final DataTransformationClient dataTransformationClient;

    public String execute(UUID id, String text) {
        String transformationResult = dataTransformationClient.getTransformationResult(text);
        ProcessingLog processingLog = new ProcessingLog(UUID.randomUUID(), id, text, transformationResult, LocalDateTime.now());
        processingLogRepository.save(processingLog);

        return transformationResult;
    }
}
