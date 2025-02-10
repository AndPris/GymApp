package example.security.aspects;

import example.entities.Trainee;
import example.security.services.TraineeAuthService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TraineeAuthAspectTests {

    @Mock
    private TraineeAuthService traineeAuthService;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private TraineeAuthAspect traineeAuthAspect;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticationTest_ShouldReplaceLastArgumentWithAuthenticatedTrainee() throws Throwable {
        Trainee mockTrainee = new Trainee();
        Object[] originalArgs = new Object[]{null};
        when(joinPoint.getArgs()).thenReturn(originalArgs);
        when(traineeAuthService.authenticateTrainee()).thenReturn(mockTrainee);
        when(joinPoint.proceed(any())).thenReturn("success");

        Object result = traineeAuthAspect.authentication(joinPoint);

        assertEquals("success", result);
        assertEquals(mockTrainee, originalArgs[0]);
        verify(traineeAuthService, times(1)).authenticateTrainee();
        verify(joinPoint, times(1)).proceed(originalArgs);
    }

    @Test
    void authentication_ShouldThrowException_WhenAuthenticationFails() throws Throwable {
        when(traineeAuthService.authenticateTrainee()).thenThrow(new RuntimeException("Authentication failed"));

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            traineeAuthAspect.authentication(joinPoint);
        });

        assertEquals("Authentication failed", thrownException.getMessage());
        verify(traineeAuthService, times(1)).authenticateTrainee();
        verify(joinPoint, never()).proceed(any());
    }
}
