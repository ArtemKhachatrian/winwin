package travel.winwin.authapi.application.exception;

public class InvalidPasswordAuthException extends RuntimeException {

    static final String INVALID_CREDENTIALS = "Invalid credentials";

    public InvalidPasswordAuthException() {
        super(INVALID_CREDENTIALS);
    }

}
