package example.security.services;

import example.entities.Trainer;
import example.repositories.TrainerRepository;
import example.repositories.imp.TrainerRepositoryImp;
import example.utils.input.InputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrainerAuthServiceTests {
    private InputHandler inputHandler;
    private TrainerRepository trainerRepository;
    private TrainerAuthService trainerAuthService;

    @BeforeEach
    public void setUp() {
        inputHandler = mock(InputHandler.class);
        trainerRepository = mock(TrainerRepositoryImp.class);
        trainerAuthService = new TrainerAuthService(trainerRepository, inputHandler);
    }

    @Test
    public void authenticateTrainer_ShouldThrow() {
        when(inputHandler.getLine(any(String.class), any(Boolean.class))).thenReturn("test");
        when(trainerRepository.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(Optional.empty());

        Exception e = assertThrows(RuntimeException.class, () -> trainerAuthService.authenticateTrainer());
        assertEquals("Trainer not found", e.getMessage());
    }

    @Test
    public void authenticateTrainer_ShouldReturnTrainer() {
        Trainer trainer = new Trainer();
        when(inputHandler.getLine(any(String.class), any(Boolean.class))).thenReturn("test");
        when(trainerRepository.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(Optional.of(trainer));

        Trainer authTrainer = trainerAuthService.authenticateTrainer();
        assertEquals(trainer, authTrainer);
    }
}
