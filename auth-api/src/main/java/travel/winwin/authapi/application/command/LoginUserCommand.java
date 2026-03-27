package travel.winwin.authapi.application.command;

public record LoginUserCommand(String password, String email) {
}
