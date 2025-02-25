package example.services.imp;

import example.entities.User;
import example.exceptions.UserNotFoundException;
import example.repositories.UserRepository;
import example.services.UserService;
import example.validation.CustomValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final CustomValidator customValidator;

    public UserServiceImp(UserRepository userRepository, CustomValidator customValidator) {
        this.userRepository = userRepository;
        this.customValidator = customValidator;
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
        customValidator.validate(user);
        userRepository.save(user);
    }
}
