package example.services;

import example.entities.Trainee;
import example.entities.User;
import example.exceptions.UserNotFoundException;
import example.repositories.UserRepository;
import example.services.imp.UserServiceImp;
import example.validation.CustomValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTests {
    private UserRepository userRepository;
    private CustomValidator customValidator;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        customValidator = mock(CustomValidator.class);
        passwordEncoder = mock(PasswordEncoder.class);

        userService = new UserServiceImp(userRepository, customValidator, passwordEncoder);
    }

    @Test
    public void existsUserByUsernameAndPasswordTest_ShouldThrow() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        Exception ex = assertThrows(UserNotFoundException.class,
                () -> userService.existsUserByUsernameAndPassword("test", "test"));
        assertEquals(ex.getMessage(), "There's no user with such username");
    }

    @Test
    public void existsUserByUsernameAndPasswordTest_ShouldReturnFalse() {
        User user = new Trainee();
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(false);

        assertFalse(userService.existsUserByUsernameAndPassword("test", "test"));
    }

    @Test
    public void existsUserByUsernameAndPasswordTest_ShouldReturnTrue() {
        User user = new Trainee();
        user.setPassword("test")
        ;
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);

        assertTrue(userService.existsUserByUsernameAndPassword("test", "test"));
    }

    @Test
    public void changeUserPassword_ShouldThrowUserNotFoundException1() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        Exception e = assertThrows(UserNotFoundException.class,
                () -> userService.changeUserPassword("1", "1", "1"));
        assertEquals("There's no user with such username and password", e.getMessage());
    }

    @Test
    public void changeUserPassword_ShouldThrowUserNotFoundException2() {
        User user = new Trainee();
        user.setPassword("test");
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(false);

        Exception e = assertThrows(UserNotFoundException.class,
                () -> userService.changeUserPassword("1", "1", "1"));
        assertEquals("There's no user with such username and password", e.getMessage());
    }

    @Test
    public void changeUserPassword_ShouldChange() {
        User user = new Trainee();
        user.setUsername("username");
        user.setPassword("password");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encoded");

        userService.changeUserPassword("username", "password", "newPass");
        assertEquals("encoded", user.getPassword());
    }
}
