package travel.winwin.authapi.infrastructure.persistance.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import travel.winwin.authapi.application.port.UserRepository;
import travel.winwin.authapi.domain.model.User;
import travel.winwin.authapi.infrastructure.persistance.entity.UserEntity;
import travel.winwin.authapi.infrastructure.persistance.mapper.UserMapper;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;


    @Override
    public void save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        jpaUserRepository.save(entity);
    }

    @Override
    public User findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .map(userMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("User not found by email: " + email));
    }

    @Override
    public boolean isEmailExist(String email) {
        return jpaUserRepository.findByEmail(email).isPresent();
    }
}
