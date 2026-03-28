package travel.winwin.authapi.web.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import travel.winwin.authapi.application.exception.InvalidPasswordAuthException;
import travel.winwin.authapi.application.exception.UserAlreadyExistException;
import travel.winwin.authapi.web.communication.ErrorResponse;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidPasswordAuthException.class)
    public ResponseEntity<ErrorResponse> invalidPassword(InvalidPasswordAuthException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), 401, System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFound(EntityNotFoundException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), 404, System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> userAlreadyExist(UserAlreadyExistException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), 409, System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationFailed(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining("; "));
        ErrorResponse response = new ErrorResponse(errorMessage, 400, System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> genericException(Exception e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), 500, System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> responseStatusException(ResponseStatusException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), e.getBody().getStatus(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.valueOf(e.getBody().getStatus())).body(response);
    }
}
