package example.services.imp;

import example.entities.User;
import example.exceptions.UserNotFoundException;
import example.repositories.UserRepository;
import example.services.UserService;
import example.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean existsUserByUsernameAndPassword(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        return user.isPresent();
    }

    @Override
    public void changeUserPassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsernameAndPassword(username, oldPassword)
                .orElseThrow(() -> new UserNotFoundException("There's no user with such username and password"));

        user.setPassword(newPassword);
        Validator.validate(user);
        userRepository.save(user);
    }
}
