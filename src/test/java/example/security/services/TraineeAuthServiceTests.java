package example.security.services;

import example.entities.Trainee;
import example.repositories.TraineeRepository;
import example.repositories.imp.TraineeRepositoryImp;
import example.utils.input.InputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TraineeAuthServiceTests {
    private InputHandler inputHandler;
    private TraineeRepository traineeRepository;
    private TraineeAuthService traineeAuthService;

//    @BeforeEach
//    public void setUp() {
//        inputHandler = mock(InputHandler.class);
//        traineeRepository = mock(TraineeRepositoryImp.class);
//        traineeAuthService = new TraineeAuthService(traineeRepository, inputHandler);
//    }
//
//    @Test
//    public void authenticateTrainee_ShouldThrow() {
//        when(inputHandler.getLine(any(String.class), any(Boolean.class))).thenReturn("test");
//        when(traineeRepository.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(Optional.empty());
//
//        Exception e = assertThrows(RuntimeException.class, () -> traineeAuthService.authenticateTrainee());
//        assertEquals("Trainee not found", e.getMessage());
//    }
//
//    @Test
//    public void authenticateTrainee_ShouldReturnTrainee() {
//        Trainee trainee = new Trainee();
//        when(inputHandler.getLine(any(String.class), any(Boolean.class))).thenReturn("test");
//        when(traineeRepository.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(Optional.of(trainee));
//
//        Trainee authTrainee = traineeAuthService.authenticateTrainee();
//        assertEquals(trainee, authTrainee);
//    }
}
