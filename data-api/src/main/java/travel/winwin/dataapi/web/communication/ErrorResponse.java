package travel.winwin.dataapi.web.communication;

public record ErrorResponse(
        String message,
        int status,
        Long timestamp
) {
}
