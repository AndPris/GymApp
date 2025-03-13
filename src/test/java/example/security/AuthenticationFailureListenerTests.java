package example.security;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.verify;

class AuthenticationFailureListenerTests {
    @InjectMocks
    private AuthenticationFailureListener listener;

    @Mock
    private HttpServletRequest request;

    @Mock
    private LoginAttemptService loginAttemptService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void onApplicationEvent_ShouldCallLoginFailed_WithRemoteAddr_WhenNoForwardedHeader() {
        Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        Mockito.when(request.getRemoteAddr()).thenReturn("192.168.1.1");

        AuthenticationFailureBadCredentialsEvent event = new AuthenticationFailureBadCredentialsEvent(authentication, new BadCredentialsException("test"));
        listener.onApplicationEvent(event);

        verify(loginAttemptService).loginFailed("192.168.1.1");
    }

    @Test
    void onApplicationEvent_ShouldCallLoginFailed_WithForwardedIP_WhenHeaderExists() {
        Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.42, 192.168.1.1");
        Mockito.when(request.getRemoteAddr()).thenReturn("192.168.1.1");

        AuthenticationFailureBadCredentialsEvent event = new AuthenticationFailureBadCredentialsEvent(authentication, new BadCredentialsException("test"));
        listener.onApplicationEvent(event);

        verify(loginAttemptService).loginFailed("203.0.113.42");
    }

    @Test
    void onApplicationEvent_ShouldCallLoginFailed_WithRemoteAddr_WhenForwardedHeaderDoesNotMatchRemoteAddr() {
        Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.42");
        Mockito.when(request.getRemoteAddr()).thenReturn("192.168.1.1");

        AuthenticationFailureBadCredentialsEvent event = new AuthenticationFailureBadCredentialsEvent(authentication, new BadCredentialsException("test"));
        listener.onApplicationEvent(event);

        verify(loginAttemptService).loginFailed("192.168.1.1");
    }
}
