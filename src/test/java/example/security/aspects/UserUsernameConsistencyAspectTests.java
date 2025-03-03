package example.security.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserUsernameConsistencyAspectTests {
    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private UserAuthAspect userAuthAspect;

    @InjectMocks
    private UserUsernameConsistencyAspect userUsernameConsistencyAspect;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void checkUsernameConsistencyTest_ShouldReturnAuthHeaderError() throws Throwable {
        Object[] originalArgs = new Object[]{"test"};
        when(joinPoint.getArgs()).thenReturn(originalArgs);

        when(userAuthAspect.getAuthorizedUsername()).thenReturn(null);

        ResponseEntity<String> result = (ResponseEntity) userUsernameConsistencyAspect.checkUsernameConsistency(joinPoint);

        assertEquals("Authorization header is missing or invalid", result.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());

        verify(joinPoint, never()).proceed(any());
    }

    @Test
    public void checkUsernameConsistencyTest_ShouldReturnForbiddenError() throws Throwable {
        Object[] originalArgs = new Object[]{"test"};
        when(joinPoint.getArgs()).thenReturn(originalArgs);

        when(userAuthAspect.getAuthorizedUsername()).thenReturn("not test");

        ResponseEntity<String> result = (ResponseEntity) userUsernameConsistencyAspect.checkUsernameConsistency(joinPoint);

        assertEquals("Forbidden: You can not perform this operation", result.getBody());
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());

        verify(joinPoint, never()).proceed();
    }

    @Test
    public void checkUsernameConsistencyTest_ShouldProceed() throws Throwable {
        Object[] originalArgs = new Object[]{"test"};
        when(joinPoint.getArgs()).thenReturn(originalArgs);

        when(userAuthAspect.getAuthorizedUsername()).thenReturn("test");
        when(joinPoint.proceed()).thenReturn("success");

        String result = (String) userUsernameConsistencyAspect.checkUsernameConsistency(joinPoint);

        assertEquals("success", result);
        verify(joinPoint, times(1)).proceed();
    }
}
