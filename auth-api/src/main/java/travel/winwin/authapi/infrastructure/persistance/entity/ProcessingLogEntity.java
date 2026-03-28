package travel.winwin.authapi.infrastructure.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "processing_log")
public class ProcessingLogEntity {

    @Id
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "input_text")
    private String inputText;

    @Column(name = "output_text")
    private String outputText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
