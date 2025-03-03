package example.mappers;

import example.dtos.trainee.TraineeCreateDTO;
import example.dtos.trainee.TraineeDTO;
import example.dtos.trainee.TraineeSummaryDTO;
import example.dtos.trainer.TrainerSummaryDTO;
import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class TraineeMapperTests {

    @InjectMocks
    private TraineeMapper traineeMapper;

    @Mock
    private TrainerMapper trainerMapper;

    private Trainee trainee;
    private Date date;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        date = new Date();
        trainee = new Trainee("John", "Doe", "123 Main St", date);

        TrainingType trainingType = new TrainingType("Pilates");
        trainingType.setId(1L);

        Trainer trainer = new Trainer("Jane", "Smith", trainingType);
        trainee.addTrainer(trainer);
    }

    @Test
    void traineeToTraineeDTO_shouldReturnTraineeDTO() {
        TrainerSummaryDTO trainerSummaryDTO = new TrainerSummaryDTO();
        trainerSummaryDTO.setUsername("Jane.Smith");
        when(trainerMapper.trainerToTrainerSummaryDTO(any(Trainer.class))).thenReturn(trainerSummaryDTO);

        TraineeDTO traineeDTO = traineeMapper.traineeToTraineeDTO(trainee);

        assertEquals(trainee.getFirstName(), traineeDTO.getFirstName());
        assertEquals(trainee.getLastName(), traineeDTO.getLastName());
        assertEquals(trainee.getAddress(), traineeDTO.getAddress());
        assertEquals(trainee.getDateOfBirth(), traineeDTO.getDateOfBirth());
        assertEquals(1, traineeDTO.getTrainersList().size());
        assertEquals("Jane.Smith", traineeDTO.getTrainersList().get(0).getUsername());
    }

    @Test
    void traineeCreateDTOToTrainee_shouldReturnTrainee() {
        TraineeCreateDTO traineeCreateDTO = new TraineeCreateDTO();
        traineeCreateDTO.setFirstName("John");
        traineeCreateDTO.setLastName("Doe");
        traineeCreateDTO.setAddress("123 Main St");
        traineeCreateDTO.setDateOfBirth(date);

        Trainee traineeResult = traineeMapper.traineeCreateDTOToTrainee(traineeCreateDTO);

        assertEquals(traineeCreateDTO.getFirstName(), traineeResult.getFirstName());
        assertEquals(traineeCreateDTO.getLastName(), traineeResult.getLastName());
        assertEquals(traineeCreateDTO.getAddress(), traineeResult.getAddress());
        assertEquals(traineeCreateDTO.getDateOfBirth(), traineeResult.getDateOfBirth());
    }

    @Test
    void traineeToTraineeSummaryDTO_shouldReturnTraineeSummaryDTO() {
        TraineeSummaryDTO traineeSummaryDTO = traineeMapper.traineeToTraineeSummaryDTO(trainee);

        assertEquals(trainee.getUsername(), traineeSummaryDTO.getUsername());
        assertEquals(trainee.getFirstName(), traineeSummaryDTO.getFirstName());
        assertEquals(trainee.getLastName(), traineeSummaryDTO.getLastName());
    }
}
