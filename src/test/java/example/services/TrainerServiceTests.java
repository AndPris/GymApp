package example.services;

import example.entities.Trainer;
import example.entities.Training;
import example.exceptions.TrainerNotFoundException;
import example.repositories.TrainerRepository;
import example.repositories.imp.TrainerRepositoryImp;
import example.services.imp.TrainerServiceImp;
import example.utils.password.PasswordGenerator;
import example.utils.password.imp.SimplePasswordGenerator;
import example.utils.username.UsernameGenerator;
import example.utils.username.imp.SimpleUsernameGenerator;
import example.validation.CustomValidator;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrainerServiceTests {
    private PasswordGenerator passwordGenerator;
    private UsernameGenerator usernameGenerator;
    private TrainerRepository trainerRepository;
    private TrainerServiceImp trainerService;
    private CustomValidator customValidator;

    @BeforeEach
    public void init() {
        PasswordGenerator passwordGenerator = mock(SimplePasswordGenerator.class);
        when(passwordGenerator.generatePassword()).thenReturn("password");

        SimpleUsernameGenerator usernameGenerator = mock(SimpleUsernameGenerator.class);
        when(usernameGenerator.generateUsername(any())).thenReturn("username");

        trainerRepository = mock(TrainerRepositoryImp.class);
        customValidator = mock(CustomValidator.class);

        trainerService = new TrainerServiceImp();
        trainerService.setCustomValidator(customValidator);
        trainerService.setTrainerRepository(trainerRepository);
        trainerService.setPasswordGenerator(passwordGenerator);
        trainerService.setUsernameGenerator(usernameGenerator);
    }

    @Test
    public void createTrainerNullTest_ShouldThrow() {
        String message = assertThrows(IllegalArgumentException.class, () -> trainerService.createTrainer(null)).getMessage();
        assertEquals("Cannot create a trainer: trainer is null", message);
    }

    @Test
    public void createTrainerTest_ShouldGeneratePasswordAndUsername() {
        Trainer trainer = new Trainer();
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        Trainer result = trainerService.createTrainer(trainer);
        assertNotNull(result);
        assertEquals("username", result.getUsername());
        assertEquals("password", result.getPassword());
    }

    @Test
    public void getAllTrainersTest() {
        Trainer training = new Trainer();
        when(trainerRepository.findAll()).thenReturn(Arrays.asList(training));

        List<Trainer> trainers = (List<Trainer>) trainerService.getAllTrainers();
        assertEquals(1, trainers.size());
        assertEquals(training, trainers.get(0));
    }

    @Test
    public void getTrainerByIdTest_ShouldReturnEmptyOptional() {
        when(trainerRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Optional<Trainer> result = trainerService.getTrainerById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void getTrainerByIdTest_ShouldReturnOptionalTrainer() {
        Trainer trainer = new Trainer();
        when(trainerRepository.findById(any(Long.class))).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerService.getTrainerById(1L);
        assertEquals(trainer, result.get());
    }

    @Test
    public void getTrainerByUsernameTest_ShouldReturnEmptyOptional() {
        when(trainerRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        Optional<Trainer> result = trainerService.getTrainerByUsername("test");
        assertFalse(result.isPresent());
    }

    @Test
    public void getTrainerByUsernameTest_ShouldReturnOptionalTrainer() {
        Trainer trainer = new Trainer();
        when(trainerRepository.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerService.getTrainerByUsername("test");
        assertEquals(trainer, result.get());
    }

    @Test
    public void existsTrainerByIdTest_ShouldReturnFalse() {
        when(trainerRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        boolean result = trainerService.existsTrainerById(1L);
        assertFalse(result);
    }

    @Test
    public void existsTrainerByIdTest_ShouldReturnTrue() {
        Trainer trainer = new Trainer();
        when(trainerRepository.findById(any(Long.class))).thenReturn(Optional.of(trainer));

        boolean result = trainerService.existsTrainerById(1L);
        assertTrue(result);
    }

    @Test
    public void existsTrainerByUsernameTest_ShouldReturnFalse() {
        when(trainerRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        boolean result = trainerService.existsTrainerByUsername("test");
        assertFalse(result);
    }

    @Test
    public void existsTrainerByUsernameTest_ShouldReturnTrue() {
        Trainer trainer = new Trainer();
        when(trainerRepository.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        boolean result = trainerService.existsTrainerByUsername("test");
        assertTrue(result);
    }

    @Test
    public void toggleTrainerIsActiveStatusTest_ShouldThrow() {
        when(trainerRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        Exception e = assertThrows(TrainerNotFoundException.class,
                () -> trainerService.toggleTrainerIsActiveStatus("test"));
        assertEquals("No trainer with such username: test", e.getMessage());
    }

    @Test
    public void toggleTrainerIsActiveStatusTest_ShouldToggle() {
        Trainer trainer = new Trainer();
        trainer.setActive(false);
        when(trainerRepository.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        boolean result = trainerService.toggleTrainerIsActiveStatus("test");
        assertTrue(result);
    }

    @Test
    public void findTrainerTrainingListTest_ShouldReturnEmptyList() {
        when(trainerRepository.findTrainingList(any(), any(), any(), any(), any())).thenReturn(new ArrayList<>());
        List<Training> trainings = trainerService.findTrainerTrainingList(null, null, null, null, null);
        assertTrue(trainings.isEmpty());
    }

    @Test
    public void findTrainerTrainingListTest_ShouldReturnNonEmptyList() {
        Training training = new Training();
        when(trainerRepository.findTrainingList(any(), any(), any(), any(), any())).thenReturn(Arrays.asList(training));

        List<Training> trainings = trainerService.findTrainerTrainingList(null, null, null, null, null);

        assertEquals(1, trainings.size());
        assertEquals(training, trainings.get(0));
    }
}
