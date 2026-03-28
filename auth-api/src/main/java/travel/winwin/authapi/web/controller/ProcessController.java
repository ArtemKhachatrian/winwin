package travel.winwin.authapi.web.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import travel.winwin.authapi.application.usecase.ProcessUseCase;
import travel.winwin.authapi.web.communication.ProcessRequest;
import travel.winwin.authapi.web.config.security.AuthPrincipal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/process")
public class ProcessController {
    private final ProcessUseCase processUseCase;

    @PostMapping
    public ResponseEntity<String> process(
            @AuthenticationPrincipal AuthPrincipal authPrincipal,
            @RequestBody ProcessRequest request) {
        return ResponseEntity.ok(processUseCase.execute(authPrincipal.id(), request.text()));
    }
}
