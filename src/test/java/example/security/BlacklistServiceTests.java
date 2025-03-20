package example.security;

import example.security.jwt.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlacklistServiceTests {
    @InjectMocks
    private BlacklistService blacklistService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddTokenToBlacklist() {
        String token = "test-token";

        blacklistService.blacklistToken(token);

        assertTrue(blacklistService.isTokenBlacklisted(token));
    }

    @Test
    void shouldReturnFalseForNonBlacklistedToken() {
        String token = "non-blacklisted-token";

        assertFalse(blacklistService.isTokenBlacklisted(token));
    }

    @Test
    void shouldCleanupExpiredTokens() {
        String expiredToken = "expired-token";
        String validToken = "valid-token";

        blacklistService.blacklistToken(expiredToken);
        blacklistService.blacklistToken(validToken);

        Mockito.when(jwtTokenUtil.isTokenExpired(expiredToken)).thenReturn(true);
        Mockito.when(jwtTokenUtil.isTokenExpired(validToken)).thenReturn(false);

        blacklistService.cleanupExpiredTokens();

        assertFalse(blacklistService.isTokenBlacklisted(expiredToken));
        assertTrue(blacklistService.isTokenBlacklisted(validToken));
    }
}
