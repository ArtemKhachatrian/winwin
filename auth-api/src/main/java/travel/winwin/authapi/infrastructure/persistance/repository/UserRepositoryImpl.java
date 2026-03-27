package travel.winwin.authapi.infrastructure.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import travel.winwin.authapi.application.port.UserRepository;
import travel.winwin.authapi.domain.model.User;
import travel.winwin.authapi.infrastructure.persistance.entity.UserEntity;
import travel.winwin.authapi.infrastructure.persistance.mapper.UserMapper;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;


    @Override
    public void save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        jpaUserRepository.save(entity);
    }
}
