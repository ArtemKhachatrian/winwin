package travel.winwin.dataapi.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import travel.winwin.dataapi.application.usecase.TransformUseCase;
import travel.winwin.dataapi.web.communication.ErrorResponse;
import travel.winwin.dataapi.web.communication.TransformationRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transform")
public class TransformController {

    private final TransformUseCase transformUseCase;
    @Value("${winwin.internal.token}")
    private String internalToken;

    @PostMapping
    public ResponseEntity<?> transform(@RequestHeader(value = "X-Internal-Token", required = false) String token,
                                       @RequestBody TransformationRequest request) {
        if (!StringUtils.hasText(token) || !token.equals(internalToken)) {
            ErrorResponse response = new ErrorResponse("Forbidden: invalid or missing X-Internal-Token", 403, System.currentTimeMillis());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(response);
        }

        return ResponseEntity.ok(transformUseCase.execute(request.text()));
    }
}
