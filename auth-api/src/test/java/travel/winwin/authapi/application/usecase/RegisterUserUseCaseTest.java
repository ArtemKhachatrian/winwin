package travel.winwin.authapi.application.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import travel.winwin.authapi.application.command.RegisterUserCommand;
import travel.winwin.authapi.application.exception.UserAlreadyExistException;
import travel.winwin.authapi.application.port.UserRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    RegisterUserUseCase registerUserUseCase;

    @Test
    void whenRegisterUserUseCaseExecuteThenUserRepositorySaveCorrectUser() {
        String userEmail = "1234@gmail.com";
        String userPassword = "1234";
        String encoded = "4321";
        RegisterUserCommand registerUserCommand = new RegisterUserCommand(userEmail, userPassword);

        when(userRepository.isEmailExist(userEmail)).thenReturn(false);
        when(passwordEncoder.encode(userPassword)).thenReturn(encoded);

        registerUserUseCase.execute(registerUserCommand);

        verify(userRepository).isEmailExist(userEmail);
        verify(passwordEncoder).encode(userPassword);
        verify(userRepository).save(argThat(user -> user.getId() != null &&
                user.getEmail().equals(userEmail) &&
                user.getPasswordHash().equals(encoded)));
    }

    @Test
    void whenUserExistThenThrowUserAlreadyExistException() {
        String userEmail = "1234@gmail.com";
        String userPassword = "1234";
        RegisterUserCommand registerUserCommand = new RegisterUserCommand(userEmail, userPassword);

        when(userRepository.isEmailExist(userEmail)).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> registerUserUseCase.execute(registerUserCommand));
    }
}