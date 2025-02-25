package example.services;

import example.dtos.trainee.TraineeTrainerListUpdateDTO;
import example.dtos.trainee.TraineeUpdateDTO;
import example.dtos.trainer.TrainerUpdateDTO;
import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;
import example.entities.TrainingType;
import example.exceptions.TraineeNotFoundException;
import example.exceptions.TrainerNotFoundException;
import example.exceptions.TrainingTypeNotFoundException;
import example.repositories.TraineeRepository;
import example.repositories.imp.TraineeRepositoryImp;
import example.services.imp.TraineeServiceImp;
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
import static org.mockito.Mockito.*;

public class TraineeServiceTests {
    private PasswordGenerator passwordGenerator;
    private UsernameGenerator usernameGenerator;
    private CustomValidator customValidator;
    private TraineeRepository traineeRepository;
    private TraineeServiceImp traineeService;
    private TrainerService trainerService;

    @BeforeEach
    public void init() {
        passwordGenerator = mock(SimplePasswordGenerator.class);
        when(passwordGenerator.generatePassword()).thenReturn("password");

        usernameGenerator = mock(SimpleUsernameGenerator.class);
        when(usernameGenerator.generateUsername(any())).thenReturn("username");

        customValidator = mock(CustomValidator.class);
        traineeRepository = mock(TraineeRepositoryImp.class);
        trainerService = mock(TrainerServiceImp.class);

        traineeService = new TraineeServiceImp();
        traineeService.setTrainerService(trainerService);
        traineeService.setCustomValidator(customValidator);
        traineeService.setTraineeRepository(traineeRepository);
        traineeService.setPasswordGenerator(passwordGenerator);
        traineeService.setUsernameGenerator(usernameGenerator);
    }

    @Test
    public void createTraineeNullTest_ShouldThrow() {
        String message = assertThrows(IllegalArgumentException.class, () -> traineeService.createTrainee(null)).getMessage();
        assertEquals("Cannot create a trainee: trainee is null", message);
    }

    @Test
    public void createTraineeTest_ShouldThrow() {
        Trainee trainee = new Trainee();
        String errorMessage = "Validation error";
        doThrow(new ValidationException(errorMessage)).when(customValidator).validate(trainee);
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        Exception e = assertThrows(ValidationException.class, () -> traineeService.createTrainee(trainee));
        assertEquals(errorMessage, e.getMessage());
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
    public void updateTraineeTest_ShouldThrowTraineeNotFoundException() {
        when(traineeRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        String message = assertThrows(TraineeNotFoundException.class, () -> traineeService.updateTrainee("test", null)).getMessage();
        assertEquals("There's no trainee with such username: test", message);
    }

    @Test
    public void updateTraineeTest_ShouldUpdate() {
        Trainee trainee = new Trainee();

        TraineeUpdateDTO traineeUpdateDTO = new TraineeUpdateDTO();
        traineeUpdateDTO.setFirstName("test");
        traineeUpdateDTO.setAddress("addr");

        when(traineeRepository.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        Trainee result = traineeService.updateTrainee("test", traineeUpdateDTO);
        assertEquals("test", result.getFirstName());
        assertNull(result.getLastName());
        assertNull(result.getDateOfBirth());
        assertEquals("addr", trainee.getAddress());
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
    public void toggleTraineeIsActiveStatusTest_ShouldThrow() {
        when(traineeRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        Exception e = assertThrows(TraineeNotFoundException.class,
                () -> traineeService.toggleTraineeIsActiveStatus("test"));
        assertEquals("No trainee with such username: test", e.getMessage());
    }

    @Test
    public void toggleTraineeIsActiveStatusTest_ShouldToggle() {
        Trainee trainee = new Trainee();
        trainee.setActive(false);
        when(traineeRepository.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));

        boolean result = traineeService.toggleTraineeIsActiveStatus("test");
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
    public void updateTraineeTrainerListTest_ShouldThrowTraineeNotFoundException() {
        when(traineeRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        String message = assertThrows(TraineeNotFoundException.class, () -> traineeService.updateTraineeTrainerList("test", null)).getMessage();
        assertEquals("There's no trainee with such username: test", message);
    }

    @Test
    public void updateTraineeTrainerListTest_ShouldThrowTrainerNotFoundException() {
        Trainee trainee = new Trainee();

        TraineeTrainerListUpdateDTO traineeTrainerListUpdateDTO = new TraineeTrainerListUpdateDTO();
        traineeTrainerListUpdateDTO.setTrainers(Arrays.asList("test", "test2"));

        when(traineeRepository.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));
        when(trainerService.getTrainerByUsername(any(String.class))).thenReturn(Optional.empty());

        String message = assertThrows(TrainerNotFoundException.class, () -> traineeService.updateTraineeTrainerList("test", traineeTrainerListUpdateDTO)).getMessage();
        assertEquals("There's no trainer with such username: test", message);
    }

    @Test
    public void updateTraineeTrainerListTest_ShouldUpdate() {
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();

        TraineeTrainerListUpdateDTO traineeTrainerListUpdateDTO = new TraineeTrainerListUpdateDTO();
        traineeTrainerListUpdateDTO.setTrainers(Arrays.asList("test"));

        when(traineeRepository.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));
        when(trainerService.getTrainerByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        List<Trainer> trainers = traineeService.updateTraineeTrainerList("test", traineeTrainerListUpdateDTO);
        assertEquals(trainer, trainers.get(0));
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
    public void findActiveTrainersNotAssignedToTraineeTest_ShouldReturnList() {
        Trainer trainer = new Trainer();

        when(traineeRepository.findActiveTrainersNotAssignedToTrainee(any())).thenReturn(Arrays.asList(trainer));

        List<Trainer> trainers = traineeService.findActiveTrainersNotAssignedToTrainee("test");

        assertEquals(1, trainers.size());
        assertEquals(trainer, trainers.get(0));
    }

}
