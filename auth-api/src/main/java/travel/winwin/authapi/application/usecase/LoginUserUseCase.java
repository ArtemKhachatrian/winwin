package travel.winwin.authapi.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import travel.winwin.authapi.application.command.LoginUserCommand;
import travel.winwin.authapi.application.exception.InvalidPasswordAuthException;
import travel.winwin.authapi.application.port.UserRepository;
import travel.winwin.authapi.domain.model.User;
import travel.winwin.authapi.web.config.security.JwtUtil;

@Component
@RequiredArgsConstructor
public class LoginUserUseCase {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public String execute(LoginUserCommand command) {
        User byEmail = userRepository.findByEmail(command.email());
        if (passwordEncoder.matches(command.password(), byEmail.getPasswordHash())) {
            return jwtUtil.generateToken(command.email(), byEmail.getId());
        }
        throw new InvalidPasswordAuthException();
    }
}
