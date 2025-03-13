package example.security.jwt;

import example.entities.Trainee;
import example.entities.User;
import example.repositories.UserRepository;
import example.security.BlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

class JwtTokenFilterTests {

    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BlacklistService blacklistService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private final String validToken = "validToken";
    private final String invalidToken = "invalidToken";
    private final String blacklistedToken = "blacklistedToken";
    private final String username = "testUser";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldContinueFilterWhenNoAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldContinueFilterWhenTokenIsInvalid() throws ServletException, IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + invalidToken);
        when(jwtTokenUtil.validate(invalidToken)).thenReturn(false);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldContinueFilterWhenTokenIsBlacklisted() throws ServletException, IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + blacklistedToken);
        when(jwtTokenUtil.validate(blacklistedToken)).thenReturn(true);
        when(blacklistService.isTokenBlacklisted(blacklistedToken)).thenReturn(true);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldAuthenticateWhenTokenIsValid() throws ServletException, IOException {
        User user = new Trainee();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + validToken);
        when(jwtTokenUtil.validate(validToken)).thenReturn(true);
        when(blacklistService.isTokenBlacklisted(validToken)).thenReturn(false);
        when(jwtTokenUtil.getUsername(validToken)).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}
