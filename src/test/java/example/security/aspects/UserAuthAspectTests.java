package example.security.aspects;

import example.security.services.UserAuthService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserAuthAspectTests {

    @Mock
    private UserAuthService userAuthService;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private UserAuthAspect userAuthAspect;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticationTest_ShouldProceed() throws Throwable {
        Object[] originalArgs = new Object[]{"Basic dGVzdC50ZXN0NDpnPTtmeTYtdVdm"};
        when(joinPoint.getArgs()).thenReturn(originalArgs);

        when(userAuthService.userExistsByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(true);
        when(joinPoint.proceed()).thenReturn("success");

        Object result = userAuthAspect.authentication(joinPoint);

        assertEquals("success", result);
        assertEquals("test.test4", UserAuthAspect.getAuthorizedUsername());
        verify(userAuthService, times(1)).userExistsByUsernameAndPassword(any(String.class), any(String.class));
        verify(joinPoint, times(1)).proceed();
    }

    @ParameterizedTest
    @MethodSource("invalidHeaders")
    void authenticationTest_ShouldReturnInvalidHeaderMessage(String header) throws Throwable {
        Object[] originalArgs = new Object[]{header};
        when(joinPoint.getArgs()).thenReturn(originalArgs);

        ResponseEntity<String> result = (ResponseEntity) userAuthAspect.authentication(joinPoint);

        assertEquals("Authorization header is missing or invalid", result.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());

        verify(userAuthService, never()).userExistsByUsernameAndPassword(any(String.class), any(String.class));
        verify(joinPoint, never()).proceed(any());
    }

    private static Stream<Arguments> invalidHeaders() {
        return Stream.of(
                null,
                Arguments.of("invalid"));
    }

    @Test
    void authenticationTest_ShouldReturnNoSuchUser() throws Throwable {
        Object[] originalArgs = new Object[]{"Basic dGVzdC50ZXN0NDpnPTtmeTYtdVdm"};
        when(joinPoint.getArgs()).thenReturn(originalArgs);

        when(userAuthService.userExistsByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(false);

        ResponseEntity<String> result = (ResponseEntity) userAuthAspect.authentication(joinPoint);

        assertEquals("Authorization fails: no such user", result.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());

        verify(userAuthService, times(1)).userExistsByUsernameAndPassword(any(String.class), any(String.class));
        verify(joinPoint, never()).proceed(originalArgs);
    }
}
