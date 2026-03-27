package travel.winwin.authapi.infrastructure.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {


    @Id
    private UUID id;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

}
