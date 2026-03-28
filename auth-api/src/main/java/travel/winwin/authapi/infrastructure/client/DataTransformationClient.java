package travel.winwin.authapi.infrastructure.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import travel.winwin.authapi.infrastructure.dto.TransformationRequest;

@RequiredArgsConstructor
@Component
public class DataTransformationClient {

    private final RestTemplate restTemplate;

    @Value("${winwin.internal.token}")
    private String internalToken;

    @Value("${winwin.data.api.url}")
    private String dataApiUrl;

    public String getTransformationResult(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Internal-Token", internalToken);

        HttpEntity<TransformationRequest> entity = new HttpEntity<>(new TransformationRequest(text), headers);

        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(dataApiUrl + "/api/transform", entity, String.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "data-api unreachable: " + e.getMessage());
        }

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "data-api returned error");
        }

        return response.getBody();
    }

}
