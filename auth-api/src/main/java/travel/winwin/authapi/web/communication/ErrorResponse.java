package travel.winwin.authapi.web.communication;

public record ErrorResponse(
        String message,
        int status,
        Long timestamp
) {
}
