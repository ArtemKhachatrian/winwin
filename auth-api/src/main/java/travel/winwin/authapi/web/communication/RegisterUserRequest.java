package travel.winwin.authapi.web.communication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserRequest(@Email String email, @NotBlank String password) {
}
