package example.services.imp;

import example.entities.User;
import example.exceptions.UserNotFoundException;
import example.repositories.UserRepository;
import example.services.UserService;
import example.validation.CustomValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final CustomValidator customValidator;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepository userRepository, CustomValidator customValidator,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customValidator = customValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean existsUserByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("There's no user with such username"));

        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public void changeUserPassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("There's no user with such username and password"));

        if(!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UserNotFoundException("There's no user with such username and password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        customValidator.validate(user);
        userRepository.save(user);
    }
}
