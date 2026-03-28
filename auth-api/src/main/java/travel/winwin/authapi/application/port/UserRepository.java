package travel.winwin.authapi.application.port;

import travel.winwin.authapi.domain.model.User;

public interface UserRepository {

    void save(User user);

    User findByEmail(String email);

    boolean isEmailExist(String email);
}
