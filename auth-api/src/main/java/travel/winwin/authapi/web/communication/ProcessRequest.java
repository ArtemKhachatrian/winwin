package travel.winwin.authapi.web.communication;

import jakarta.validation.constraints.NotBlank;

public record ProcessRequest(@NotBlank String text) {
}
