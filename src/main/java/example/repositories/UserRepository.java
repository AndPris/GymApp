package example.repositories;

import example.entities.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
