import example.daos.TrainingDAO;
import example.entities.Training;
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

public class TrainingDAOTests {
    private Storage<Training> storage;
    private IdGenerator idGenerator;
    private TrainingDAO trainingDAO;
    private Training testTraining;

    @BeforeEach
    public void init() throws IOException {
        idGenerator = new SimpleIdGenerator();
        storage = new TrainingStorage("t");
        storage.init();
        trainingDAO = new TrainingDAO();

        trainingDAO.setIdGenerator(idGenerator);
        trainingDAO.setTrainingStorage(storage);

        testTraining = new Training();
    }

    @Test
    public void saveNullTest() {
        String message = assertThrows(IllegalArgumentException.class, () -> trainingDAO.save(null)).getMessage();
        assertEquals("Can not perform save: training is null", message);
    }

    @Test
    public void saveNoIdTest() {
        Training result = trainingDAO.save(testTraining);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(storage.containsKey(1L));
        assertTrue(storage.values().contains(testTraining));
    }

    @Test
    public void saveWithIdTest() {
        testTraining.setId(5L);
        Training result = trainingDAO.save(testTraining);
        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertTrue(storage.containsKey(5L));
        assertTrue(storage.values().contains(testTraining));
    }

    @Test
    public void findAllTest() {
        Collection<Training> trainings = (Collection<Training>) trainingDAO.findAll();
        assertTrue(trainings.isEmpty());
        trainingDAO.save(testTraining);
        assertEquals(1, trainings.size());
        assertTrue(trainings.contains(testTraining));
    }

    @Test
    public void findByIdTest() {
        trainingDAO.save(testTraining);

        String message = assertThrows(IllegalArgumentException.class, () -> trainingDAO.findById(null)).getMessage();
        assertEquals("Can not find training by Id: id is null", message);

        Optional<Training> result = trainingDAO.findById(2L);
        assertFalse(result.isPresent());

        result = trainingDAO.findById(1L);
        assertEquals(testTraining, result.get());
    }

    @Test
    public void existsByIdTest() {
        assertFalse(trainingDAO.existsById(10L));
        assertFalse(trainingDAO.existsById(null));
        trainingDAO.save(testTraining);
        assertTrue(trainingDAO.existsById(1L));
    }
}
