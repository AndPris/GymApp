package example.controllers;

import example.dtos.ActiveStatusDTO;
import example.dtos.CredentialsDTO;
import example.dtos.trainer.TrainerCreateDTO;
import example.dtos.trainer.TrainerDTO;
import example.dtos.trainer.TrainerUpdateDTO;
import example.dtos.training.TrainingCreateDTO;
import example.dtos.training.TrainingTrainerDTO;
import example.entities.Trainer;
import example.entities.Training;
import example.exceptions.TrainerNotFoundException;
import example.mappers.TrainerMapper;
import example.mappers.TrainingMapper;
import example.services.TrainerService;
import example.services.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainerRestControllerTests {

    @InjectMocks
    private TrainerRestController trainerRestController;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TrainingMapper trainingMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainer_ShouldReturnCreatedCredentials() {
        TrainerCreateDTO trainerCreateDTO = new TrainerCreateDTO();
        Trainer trainer = new Trainer();
        CredentialsDTO credentialsDTO = new CredentialsDTO();

        when(trainerMapper.trainerCreateDTOToTrainer(trainerCreateDTO)).thenReturn(trainer);
        when(trainerService.createTrainer(trainer)).thenReturn(trainer);
        when(trainerMapper.toCredentialsDTO(trainer)).thenReturn(credentialsDTO);

        ResponseEntity<CredentialsDTO> response = trainerRestController.createTrainer(trainerCreateDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(credentialsDTO, response.getBody());
    }

    @Test
    void getAllTrainers_ShouldReturnListOfTrainerDTOs() {
        List<Trainer> trainers = Arrays.asList(new Trainer(), new Trainer());

        when(trainerService.getAllTrainers()).thenReturn(trainers);
        when(trainerMapper.trainerToTrainerDTO(any(Trainer.class))).thenReturn(new TrainerDTO());

        ResponseEntity<List<TrainerDTO>> response = trainerRestController.getAllTrainers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(trainers.size(), response.getBody().size());
    }

    @Test
    void getTrainerByUsername_ShouldReturnTrainerDTO() {
        String username = "test";
        Trainer trainer = new Trainer();
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerService.getTrainerByUsername(username)).thenReturn(Optional.of(trainer));
        when(trainerMapper.trainerToTrainerDTO(trainer)).thenReturn(trainerDTO);

        ResponseEntity<TrainerDTO> response = trainerRestController.getTrainerByUsername(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(trainerDTO, response.getBody());
    }

    @Test
    void getTrainerByUsername_ShouldReturnNotFound() {
        String username = "test";

        when(trainerService.getTrainerByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<TrainerDTO> response = trainerRestController.getTrainerByUsername(username);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createTraining_ShouldReturnCreatedStatus() {
        String username = "test";
        TrainingCreateDTO trainingCreateDTO = new TrainingCreateDTO();
        Trainer trainer = new Trainer();
        Training training = new Training();

        when(trainerService.getTrainerByUsername(username)).thenReturn(Optional.of(trainer));
        when(trainingMapper.trainingCreateDTOToTraining(trainingCreateDTO)).thenReturn(training);

        ResponseEntity<?> response = trainerRestController.createTraining(username, trainingCreateDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createTraining_ShouldThrowTrainerNotFoundException() {
        String username = "test";
        TrainingCreateDTO trainingCreateDTO = new TrainingCreateDTO();

        when(trainerService.getTrainerByUsername(username)).thenReturn(Optional.empty());

        TrainerNotFoundException e = assertThrows(TrainerNotFoundException.class, () -> {
            trainerRestController.createTraining(username, trainingCreateDTO);
        });
        assertEquals("There's no trainer with such username: test", e.getMessage());
    }

    @Test
    void getTrainingsList_ShouldReturnTrainingList() {
        String username = "test";
        List<Training> trainings = Arrays.asList(new Training(), new Training());

        when(trainerService.getTrainerByUsername(username)).thenReturn(Optional.of(new Trainer()));
        when(trainerService.findTrainerTrainingList(eq(username), any(), any(), any(), any())).thenReturn(trainings);
        when(trainingMapper.trainingToTrainingTrainerDTO(any(Training.class))).thenReturn(new TrainingTrainerDTO());

        ResponseEntity<List<TrainingTrainerDTO>> response = trainerRestController.getTrainingsList(username, null, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(trainings.size(), response.getBody().size());
    }

    @Test
    void getTrainingsList_ShouldReturnNotFound() {
        String username = "test";

        when(trainerService.getTrainerByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<?> response = trainerRestController.getTrainingsList(username, null, null, null, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void toggleTrainerActiveStatus_ShouldReturnActiveStatus() {
        String username = "test";
        boolean active = true;

        when(trainerService.toggleTrainerIsActiveStatus(username)).thenReturn(active);

        ResponseEntity<?> response = trainerRestController.toggleTrainerActiveStatus(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((ActiveStatusDTO) response.getBody()).getActive());
    }

    @Test
    void updateTrainer_ShouldReturnTrainerDTO() {
        String username = "test";
        TrainerUpdateDTO trainerUpdateDTO = new TrainerUpdateDTO();
        Trainer trainer = new Trainer();
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerService.updateTrainer(username, trainerUpdateDTO)).thenReturn(trainer);
        when(trainerMapper.trainerToTrainerDTO(trainer)).thenReturn(trainerDTO);

        ResponseEntity<TrainerDTO> response = trainerRestController.updateTrainer(username, trainerUpdateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(trainerDTO, response.getBody());
    }
}
