import example.daos.TrainingDAO;
import example.entities.Training;
import example.services.TrainingService;
import example.services.imp.TrainingServiceImp;
import example.storages.Storage;
import example.storages.imp.TrainingStorage;
import example.utils.id.IdGenerator;
import example.utils.id.imp.SimpleIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingServiceTests {
    private Storage<Training> storage;
    private IdGenerator idGenerator;
    private TrainingDAO trainingDAO;
    private Training testTraining;
    private TrainingServiceImp trainingService;

    @BeforeEach
    public void init() throws IOException {
        idGenerator = new SimpleIdGenerator();
        storage = new TrainingStorage("t");
        storage.init();
        trainingDAO = new TrainingDAO();

        trainingDAO.setIdGenerator(idGenerator);
        trainingDAO.setTrainingStorage(storage);

        trainingService = new TrainingServiceImp();
        trainingService.setTrainingDAO(trainingDAO);
        testTraining = new Training();
    }

    @Test
    public void saveNullTest() {
        String message = assertThrows(IllegalArgumentException.class, () -> trainingService.createTraining(null)).getMessage();
        assertEquals("Can not perform save: training is null", message);
    }

    @Test
    public void saveNoIdTest() {
        Training result = trainingService.createTraining(testTraining);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(storage.containsKey(1L));
        assertTrue(storage.values().contains(testTraining));
    }

    @Test
    public void saveWithIdTest() {
        testTraining.setId(5L);
        Training result = trainingService.createTraining(testTraining);
        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertTrue(storage.containsKey(5L));
        assertTrue(storage.values().contains(testTraining));
    }

    @Test
    public void findAllTest() {
        Collection<Training> trainings = (Collection<Training>) trainingService.getAllTrainings();
        assertTrue(trainings.isEmpty());
        trainingService.createTraining(testTraining);
        assertEquals(1, trainings.size());
        assertTrue(trainings.contains(testTraining));
    }

    @Test
    public void findByIdTest() {
        trainingService.createTraining(testTraining);

        String message = assertThrows(IllegalArgumentException.class, () -> trainingService.getTrainingById(null)).getMessage();
        assertEquals("Can not find training by Id: id is null", message);

        Optional<Training> result = trainingService.getTrainingById(2L);
        assertFalse(result.isPresent());

        result = trainingService.getTrainingById(1L);
        assertEquals(testTraining, result.get());
    }

    @Test
    public void existsByIdTest() {
        assertFalse(trainingService.existsTraining(10L));
        assertFalse(trainingService.existsTraining(null));
        trainingService.createTraining(testTraining);
        assertTrue(trainingService.existsTraining(1L));
    }
}
