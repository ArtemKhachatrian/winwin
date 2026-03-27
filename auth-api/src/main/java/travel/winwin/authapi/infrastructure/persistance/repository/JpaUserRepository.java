package travel.winwin.authapi.infrastructure.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travel.winwin.authapi.infrastructure.persistance.entity.UserEntity;

import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {
}
