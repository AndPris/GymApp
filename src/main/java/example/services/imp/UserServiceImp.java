package example.services.imp;

import example.entities.User;
import example.exceptions.UserNotFoundException;
import example.repositories.UserRepository;
import example.services.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private Validator validator;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
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
        validateUser(user);
        userRepository.save(user);
    }

    private void validateUser(User user) {
        for (ConstraintViolation<User> violation : validator.validate(user)) {
            throw new ValidationException("Validation error: " + violation.getMessage());
        }
    }
}
