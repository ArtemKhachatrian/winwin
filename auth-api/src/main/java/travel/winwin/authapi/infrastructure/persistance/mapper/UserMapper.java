package travel.winwin.authapi.infrastructure.persistance.mapper;

import org.mapstruct.Mapper;
import travel.winwin.authapi.domain.model.User;
import travel.winwin.authapi.infrastructure.persistance.entity.UserEntity;

@Mapper
public interface UserMapper {

    UserEntity toEntity(User user);

    User toDomain(UserEntity user);
}
