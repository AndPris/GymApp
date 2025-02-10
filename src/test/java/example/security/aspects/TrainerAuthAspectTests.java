package example.security.aspects;

import example.entities.Trainer;
import example.security.services.TrainerAuthService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TrainerAuthAspectTests {

    @Mock
    private TrainerAuthService trainerAuthService;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private TrainerAuthAspect trainerAuthAspect;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticationTest_ShouldReplaceLastArgumentWithAuthenticatedTrainer() throws Throwable {
        Trainer mockTrainer = new Trainer();
        Object[] originalArgs = new Object[]{null};
        when(joinPoint.getArgs()).thenReturn(originalArgs);
        when(trainerAuthService.authenticateTrainer()).thenReturn(mockTrainer);
        when(joinPoint.proceed(any())).thenReturn("success");

        Object result = trainerAuthAspect.authentication(joinPoint);

        assertEquals("success", result);
        assertEquals(mockTrainer, originalArgs[0]);
        verify(trainerAuthService, times(1)).authenticateTrainer();
        verify(joinPoint, times(1)).proceed(originalArgs);
    }

    @Test
    void authentication_ShouldThrowException_WhenAuthenticationFails() throws Throwable {
        when(trainerAuthService.authenticateTrainer()).thenThrow(new RuntimeException("Authentication failed"));

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            trainerAuthAspect.authentication(joinPoint);
        });

        assertEquals("Authentication failed", thrownException.getMessage());
        verify(trainerAuthService, times(1)).authenticateTrainer();
        verify(joinPoint, never()).proceed(any());
    }
}
