package travel.winwin.authapi.application.usecase;

import travel.winwin.authapi.application.command.LoginUserCommand;
import travel.winwin.authapi.application.port.UserRepository;

public class LoginUserUseCase {
    private final UserRepository userRepository;

    public LoginUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String execute(LoginUserCommand command) {
        return null;
    }
}
