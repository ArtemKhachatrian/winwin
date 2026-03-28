package travel.winwin.authapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ProcessingLog {

    private UUID id;
    private UUID userId;
    private String inputText;
    private String outputText;
    private LocalDateTime createdAt;
}
