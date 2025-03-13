package example.controllers;

import example.dtos.CredentialsDTO;
import example.exceptions.IPAddressBlockedException;
import example.security.LoginAttemptService;
import example.security.BlacklistService;
import example.security.jwt.JwtTokenUtil;
import example.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LoginControllerTests {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private LoginAttemptService loginAttemptService;

    @Mock
    private BlacklistService blacklistService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ShouldReturnBlockedResponse_WhenBlocked() {
        CredentialsDTO credentialsDTO = new CredentialsDTO();
        credentialsDTO.setUsername("testUser");
        credentialsDTO.setPassword("testPassword");

        when(loginAttemptService.isBlocked()).thenReturn(true);

        Exception e = assertThrows(IPAddressBlockedException.class, () -> loginController.login(credentialsDTO));
        assertEquals("Try to login later, please", e.getMessage());
        verify(loginAttemptService).isBlocked();
        verifyNoInteractions(authenticationManager, jwtTokenUtil);
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenAuthenticationFails() {
        CredentialsDTO credentialsDTO = new CredentialsDTO();
        credentialsDTO.setUsername("testUser");
        credentialsDTO.setPassword("wrongPassword");

        when(loginAttemptService.isBlocked()).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        ResponseEntity<String> response = loginController.login(credentialsDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username and password", response.getBody());
        verify(loginAttemptService).isBlocked();
        verify(loginAttemptService).loginFailed();
        verifyNoInteractions(jwtTokenUtil);
    }

    @Test
    void login_ShouldReturnOk_WhenAuthenticationSucceeds() {
        CredentialsDTO credentialsDTO = new CredentialsDTO();
        credentialsDTO.setUsername("testUser");
        credentialsDTO.setPassword("testPassword");

        Authentication authentication = mock(Authentication.class);
        User user = mock(User.class);

        when(loginAttemptService.isBlocked()).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtTokenUtil.generateAccessToken(user, 1000 * 60)).thenReturn("jwt-token");

        ResponseEntity<String> response = loginController.login(credentialsDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token", response.getBody());
        verify(loginAttemptService).isBlocked();
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenUtil).generateAccessToken(user, 1000 * 60);
    }

    @Test
    void logout_ShouldReturnBadRequest_WhenAuthorizationHeaderIsInvalid() {
        String invalidToken = "InvalidHeader";

        ResponseEntity<String> response = loginController.logout(invalidToken, null, null, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid authorization header", response.getBody());
        verifyNoInteractions(blacklistService);
    }

    @Test
    void logout_ShouldReturnOk_WhenLogoutSucceeds() {
        String token = "Bearer jwt-token";
        Authentication authentication = mock(Authentication.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        ResponseEntity<String> controllerResponse = loginController.logout(token, authentication, request, response);

        assertEquals(HttpStatus.OK, controllerResponse.getStatusCode());
        assertEquals("Logged out successfully", controllerResponse.getBody());
        verify(blacklistService).blacklistToken("jwt-token");
    }
}
