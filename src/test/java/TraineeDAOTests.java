import example.daos.TraineeDAO;
import example.entities.Trainee;
import example.storages.Storage;
import example.storages.imp.TraineeStorage;
import example.utils.id.IdGenerator;
import example.utils.id.imp.SimpleIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeDAOTests {
    private Storage<Trainee> storage;
    private IdGenerator idGenerator;
    private TraineeDAO traineeDAO;
    private Trainee testTrainee;

    @BeforeEach
    public void init() throws IOException {
        idGenerator = new SimpleIdGenerator();
        storage = new TraineeStorage("t");
        storage.init();
        traineeDAO = new TraineeDAO();

        traineeDAO.setIdGenerator(idGenerator);
        traineeDAO.setTraineeStorage(storage);

        testTrainee = new Trainee();
    }

    @Test
    public void saveNullTest() {
        String message = assertThrows(IllegalArgumentException.class, () -> traineeDAO.save(null)).getMessage();
        assertEquals("Can not perform save: trainee is null", message);
    }

    @Test
    public void saveNoIdTest() {
        Trainee result = traineeDAO.save(testTrainee);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(storage.containsKey(1L));
        assertTrue(storage.values().contains(testTrainee));
    }

    @Test
    public void saveWithIdTest() {
        testTrainee.setId(5L);
        Trainee result = traineeDAO.save(testTrainee);
        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertTrue(storage.containsKey(5L));
        assertTrue(storage.values().contains(testTrainee));
    }

    @Test
    public void findAllTest() {
        Collection<Trainee> trainees = (Collection<Trainee>) traineeDAO.findAll();
        assertTrue(trainees.isEmpty());
        traineeDAO.save(testTrainee);
        assertEquals(1, trainees.size());
        assertTrue(trainees.contains(testTrainee));
    }

    @Test
    public void findByIdTest() {
        traineeDAO.save(testTrainee);

        String message = assertThrows(IllegalArgumentException.class, () -> traineeDAO.findById(null)).getMessage();
        assertEquals("Can not find trainee by Id: id is null", message);

        Optional<Trainee> result = traineeDAO.findById(2L);
        assertFalse(result.isPresent());

        result = traineeDAO.findById(1L);
        assertEquals(testTrainee, result.get());
    }

    @Test
    public void existsByIdTest() {
        assertFalse(traineeDAO.existsById(10L));
        assertFalse(traineeDAO.existsById(null));
        traineeDAO.save(testTrainee);
        assertTrue(traineeDAO.existsById(1L));
    }

    @Test
    public void updateInvalidParamsTest() {
        String message = assertThrows(IllegalArgumentException.class, () -> traineeDAO.update(null, null)).getMessage();
        assertEquals("Can not perform update: no trainee with such id: null", message);

        message = assertThrows(IllegalArgumentException.class, () -> traineeDAO.update(10L, null)).getMessage();
        assertEquals("Can not perform update: no trainee with such id: 10", message);

        traineeDAO.save(testTrainee);

        message = assertThrows(IllegalArgumentException.class, () -> traineeDAO.update(1L, null)).getMessage();
        assertEquals("Can not perform update: trainee is null", message);
    }

    @Test
    public void updateValidParamsTest() {
        testTrainee.setFirstName("first");
        testTrainee.setPassword("test password");
        traineeDAO.save(testTrainee);

        Trainee newTrainee = new Trainee();
        newTrainee.setFirstName("update");
        newTrainee.setLastName("test");

        traineeDAO.update(1L, newTrainee);

        Trainee updatedTrainee = traineeDAO.findById(1L).get();


        assertEquals(1L, updatedTrainee.getId());
        assertEquals("update", updatedTrainee.getFirstName());
        assertEquals("test", updatedTrainee.getLastName());
        assertEquals("test password", updatedTrainee.getPassword());
        assertNull(updatedTrainee.getAddress());
        assertNull(updatedTrainee.getDateOfBirth());
        assertNull(updatedTrainee.getUsername());
        assertFalse(updatedTrainee.isActive());
    }

    @Test
    public void deleteTest() {
        String message = assertThrows(IllegalArgumentException.class, () -> traineeDAO.deleteById(null)).getMessage();
        assertEquals("Can not perform deleteById: id is null", message);

        assertFalse(traineeDAO.deleteById(1L));

        traineeDAO.save(testTrainee);
        assertTrue(traineeDAO.existsById(1L));

        traineeDAO.deleteById(1L);
        assertFalse(traineeDAO.existsById(1L));

        Collection<Trainee> trainees = (Collection<Trainee>) traineeDAO.findAll();
        assertTrue(trainees.isEmpty());
    }
}
