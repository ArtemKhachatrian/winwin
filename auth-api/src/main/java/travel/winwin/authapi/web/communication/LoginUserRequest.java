package travel.winwin.authapi.web.communication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserRequest(@NotBlank String password, @Email String email) {
}
