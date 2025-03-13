package example.security;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void loginFailed_IncrementsAttemptsAndLocksAfterMaxAttempts() {
        when(request.getRemoteAddr()).thenReturn("192.168.0.1");

        loginAttemptService.loginFailed();
        loginAttemptService.loginFailed();
        loginAttemptService.loginFailed();

        assertTrue(loginAttemptService.isBlocked());
    }

    @Test
    void isBlocked_ReturnsFalse_WhenUnderMaxAttempts() {
        when(request.getRemoteAddr()).thenReturn("192.168.0.2");

        loginAttemptService.loginFailed();
        loginAttemptService.loginFailed();

        assertFalse(loginAttemptService.isBlocked());
    }

    @Test
    void isBlocked_ReturnsFalse_AfterBlockTimeExpires() throws InterruptedException {
        when(request.getRemoteAddr()).thenReturn("192.168.0.3");
        loginAttemptService.setBlockTime(500);

        loginAttemptService.loginFailed();
        loginAttemptService.loginFailed();
        loginAttemptService.loginFailed();

        assertTrue(loginAttemptService.isBlocked());

        Thread.sleep(600);

        assertFalse(loginAttemptService.isBlocked());
    }

    @Test
    void getClientIP_ReturnsCorrectIp_FromForwardedHeader() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.1, 192.168.0.1");
        when(request.getRemoteAddr()).thenReturn("192.168.0.1");

        assertFalse(loginAttemptService.isBlocked());
    }

    @Test
    void getClientIP_ReturnsRemoteAddr_WhenNoForwardedHeader() {
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("192.168.0.4");

        assertFalse(loginAttemptService.isBlocked());
    }
}