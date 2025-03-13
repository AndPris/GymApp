package example.security;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class LoginAttemptServiceTests {
    @InjectMocks
    private LoginAttemptService loginAttemptService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginFailed_IncreasesAttemptsCount() {
        Mockito.when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        loginAttemptService.loginFailed("127.0.0.1");
        loginAttemptService.loginFailed("127.0.0.1");

        assertFalse(loginAttemptService.isBlocked());
    }

    @Test
    void loginFailed_BlocksAfterMaxAttempts() {
        Mockito.when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        loginAttemptService.loginFailed("127.0.0.1");
        loginAttemptService.loginFailed("127.0.0.1");
        loginAttemptService.loginFailed("127.0.0.1");

        assertTrue(loginAttemptService.isBlocked());
    }

    @Test
    void isBlocked_ReturnsFalse_AfterLockExpires() throws InterruptedException {
        Mockito.when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        loginAttemptService.setBlockTime(1000);

        loginAttemptService.loginFailed("127.0.0.1");
        loginAttemptService.loginFailed("127.0.0.1");
        loginAttemptService.loginFailed("127.0.0.1");

        assertTrue(loginAttemptService.isBlocked());

        Thread.sleep(1100);

        assertFalse(loginAttemptService.isBlocked());
    }
}
