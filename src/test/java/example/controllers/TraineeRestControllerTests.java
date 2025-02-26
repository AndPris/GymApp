package example.controllers;

import example.dtos.*;
import example.dtos.trainee.*;
import example.dtos.trainer.TrainerSummaryDTO;
import example.dtos.training.TrainingBaseDTO;
import example.dtos.training.TrainingTraineeDTO;
import example.entities.*;
import example.mappers.*;
import example.services.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeRestControllerTests {

    @InjectMocks
    private TraineeRestController traineeRestController;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TrainingMapper trainingMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainee_ShouldReturnCreatedCredentials() {
        TraineeCreateDTO createDTO = new TraineeCreateDTO();
        Trainee trainee = new Trainee();
        CredentialsDTO credentialsDTO = new CredentialsDTO();

        when(traineeMapper.traineeCreateDTOToTrainee(createDTO)).thenReturn(trainee);
        when(traineeService.createTrainee(trainee)).thenReturn(trainee);
        when(traineeMapper.toCredentialsDTO(trainee)).thenReturn(credentialsDTO);

        ResponseEntity<CredentialsDTO> response = traineeRestController.createTrainee(createDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(credentialsDTO, response.getBody());
    }

    @Test
    void getAllTrainees_ShouldReturnListOfTrainees() {
        List<Trainee> trainees = Arrays.asList(new Trainee(), new Trainee());
        List<TraineeDTO> traineeDTOs = Arrays.asList(new TraineeDTO(), new TraineeDTO());

        when(traineeService.getAllTrainees()).thenReturn(trainees);
        when(traineeMapper.traineeToTraineeDTO(any(Trainee.class)))
                .thenReturn(traineeDTOs.get(0), traineeDTOs.get(1));

        ResponseEntity<List<TraineeDTO>> response = traineeRestController.getAllTrainees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(traineeDTOs, response.getBody());
    }

    @Test
    void getTraineeByUsername_ShouldReturnTraineeIfFound() {
        Trainee trainee = new Trainee();
        TraineeDTO traineeDTO = new TraineeDTO();

        when(traineeService.getTraineeByUsername("test")).thenReturn(Optional.of(trainee));
        when(traineeMapper.traineeToTraineeDTO(trainee)).thenReturn(traineeDTO);

        ResponseEntity<TraineeDTO> response = traineeRestController.getTraineeByUsername("test", "test");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(traineeDTO, response.getBody());
    }

    @Test
    void getTraineeByUsername_ShouldReturnNotFoundIfAbsent() {
        when(traineeService.getTraineeByUsername("unknownUser")).thenReturn(Optional.empty());

        ResponseEntity<TraineeDTO> response = traineeRestController.getTraineeByUsername("unknownUser", "test");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteTraineeByUsername_ShouldReturnOkIfDeleted() {
        when(traineeService.deleteTraineeByUsername("test")).thenReturn(true);

        ResponseEntity<?> response = traineeRestController.deleteTraineeByUsername("test", "test");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteTraineeByUsername_ShouldReturnNotFoundIfNotDeleted() {
        when(traineeService.deleteTraineeByUsername("test")).thenReturn(false);

        ResponseEntity<?> response = traineeRestController.deleteTraineeByUsername("test", "test");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getActiveTrainersListInverse_returnsTrainers() {
        String username = "test";
        String authHeader = "test";
        Trainer trainer = new Trainer();
        trainer.setActive(true);

        when(traineeService.getTraineeByUsername(username)).thenReturn(Optional.of(new Trainee()));
        when(traineeService.findActiveTrainersNotAssignedToTrainee(username)).thenReturn(Arrays.asList(trainer));
        when(trainerMapper.trainerToTrainerSummaryDTO(trainer)).thenReturn(new TrainerSummaryDTO());

        ResponseEntity<List<TrainerSummaryDTO>> response = traineeRestController.getActiveTrainersList(username, true, authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getActiveTrainersListNotInverse_returnsTrainers() {
        String username = "test";
        String authHeader = "test";

        Trainer trainer1 = new Trainer();
        trainer1.setActive(true);
        Trainer trainer2 = new Trainer();
        trainer2.setActive(false);

        Trainee trainee = new Trainee();
        trainee.setTrainers(Arrays.asList(trainer1, trainer2));

        when(traineeService.getTraineeByUsername(username)).thenReturn(Optional.of(trainee));
        when(trainerMapper.trainerToTrainerSummaryDTO(any(Trainer.class))).thenReturn(new TrainerSummaryDTO());

        ResponseEntity<List<TrainerSummaryDTO>> response = traineeRestController.getActiveTrainersList(username, false, authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getActiveTrainersList_returnsNotFound() {
        String username = "test";
        String authHeader = "test";

        when(traineeService.getTraineeByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<?> response = traineeRestController.getActiveTrainersList(username, true, authHeader);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getTrainingsList_returnsNotFound() {
        String username = "test";
        String authHeader = "test";

        when(traineeService.getTraineeByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<?> response = traineeRestController.getTrainingsList(username, null, null, null, null, null, authHeader);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getTrainingsList_returnsTrainingList() {
        String username = "test";
        String authHeader = "test";
        Training training = new Training();

        when(traineeService.getTraineeByUsername(username)).thenReturn(Optional.of(new Trainee()));
        when(traineeService.findTraineeTrainingList(eq(username), any(), any(), any(), any(), any())).thenReturn(Arrays.asList(training));
        when(trainingMapper.trainingToTrainingTraineeDTO(training)).thenReturn(new TrainingTraineeDTO());

        ResponseEntity<List<TrainingTraineeDTO>> response = traineeRestController.getTrainingsList(username, null, null, null, null, null, authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void toggleTraineeActiveStatus_updatesStatus() {
        String username = "test";
        String authHeader = "test";

        when(traineeService.toggleTraineeIsActiveStatus(username)).thenReturn(true);

        ResponseEntity<?> response = traineeRestController.toggleTraineeActiveStatus(username, authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((ActiveStatusDTO) response.getBody()).getActive());
    }

    @Test
    void updateTrainee_updatesTraineeDetails() {
        String username = "test";
        String authHeader = "test";
        TraineeUpdateDTO updateDTO = new TraineeUpdateDTO();
        Trainee trainee = new Trainee();
        TraineeDTO traineeDTO = new TraineeDTO();

        when(traineeService.updateTrainee(username, updateDTO)).thenReturn(trainee);
        when(traineeMapper.traineeToTraineeDTO(trainee)).thenReturn(traineeDTO);

        ResponseEntity<TraineeDTO> response = traineeRestController.updateTrainee(username, updateDTO, authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(traineeDTO, response.getBody());
    }

    @Test
    void updateTraineeTrainerList_ShouldReturnNotFound() {
        String username = "test";
        String authHeader = "test";

        when(traineeService.getTraineeByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<?> response = traineeRestController.updateTraineeTrainerList(username, null, authHeader);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateTraineeTrainerList_updatesTrainerList() {
        String username = "test";
        String authHeader = "test";
        TraineeTrainerListUpdateDTO updateDTO = new TraineeTrainerListUpdateDTO();
        Trainer trainer = new Trainer();

        when(traineeService.getTraineeByUsername(username)).thenReturn(Optional.of(new Trainee()));
        when(traineeService.updateTraineeTrainerList(username, updateDTO)).thenReturn(Arrays.asList(trainer));
        when(trainerMapper.trainerToTrainerSummaryDTO(trainer)).thenReturn(new TrainerSummaryDTO());

        ResponseEntity<List<TrainerSummaryDTO>> response = traineeRestController.updateTraineeTrainerList(username, updateDTO, authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}
