package example.services;

import example.entities.Trainer;
import example.entities.Training;
import example.repositories.TrainerRepository;
import example.repositories.imp.TrainerRepositoryImp;
import example.services.imp.TrainerServiceImp;
import example.utils.password.PasswordGenerator;
import example.utils.password.imp.SimplePasswordGenerator;
import example.utils.username.UsernameGenerator;
import example.utils.username.imp.SimpleUsernameGenerator;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
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

    @BeforeEach
    public void init() {
        PasswordGenerator passwordGenerator = mock(SimplePasswordGenerator.class);
        when(passwordGenerator.generatePassword()).thenReturn("password");

        SimpleUsernameGenerator usernameGenerator = mock(SimpleUsernameGenerator.class);
        when(usernameGenerator.generateUsername(any())).thenReturn("username");

        trainerRepository = mock(TrainerRepositoryImp.class);

        trainerService = new TrainerServiceImp();
        trainerService.setTrainerRepository(trainerRepository);
        trainerService.setPasswordGenerator(passwordGenerator);
        trainerService.setUsernameGenerator(usernameGenerator);
    }

    @Test
    public void createTrainerNullTest_ShouldThrow() {
        String message = assertThrows(IllegalArgumentException.class, () -> trainerService.createTrainer(null)).getMessage();
        assertEquals("Cannot create a trainer: trainer is null", message);
    }

    @ParameterizedTest
    @MethodSource("invalidTrainers")
    public void createTrainerTest_ShouldThrow(Trainer trainer, String errorMessage) {
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        Exception e = assertThrows(ValidationException.class, () -> trainerService.createTrainer(trainer));
        assertEquals(errorMessage, e.getMessage());
    }

    private static Stream<Arguments> invalidTrainers() {
        Trainer trainer1 = new Trainer();
        trainer1.setPassword("1");

        Trainer trainer2 = new Trainer();
        trainer2.setPassword("1111111111111111111111111111111111111111111");

        Trainer trainer3 = new Trainer();
        trainer3.setUsername("1");

        Trainer trainer4 = new Trainer();
        trainer4.setUsername("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");

        return Stream.of(
                Arguments.of(new Trainer("f", "last", null),
                        "Validation error: First name must be from 2 to 20 characters"),
                Arguments.of(new Trainer("fgggggggggggggggggggggggggggggggggggggggggggggggg", "last", null),
                        "Validation error: First name must be from 2 to 20 characters"),
                Arguments.of(new Trainer("first", "l", null),
                        "Validation error: Last name must be from 2 to 20 characters"),
                Arguments.of(new Trainer("first", "lllllllllllllllllllllllllllllllllllllllllllllll", null),
                        "Validation error: Last name must be from 2 to 20 characters"),
                Arguments.of(trainer1, "Validation error: Password must be from 6 to 20 characters"),
                Arguments.of(trainer2, "Validation error: Password must be from 6 to 20 characters"),
                Arguments.of(trainer3, "Validation error: Username must be from 2 to 50 characters"),
                Arguments.of(trainer4, "Validation error: Username must be from 2 to 50 characters")
        );
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
    public void updateTrainerTest_ShouldThrow1() {
        String message = assertThrows(IllegalArgumentException.class, () -> trainerService.updateTrainer(null)).getMessage();
        assertEquals("Cannot update a trainer: trainer is null", message);

        message = assertThrows(IllegalArgumentException.class, () -> trainerService.updateTrainer(new Trainer())).getMessage();
        assertEquals("Cannot update a trainer: id is null", message);

        Trainer trainer = new Trainer();
        trainer.setId(2L);
        when(trainerRepository.findById(2L)).thenReturn(Optional.empty());
        message = assertThrows(IllegalArgumentException.class, () -> trainerService.updateTrainer(trainer)).getMessage();
        assertEquals("Cannot update a trainer: there is no trainer with id 2", message);
    }

    @ParameterizedTest
    @MethodSource("invalidTrainers")
    public void updateTrainerTest_ShouldThrow2(Trainer trainer, String errorMessage) {
        trainer.setId(1L);
        when(trainerRepository.findById(any(Long.class))).thenReturn(Optional.of(trainer));

        Exception e = assertThrows(ValidationException.class, () -> trainerService.updateTrainer(trainer));
        assertEquals(errorMessage, e.getMessage());
    }

    @Test
    public void updateTrainerTest_ShouldPerformUpdate() {
        Trainer existing = new Trainer("first", "last", null);
        existing.setId(1L);
        Trainer update = new Trainer("updated", null, null);
        update.setId(1L);

        when(trainerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(trainerRepository.save(existing)).thenReturn(existing);

        Trainer result = trainerService.updateTrainer(update);
        assertEquals("updated", result.getFirstName());
        assertEquals("last", result.getLastName());
        assertNull(result.getSpecialization());
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
    public void changeTrainerPassword_ShouldThrow1() {
        when(trainerRepository.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(Optional.empty());

        Exception e = assertThrows(IllegalArgumentException.class,
                () -> trainerService.changeTrainerPassword("1", "1", "1"));
        assertEquals("There's no trainer with such username and password", e.getMessage());
    }

    @Test
    public void changeTrainerPassword_ShouldThrow2() {
        Trainer trainer = new Trainer();
        trainer.setUsername("username");
        trainer.setPassword("password");
        when(trainerRepository.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(Optional.of(trainer));

        Exception e = assertThrows(ValidationException.class,
                () -> trainerService.changeTrainerPassword("username", "password", "1"));
        assertEquals("Validation error: Password must be from 6 to 20 characters", e.getMessage());

    }

    @Test
    public void changeTrainerPassword_ShouldChange() {
        Trainer trainer = new Trainer();
        trainer.setUsername("username");
        trainer.setPassword("password");
        when(trainerRepository.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(Optional.of(trainer));

        trainerService.changeTrainerPassword("username", "password", "newPass");
        assertEquals("newPass", trainer.getPassword());
    }

    @Test
    public void toggleTrainerIsActiveStatusTest_ShouldThrow() {
        when(trainerRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception e = assertThrows(IllegalArgumentException.class,
                () -> trainerService.toggleTrainerIsActiveStatus(1L));
        assertEquals("No trainer with such id: 1", e.getMessage());
    }

    @Test
    public void toggleTrainerIsActiveStatusTest_ShouldToggle() {
        Trainer trainer = new Trainer();
        trainer.setActive(false);
        when(trainerRepository.findById(any(Long.class))).thenReturn(Optional.of(trainer));

        boolean result = trainerService.toggleTrainerIsActiveStatus(1L);
        assertTrue(result);
    }

    @Test
    public void findTrainerTrainingListTest_ShouldReturnEmptyList() {
        when(trainerRepository.findTrainingList(any(), any(), any(), any(), any())).thenReturn(new ArrayList<>());
        List<Training> trainings = trainerService.findTrainerTrainingList(null,null,null,null,null);
        assertTrue(trainings.isEmpty());
    }

    @Test
    public void findTrainerTrainingListTest_ShouldReturnNonEmptyList() {
        Training training = new Training();
        when(trainerRepository.findTrainingList(any(), any(), any(), any(), any())).thenReturn(Arrays.asList(training));

        List<Training> trainings = trainerService.findTrainerTrainingList(null,null,null,null,null);

        assertEquals(1, trainings.size());
        assertEquals(training, trainings.get(0));
    }
}
