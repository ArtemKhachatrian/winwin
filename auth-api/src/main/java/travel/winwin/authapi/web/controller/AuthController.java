package travel.winwin.authapi.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import travel.winwin.authapi.application.command.LoginUserCommand;
import travel.winwin.authapi.application.command.RegisterUserCommand;
import travel.winwin.authapi.application.usecase.LoginUserUseCase;
import travel.winwin.authapi.application.usecase.RegisterUserUseCase;
import travel.winwin.authapi.web.communication.LoginUserRequest;
import travel.winwin.authapi.web.communication.RegisterUserRequest;
import travel.winwin.authapi.web.config.security.JwtUtil;
import travel.winwin.authapi.web.mapper.AuthMapper;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthMapper authMapper;
    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Void> register(RegisterUserRequest request) {
        RegisterUserCommand command = authMapper.toCommand(request);
        registerUserUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(LoginUserRequest request) {
        LoginUserCommand command = authMapper.toCommand(request);
        return ResponseEntity.ok(loginUserUseCase.execute(command));
    }
}