package travel.winwin.authapi.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import travel.winwin.authapi.application.command.RegisterUserCommand;
import travel.winwin.authapi.application.exception.UserAlreadyExistException;
import travel.winwin.authapi.application.port.UserRepository;
import travel.winwin.authapi.domain.model.User;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void execute(RegisterUserCommand registerUserCommand) {
        if (userRepository.isEmailExist(registerUserCommand.email())) {
            throw new UserAlreadyExistException(registerUserCommand.email());
        }
        String encoded = passwordEncoder.encode(registerUserCommand.password());
        User user = new User(UUID.randomUUID(), registerUserCommand.email(), encoded);
        userRepository.save(user);
    }


}
