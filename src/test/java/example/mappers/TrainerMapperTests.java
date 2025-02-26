package example.mappers;

import example.dtos.trainee.TraineeSummaryDTO;
import example.dtos.trainer.TrainerCreateDTO;
import example.dtos.trainer.TrainerDTO;
import example.dtos.trainer.TrainerSummaryDTO;
import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.TrainingType;
import example.services.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TrainerMapperTests {

    @InjectMocks
    private TrainerMapper trainerMapper;

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private TraineeMapper traineeMapper;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainerMapper.setTraineeMapper(traineeMapper);

        TrainingType specialization = new TrainingType();
        specialization.setId(1L);

        trainer = new Trainer("John", "Doe", specialization);
        trainer.setUsername("john.doe");
        trainer.setPassword("password");
        trainer.setActive(true);

        Trainee trainee = new Trainee();
        trainee.setFirstName("test");
        trainer.setTrainees(Arrays.asList(trainee));
    }

    @Test
    void testTrainerToTrainerDTO() {
        TraineeSummaryDTO traineeSummaryDTO = new TraineeSummaryDTO();
        traineeSummaryDTO.setFirstName("test");

        when(traineeMapper.traineeToTraineeSummaryDTO(any(Trainee.class))).thenReturn(traineeSummaryDTO);

        TrainerDTO trainerDTO = trainerMapper.trainerToTrainerDTO(trainer);

        assertEquals("John", trainerDTO.getFirstName());
        assertEquals("Doe", trainerDTO.getLastName());
        assertEquals(1L, trainerDTO.getSpecialization());
        assertTrue(trainerDTO.getActive());
        assertEquals("test", trainerDTO.getTraineesList().get(0).getFirstName());
    }

    @Test
    void testTrainerToTrainerSummaryDTO() {
        TrainerSummaryDTO summaryDTO = trainerMapper.trainerToTrainerSummaryDTO(trainer);

        assertNotNull(summaryDTO);
        assertEquals("John", summaryDTO.getFirstName());
        assertEquals("Doe", summaryDTO.getLastName());
        assertEquals("john.doe", summaryDTO.getUsername());
        assertEquals(1L, summaryDTO.getSpecialization());
    }

    @Test
    void testTrainerCreateDTOToTrainer() {
        TrainerCreateDTO createDTO = new TrainerCreateDTO();
        createDTO.setFirstName("Mike");
        createDTO.setLastName("Johnson");
        createDTO.setSpecialization(3L);

        TrainingType specialization = new TrainingType();
        specialization.setId(3L);

        when(trainingTypeService.findById(3L)).thenReturn(specialization);

        Trainer trainer = trainerMapper.trainerCreateDTOToTrainer(createDTO);

        assertEquals("Mike", trainer.getFirstName());
        assertEquals("Johnson", trainer.getLastName());
        assertEquals(specialization, trainer.getSpecialization());
    }
}
