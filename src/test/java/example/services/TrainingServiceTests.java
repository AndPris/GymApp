package example.services;

import example.entities.Training;
import example.repositories.TrainingRepository;
import example.repositories.imp.TrainingRepositoryImp;
import example.services.imp.TrainingServiceImp;
import example.validation.CustomValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrainingServiceTests {
    private TrainingRepository trainingRepository;
    private CustomValidator customValidator;
    private TrainingService trainingService;

    @BeforeEach
    public void init() {
        trainingRepository = mock(TrainingRepositoryImp.class);
        customValidator = mock(CustomValidator.class);
        trainingService = new TrainingServiceImp(trainingRepository, customValidator);
    }

    @Test
    public void createTrainingNullTest_ShouldThrow() {
        String message = assertThrows(IllegalArgumentException.class, () -> trainingService.createTraining(null)).getMessage();
        assertEquals("Cannot create a training: training is null", message);
    }

    @Test
    public void createTrainingTest_ShouldCreate() {
        Training training = new Training();
        when(trainingRepository.save(training)).thenReturn(training);

        Training result = trainingService.createTraining(training);
        assertEquals(training, result);
    }

    @Test
    public void getAllTrainingsTest() {
        Training training = new Training();
        when(trainingRepository.findAll()).thenReturn(Arrays.asList(training));

        List<Training> trainings = (List<Training>) trainingService.getAllTrainings();
        assertEquals(1, trainings.size());
        assertEquals(training, trainings.get(0));
    }
}
