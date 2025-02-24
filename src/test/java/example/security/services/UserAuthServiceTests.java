package example.security.services;

import example.entities.Trainee;
import example.entities.User;
import example.repositories.UserRepository;
import example.repositories.imp.UserRepositoryImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserAuthServiceTests {
    private UserRepository userRepository;
    private UserAuthService userAuthService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepositoryImp.class);
        userAuthService = new UserAuthService(userRepository);
    }

    @Test
    public void authenticateUser_ShouldReturnFalse() {
        when(userRepository.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(Optional.empty());
        assertFalse(userAuthService.userExistsByUsernameAndPassword("test", "test"));
    }

    @Test
    public void authenticateUser_ShouldReturnTrue() {
        User user = new Trainee();
        when(userRepository.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(Optional.of(user));
        assertTrue(userAuthService.userExistsByUsernameAndPassword("test", "test"));
    }
}
