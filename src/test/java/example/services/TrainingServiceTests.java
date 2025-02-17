package example.services;

import example.entities.Training;
import example.repositories.TrainingRepository;
import example.repositories.imp.TrainingRepositoryImp;
import example.services.imp.TrainingServiceImp;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrainingServiceTests {
    private TrainingRepository trainingRepository;
    private TrainingService trainingService;

    @BeforeEach
    public void init() {
        trainingRepository = mock(TrainingRepositoryImp.class);
        trainingService = new TrainingServiceImp(trainingRepository);
    }

    @Test
    public void createTrainingNullTest_ShouldThrow() {
        String message = assertThrows(IllegalArgumentException.class, () -> trainingService.createTraining(null)).getMessage();
        assertEquals("Cannot create a training: training is null", message);
    }

    @ParameterizedTest
    @MethodSource("invalidTrainings")
    public void createTrainingTest_ShouldThrow(Training training, String errorMessage) {
        when(trainingRepository.save(training)).thenReturn(training);

        Exception e = assertThrows(ValidationException.class, () -> trainingService.createTraining(training));
        assertEquals(errorMessage, e.getMessage());
    }

    private static Stream<Arguments> invalidTrainings() {
        return Stream.of(
                Arguments.of(new Training(null, null, "t", null, null, 120),
                        "Validation error: Training name must be from 2 to 20 characters"),
                Arguments.of(new Training(null, null, "ttttttttttttttttttttttttttttttttttttttttttttttttttttt", null, null, 120),
                        "Validation error: Training name must be from 2 to 20 characters"),
                Arguments.of(new Training(null, null, "test", null, null, -10),
                        "Validation error: Minimal training duration is 20 minutes"),
                Arguments.of(new Training(null, null, "test", null, null, 500),
                        "Validation error: Maximal training duration is 180 minutes")
        );
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
