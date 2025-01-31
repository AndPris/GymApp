import example.daos.TrainerDAO;
import example.entities.Trainer;
import example.storages.Storage;
import example.storages.imp.TrainerStorage;
import example.utils.id.IdGenerator;
import example.utils.id.imp.SimpleIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerDAOTests {
    private Storage<Trainer> storage;
    private IdGenerator idGenerator;
    private TrainerDAO trainerDAO;
    private Trainer testTrainer;

    @BeforeEach
    public void init() throws IOException {
        idGenerator = new SimpleIdGenerator();
        storage = new TrainerStorage("t");
        storage.init();
        trainerDAO = new TrainerDAO();

        trainerDAO.setIdGenerator(idGenerator);
        trainerDAO.setTrainerStorage(storage);

        testTrainer = new Trainer();
    }

    @Test
    public void saveNullTest() {
        String message = assertThrows(IllegalArgumentException.class, () -> trainerDAO.save(null)).getMessage();
        assertEquals("Can not perform save: trainer is null", message);
    }

    @Test
    public void saveNoIdTest() {
        Trainer result = trainerDAO.save(testTrainer);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(storage.containsKey(1L));
        assertTrue(storage.values().contains(testTrainer));
    }

    @Test
    public void saveWithIdTest() {
        testTrainer.setId(5L);
        Trainer result = trainerDAO.save(testTrainer);
        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertTrue(storage.containsKey(5L));
        assertTrue(storage.values().contains(testTrainer));
    }

    @Test
    public void findAllTest() {
        Collection<Trainer> trainers = (Collection<Trainer>) trainerDAO.findAll();
        assertTrue(trainers.isEmpty());
        trainerDAO.save(testTrainer);
        assertEquals(1, trainers.size());
        assertTrue(trainers.contains(testTrainer));
    }

    @Test
    public void findByIdTest() {
        trainerDAO.save(testTrainer);

        String message = assertThrows(IllegalArgumentException.class, () -> trainerDAO.findById(null)).getMessage();
        assertEquals("Can not find trainer by id: id is null", message);

        Optional<Trainer> result = trainerDAO.findById(2L);
        assertFalse(result.isPresent());

        result = trainerDAO.findById(1L);
        assertEquals(testTrainer, result.get());
    }

    @Test
    public void existsByIdTest() {
        assertFalse(trainerDAO.existsById(10L));
        assertFalse(trainerDAO.existsById(null));
        trainerDAO.save(testTrainer);
        assertTrue(trainerDAO.existsById(1L));
    }

    @Test
    public void updateInvalidParamsTest() {
        String message = assertThrows(IllegalArgumentException.class, () -> trainerDAO.update(null, null)).getMessage();
        assertEquals("Can not perform update: no trainer with such id: null", message);

        message = assertThrows(IllegalArgumentException.class, () -> trainerDAO.update(10L, null)).getMessage();
        assertEquals("Can not perform update: no trainer with such id: 10", message);

        trainerDAO.save(testTrainer);

        message = assertThrows(IllegalArgumentException.class, () -> trainerDAO.update(1L, null)).getMessage();
        assertEquals("Can not perform update: trainer is null", message);
    }

    @Test
    public void updateValidParamsTest() {
        testTrainer.setFirstName("first");
        testTrainer.setPassword("test password");
        trainerDAO.save(testTrainer);

        Trainer newTrainer = new Trainer();
        newTrainer.setFirstName("update");
        newTrainer.setLastName("test");

        trainerDAO.update(1L, newTrainer);

        Trainer updatedTrainer = trainerDAO.findById(1L).get();


        assertEquals(1L, updatedTrainer.getId());
        assertEquals("update", updatedTrainer.getFirstName());
        assertEquals("test", updatedTrainer.getLastName());
        assertEquals("test password", updatedTrainer.getPassword());
        assertNull(updatedTrainer.getSpecialization());
        assertNull(updatedTrainer.getUsername());
        assertFalse(updatedTrainer.isActive());
    }
}
