package example.services;

import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;
import example.repositories.TraineeRepository;
import example.repositories.imp.TraineeRepositoryImp;
import example.services.imp.TraineeServiceImp;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TraineeServiceTests {
    private PasswordGenerator passwordGenerator;
    private UsernameGenerator usernameGenerator;
    private TraineeRepository traineeRepository;
    private TraineeServiceImp traineeService;

    @BeforeEach
    public void init() {
        PasswordGenerator passwordGenerator = mock(SimplePasswordGenerator.class);
        when(passwordGenerator.generatePassword()).thenReturn("password");

        SimpleUsernameGenerator usernameGenerator = mock(SimpleUsernameGenerator.class);
        when(usernameGenerator.generateUsername(any())).thenReturn("username");

        traineeRepository = mock(TraineeRepositoryImp.class);

        traineeService = new TraineeServiceImp();
        traineeService.setTraineeRepository(traineeRepository);
        traineeService.setPasswordGenerator(passwordGenerator);
        traineeService.setUsernameGenerator(usernameGenerator);
    }

    @Test
    public void createTraineeNullTest_ShouldThrow() {
        String message = assertThrows(IllegalArgumentException.class, () -> traineeService.createTrainee(null)).getMessage();
        assertEquals("Cannot create a trainee: trainee is null", message);
    }

    @ParameterizedTest
    @MethodSource("invalidTrainees")
    public void createTraineeTest_ShouldThrow(Trainee trainee, String errorMessage) {
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        Exception e = assertThrows(ValidationException.class, () -> traineeService.createTrainee(trainee));
        assertEquals(errorMessage, e.getMessage());
    }

    private static Stream<Arguments> invalidTrainees() {
        Trainee trainee1 = new Trainee();
        trainee1.setPassword("1");

        Trainee trainee2 = new Trainee();
        trainee2.setPassword("1111111111111111111111111111111111111111111");

        Trainee trainee3 = new Trainee();
        trainee3.setUsername("1");

        Trainee trainee4 = new Trainee();
        trainee4.setUsername("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");

        return Stream.of(
                Arguments.of(new Trainee("f", "last", null, null),
                        "Validation error: First name must be from 2 to 20 characters"),
                Arguments.of(new Trainee("fgggggggggggggggggggggggggggggggggggggggggggggggg", "last", null, null),
                        "Validation error: First name must be from 2 to 20 characters"),
                Arguments.of(new Trainee("first", "l", null, null),
                        "Validation error: Last name must be from 2 to 20 characters"),
                Arguments.of(new Trainee("first", "lllllllllllllllllllllllllllllllllllllllllllllll", null, null),
                        "Validation error: Last name must be from 2 to 20 characters"),
                Arguments.of(trainee1, "Validation error: Password must be from 6 to 20 characters"),
                Arguments.of(trainee2, "Validation error: Password must be from 6 to 20 characters"),
                Arguments.of(trainee3, "Validation error: Username must be from 2 to 50 characters"),
                Arguments.of(trainee4, "Validation error: Username must be from 2 to 50 characters")
        );
    }

    @Test
    public void createTraineeTest_ShouldGeneratePasswordAndUsername() {
        Trainee trainee = new Trainee();
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        Trainee result = traineeService.createTrainee(trainee);
        assertNotNull(result);
        assertEquals("username", result.getUsername());
        assertEquals("password", result.getPassword());
    }

    @Test
    public void updateTraineeTest_ShouldThrow1() {
        String message = assertThrows(IllegalArgumentException.class, () -> traineeService.updateTrainee(null)).getMessage();
        assertEquals("Cannot update a trainee: invalid data", message);

        message = assertThrows(IllegalArgumentException.class, () -> traineeService.updateTrainee(new Trainee())).getMessage();
        assertEquals("Cannot update a trainee: invalid data", message);

        Trainee trainee = new Trainee();
        trainee.setId(2L);
        when(traineeRepository.findById(2L)).thenReturn(Optional.empty());
        message = assertThrows(IllegalArgumentException.class, () -> traineeService.updateTrainee(trainee)).getMessage();
        assertEquals("Cannot update a trainee: invalid data", message);
    }

    @ParameterizedTest
    @MethodSource("invalidTrainees")
    public void updateTraineeTest_ShouldThrow2(Trainee trainee, String errorMessage) {
        trainee.setId(1L);
        when(traineeRepository.findById(any(Long.class))).thenReturn(Optional.of(trainee));

        Exception e = assertThrows(ValidationException.class, () -> traineeService.updateTrainee(trainee));
        assertEquals(errorMessage, e.getMessage());
    }

    @Test
    public void updateTraineeTest_ShouldPerformUpdate() {
        Trainee existing = new Trainee("first", "last", null, null);
        existing.setId(1L);
        Trainee update = new Trainee("updated", null, null, null);
        update.setId(1L);

        when(traineeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(traineeRepository.save(existing)).thenReturn(existing);

        Trainee result = traineeService.updateTrainee(update);
        assertEquals("updated", result.getFirstName());
        assertEquals("last", result.getLastName());
        assertNull(result.getAddress());
        assertNull(result.getDateOfBirth());
    }

    @Test
    public void getAllTraineesTest() {
        Trainee training = new Trainee();
        when(traineeRepository.findAll()).thenReturn(Arrays.asList(training));

        List<Trainee> trainees = (List<Trainee>) traineeService.getAllTrainees();
        assertEquals(1, trainees.size());
        assertEquals(training, trainees.get(0));
    }

    @Test
    public void getTraineeByIdTest_ShouldReturnEmptyOptional() {
        when(traineeRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Optional<Trainee> result = traineeService.getTraineeById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void getTraineeByIdTest_ShouldReturnOptionalTrainee() {
        Trainee trainee = new Trainee();
        when(traineeRepository.findById(any(Long.class))).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = traineeService.getTraineeById(1L);
        assertEquals(trainee, result.get());
    }

    @Test
    public void getTraineeByUsernameTest_ShouldReturnEmptyOptional() {
        when(traineeRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        Optional<Trainee> result = traineeService.getTraineeByUsername("test");
        assertFalse(result.isPresent());
    }

    @Test
    public void getTraineeByUsernameTest_ShouldReturnOptionalTrainee() {
        Trainee trainee = new Trainee();
        when(traineeRepository.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = traineeService.getTraineeByUsername("test");
        assertEquals(trainee, result.get());
    }

    @Test
    public void existsTraineeByIdTest_ShouldReturnFalse() {
        when(traineeRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        boolean result = traineeService.existsTraineeById(1L);
        assertFalse(result);
    }

    @Test
    public void existsTraineeByIdTest_ShouldReturnTrue() {
        Trainee trainee = new Trainee();
        when(traineeRepository.findById(any(Long.class))).thenReturn(Optional.of(trainee));

        boolean result = traineeService.existsTraineeById(1L);
        assertTrue(result);
    }

    @Test
    public void deleteTraineeByUsernameTest_ShouldReturnFalse() {
        when(traineeRepository.deleteByUsername(any(String.class))).thenReturn(false);

        boolean result = traineeService.deleteTraineeByUsername("test");
        assertFalse(result);
    }

    @Test
    public void deleteTraineeByUsernameTest_ShouldReturnTrue() {
        when(traineeRepository.deleteByUsername(any(String.class))).thenReturn(true);

        boolean result = traineeService.deleteTraineeByUsername("test");
        assertTrue(result);
    }

    @Test
    public void changeTraineePassword_ShouldThrow1() {
        when(traineeRepository.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(Optional.empty());

        Exception e = assertThrows(IllegalArgumentException.class,
                () -> traineeService.changeTraineePassword("1", "1", "1"));
        assertEquals("There's no trainee with such username and password", e.getMessage());
    }

    @Test
    public void changeTraineePassword_ShouldThrow2() {
        Trainee trainee = new Trainee();
        trainee.setUsername("username");
        trainee.setPassword("password");
        when(traineeRepository.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(Optional.of(trainee));

        Exception e = assertThrows(ValidationException.class,
                () -> traineeService.changeTraineePassword("username", "password", "1"));
        assertEquals("Validation error: Password must be from 6 to 20 characters", e.getMessage());

    }

    @Test
    public void changeTraineePassword_ShouldChange() {
        Trainee trainee = new Trainee();
        trainee.setUsername("username");
        trainee.setPassword("password");
        when(traineeRepository.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(Optional.of(trainee));

        traineeService.changeTraineePassword("username", "password", "newPass");
        assertEquals("newPass", trainee.getPassword());
    }

    @Test
    public void toggleTraineeIsActiveStatusTest_ShouldThrow() {
        when(traineeRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception e = assertThrows(IllegalArgumentException.class,
                () -> traineeService.toggleTraineeIsActiveStatus(1L));
        assertEquals("No trainee with such id: 1", e.getMessage());
    }

    @Test
    public void toggleTraineeIsActiveStatusTest_ShouldToggle() {
        Trainee trainee = new Trainee();
        trainee.setActive(false);
        when(traineeRepository.findById(any(Long.class))).thenReturn(Optional.of(trainee));

        boolean result = traineeService.toggleTraineeIsActiveStatus(1L);
        assertTrue(result);
    }

    @Test
    public void findTraineeTrainingListTest_ShouldReturnEmptyList() {
        when(traineeRepository.findTrainingList(any(), any(), any(), any(), any(), any())).thenReturn(new ArrayList<>());
        List<Training> trainings = traineeService.findTraineeTrainingList(null, null, null, null, null, null);
        assertTrue(trainings.isEmpty());
    }

    @Test
    public void findTraineeTrainingListTest_ShouldReturnNonEmptyList() {
        Training training = new Training();
        when(traineeRepository.findTrainingList(any(), any(), any(), any(), any(), any())).thenReturn(Arrays.asList(training));

        List<Training> trainings = traineeService.findTraineeTrainingList(null, null, null, null, null, null);

        assertEquals(1, trainings.size());
        assertEquals(training, trainings.get(0));
    }

    @Test
    public void addTrainerToListTest_ShouldAdd() {
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();

        traineeService.addTrainerToList(trainee, trainer);

        List<Trainer> trainers = trainee.getTrainers();
        assertTrue(trainers.contains(trainer));
    }

    @Test
    public void removeTrainerToListTest_ShouldRemove() {
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        trainee.addTrainer(trainer);

        traineeService.removeTrainerFromList(trainee, trainer);

        List<Trainer> trainers = trainee.getTrainers();
        assertFalse(trainers.contains(trainer));
    }

    @Test
    public void clearTraineeTrainerListTest_ShouldRemove() {
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        trainee.addTrainer(trainer);

        traineeService.clearTraineeTrainerList(trainee);

        List<Trainer> trainers = trainee.getTrainers();
        assertFalse(trainers.contains(trainer));
    }

    @Test
    public void findTrainersNotAssignedToTraineeTest_ShouldReturnList() {
        Trainer trainer = new Trainer();

        when(traineeRepository.findTrainersNotAssignedToTrainee(any())).thenReturn(Arrays.asList(trainer));

        List<Trainer> trainers = traineeService.findTrainersNotAssignedToTrainee("test");

        assertEquals(1, trainers.size());
        assertEquals(trainer, trainers.get(0));
    }

}
