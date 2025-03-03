package example.mappers;

import example.dtos.training.TrainingCreateDTO;
import example.dtos.training.TrainingTraineeDTO;
import example.dtos.training.TrainingTrainerDTO;
import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;
import example.entities.TrainingType;
import example.exceptions.TraineeNotFoundException;
import example.services.TraineeService;
import example.services.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrainingMapperTests {

    private TrainingMapper trainingMapper;
    private TraineeService traineeService;
    private TrainingTypeService trainingTypeService;
    private Date date;

    @BeforeEach
    void setUp() {
        traineeService = mock(TraineeService.class);
        trainingTypeService = mock(TrainingTypeService.class);
        trainingMapper = new TrainingMapper(traineeService, trainingTypeService);
        date = new Date();
    }

    @Test
    void shouldMapTrainingToTrainingTraineeDTO() {
        Training training = createMockTraining();

        TrainingTraineeDTO dto = trainingMapper.trainingToTrainingTraineeDTO(training);

        assertEquals("Yoga", dto.getTrainingName());
        assertEquals("John", dto.getTrainerFirstName());
        assertEquals("Doe", dto.getTrainerLastName());
        assertEquals(date, dto.getTrainingDate());
        assertEquals(60, dto.getTrainingDuration());
        assertEquals(1, dto.getTrainingType());
    }

    @Test
    void shouldMapTrainingToTrainingTrainerDTO() {
        Training training = createMockTraining();

        TrainingTrainerDTO dto = trainingMapper.trainingToTrainingTrainerDTO(training);

        assertEquals("Yoga", dto.getTrainingName());
        assertEquals("Jane", dto.getTraineeFirstName());
        assertEquals("Smith", dto.getTraineeLastName());
    }

    @Test
    void shouldMapTrainingCreateDTOToTraining() {
        TrainingCreateDTO createDTO = new TrainingCreateDTO();
        createDTO.setTraineeUsername("jane.smith");
        createDTO.setTrainingName("Yoga");
        createDTO.setTrainingType(1L);
        createDTO.setTrainingDate(new Date());
        createDTO.setTrainingDuration(60);

        Trainee trainee = new Trainee();
        TrainingType trainingType = new TrainingType("Yoga");

        when(traineeService.getTraineeByUsername("jane.smith")).thenReturn(Optional.of(trainee));
        when(trainingTypeService.findById(1L)).thenReturn(trainingType);

        Training training = trainingMapper.trainingCreateDTOToTraining(createDTO);

        assertEquals("Yoga", training.getTrainingName());
        assertEquals(trainingType, training.getTrainingType());
    }

    @Test
    void shouldThrowExceptionWhenTraineeNotFound() {
        TrainingCreateDTO createDTO = new TrainingCreateDTO();
        createDTO.setTraineeUsername("unknown.user");

        when(traineeService.getTraineeByUsername("unknown.user")).thenReturn(Optional.empty());

        TraineeNotFoundException ex = assertThrows(TraineeNotFoundException.class,
                () -> trainingMapper.trainingCreateDTOToTraining(createDTO));
        assertEquals("There's no trainee with such username", ex.getMessage());
    }

    private Training createMockTraining() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("Jane");
        trainee.setLastName("Smith");

        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");

        TrainingType trainingType = new TrainingType("Yoga");
        trainingType.setId(1L);

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName("Yoga");
        training.setTrainingType(trainingType);
        training.setTrainingDate(date);
        training.setTrainingDuration(60);

        return training;
    }
}
