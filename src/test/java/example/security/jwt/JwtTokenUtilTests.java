package example.security.jwt;

import example.entities.Trainee;
import example.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;


import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenUtilTests {

    private JwtTokenUtil jwtTokenUtil;
    private String secret = "fdgkl3oignsdboinb5jwmnb5jwpejwnbvn5jweovnmvbmbmn34fbqb3ncxz2mncv";

    @BeforeEach
    public void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", secret);
        jwtTokenUtil.init();
    }

    @Test
    public void testGenerateAccessToken() {
        User user = new Trainee();
        user.setUsername("testUser");

        String token = jwtTokenUtil.generateAccessToken(user, 60000);
        assertNotNull(token);
    }

    @Test
    public void testValidateToken_validToken() {
        User user = new Trainee();
        user.setUsername("validUser");
        String token = jwtTokenUtil.generateAccessToken(user, 60000);

        assertTrue(jwtTokenUtil.validate(token));
    }

    @Test
    public void testValidateToken_invalidToken() {
        assertFalse(jwtTokenUtil.validate("invalid.token.here"));
    }

    @Test
    public void testGetUsername_validToken() {
        User user = new Trainee();
        user.setUsername("testUser");
        String token = jwtTokenUtil.generateAccessToken(user, 60000);

        assertEquals("testUser", jwtTokenUtil.getUsername(token));
    }

    @Test
    public void testGetUsername_invalidToken() {
        assertNull(jwtTokenUtil.getUsername("invalid.token.here"));
    }

    @Test
    public void testIsTokenExpired_notExpiredToken() {
        User user = new Trainee();
        user.setUsername("testUser");
        String token = jwtTokenUtil.generateAccessToken(user, 60000);

        assertFalse(jwtTokenUtil.isTokenExpired(token));
    }

    @Test
    public void testIsTokenExpired_expiredToken() {
        User user = new Trainee();
        user.setUsername("testUser");
        String token = jwtTokenUtil.generateAccessToken(user, -60000);

        assertTrue(jwtTokenUtil.isTokenExpired(token));
    }

    @Test
    public void testIsTokenExpired_invalidToken() {
        assertTrue(jwtTokenUtil.isTokenExpired("invalid.token.here"));
    }
}
