package travel.winwin.authapi.web.mapper;

import org.mapstruct.Mapper;
import travel.winwin.authapi.application.command.LoginUserCommand;
import travel.winwin.authapi.application.command.RegisterUserCommand;
import travel.winwin.authapi.web.communication.LoginUserRequest;
import travel.winwin.authapi.web.communication.RegisterUserRequest;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    RegisterUserCommand toCommand(RegisterUserRequest request);

    LoginUserCommand toCommand(LoginUserRequest request);


}
