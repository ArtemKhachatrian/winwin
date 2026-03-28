package travel.winwin.authapi.web.config.security;

import java.util.UUID;

public record AuthPrincipal(UUID id, String email) {
}
