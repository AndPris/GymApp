package example.services;

import example.exceptions.AuthorizationException;
import example.services.imp.AuthorizationServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthorizationServiceTests {
    @InjectMocks
    private AuthorizationServiceImp authorizationService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void authorize_Success_WhenUsernamesMatch() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("validUser");

        assertDoesNotThrow(() -> authorizationService.authorize("validUser"));
    }

    @Test
    void authorize_ThrowsException_WhenUsernamesDoNotMatch() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("validUser");

        AuthorizationException exception = assertThrows(AuthorizationException.class,
                () -> authorizationService.authorize("invalidUser"));

        assertEquals("You are not allowed to perform this operation!", exception.getMessage());
    }

    @Test
    void authorize_ThrowsException_WhenAuthenticationIsNull() {
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> authorizationService.authorize("someUser"));
    }
}
