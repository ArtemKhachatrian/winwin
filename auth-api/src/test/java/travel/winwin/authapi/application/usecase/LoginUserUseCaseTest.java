package travel.winwin.authapi.application.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import travel.winwin.authapi.application.command.LoginUserCommand;
import travel.winwin.authapi.application.exception.InvalidPasswordAuthException;
import travel.winwin.authapi.application.port.UserRepository;
import travel.winwin.authapi.domain.model.User;
import travel.winwin.authapi.web.config.security.JwtUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    @Mock
    UserRepository userRepository;
    @Mock
    JwtUtil jwtUtil;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    LoginUserUseCase loginUserUseCase;

    @Test
    void whenUserCredentialsCorrectThenGenerateToken() {
        String userEmail = "1234@gmail.com";
        String userPassword = "1234";
        LoginUserCommand loginUserCommand = new LoginUserCommand(userPassword, userEmail);
        User user = new User(null, userEmail, userPassword);
        String expected = "token";

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(passwordEncoder.matches(user.getPasswordHash(), userPassword)).thenReturn(true);
        when(jwtUtil.generateToken(loginUserCommand.email(), user.getId())).thenReturn(expected);

        String actual = loginUserUseCase.execute(loginUserCommand);

        assertEquals(expected, actual);
        verify(jwtUtil).generateToken(loginUserCommand.email(), user.getId());
    }

    @Test
    void whenUserPasswordIsIncorrectThenThrowInvalidPasswordAuthException() {
        String userEmail = "1234@gmail.com";
        String userPassword = "1234";
        User user = new User(null, userEmail, userPassword);
        LoginUserCommand loginUserCommand = new LoginUserCommand(userPassword, userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(passwordEncoder.matches(user.getPasswordHash(), userPassword)).thenReturn(false);

        assertThrows(InvalidPasswordAuthException.class, () -> loginUserUseCase.execute(loginUserCommand));
    }
}