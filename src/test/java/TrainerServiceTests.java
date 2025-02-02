import example.daos.TrainerDAO;
import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.TrainingType;
import example.services.imp.TrainerServiceImp;
import example.storages.Storage;
import example.storages.imp.TraineeStorage;
import example.storages.imp.TrainerStorage;
import example.utils.id.IdGenerator;
import example.utils.id.imp.SimpleIdGenerator;
import example.utils.password.PasswordGenerator;
import example.utils.password.imp.SimplePasswordGenerator;
import example.utils.username.imp.SimpleUsernameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerServiceTests {
    private Storage<Trainer> storage;
    private Trainer testTrainer;
    private TrainerServiceImp trainerService;

    @BeforeEach
    public void init() throws IOException {
        IdGenerator idGenerator = new SimpleIdGenerator();
        PasswordGenerator passwordGenerator = new SimplePasswordGenerator();
        SimpleUsernameGenerator usernameGenerator = new SimpleUsernameGenerator();
        TrainerDAO trainerDAO = new TrainerDAO();

        storage = new TrainerStorage("t");
        storage.init();

        Storage<Trainee> traineeStorage = new TraineeStorage("t");
        traineeStorage.init();
        usernameGenerator.setTraineeStorage(traineeStorage);
        usernameGenerator.setTrainerStorage(storage);

        trainerDAO.setIdGenerator(idGenerator);
        trainerDAO.setTrainerStorage(storage);

        trainerService = new TrainerServiceImp();
        trainerService.setTrainerDAO(trainerDAO);
        trainerService.setPasswordGenerator(passwordGenerator);
        trainerService.setUsernameGenerator(usernameGenerator);
        testTrainer = new Trainer();
    }

    @Test
    public void createTrainerNullTest() {
        String message = assertThrows(IllegalArgumentException.class, () -> trainerService.createTrainer(null)).getMessage();
        assertEquals("Cannot perform createTrainer: trainer is null", message);
    }

    @Test
    public void createTrainerNoIdTest() {
        Trainer result = trainerService.createTrainer(testTrainer);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(storage.containsKey(1L));
        assertTrue(storage.values().contains(testTrainer));
    }

    @Test
    public void createTrainerWithIdTest() {
        testTrainer.setId(5L);
        Trainer result = trainerService.createTrainer(testTrainer);
        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertTrue(storage.containsKey(5L));
        assertTrue(storage.values().contains(testTrainer));
    }

    @Test
    public void createTrainerPasswordUsernameGenerationTest() {
        testTrainer.setFirstName("test");
        testTrainer.setLastName("test");
        Trainer result = trainerService.createTrainer(testTrainer);
        assertNotNull(result);
        assertEquals("test.test", result.getUsername());
        assertEquals(10, result.getPassword().length());
    }

    @Test
    public void updateTrainerInvalidParamsTest() {
        String message = assertThrows(IllegalArgumentException.class, () -> trainerService.updateTrainer(null, null)).getMessage();
        assertEquals("Cannot perform update: no trainer with such id: null", message);

        message = assertThrows(IllegalArgumentException.class, () -> trainerService.updateTrainer(10L, null)).getMessage();
        assertEquals("Cannot perform update: no trainer with such id: 10", message);

        trainerService.createTrainer(testTrainer);

        message = assertThrows(IllegalArgumentException.class, () -> trainerService.updateTrainer(1L, null)).getMessage();
        assertEquals("Cannot perform update: trainer is null", message);
    }

    @Test
    public void updateTrainerValidParamsTest() {
        testTrainer.setFirstName("first");
        testTrainer.setSpecialization(TrainingType.FITNESS);
        trainerService.createTrainer(testTrainer);

        Trainer newTrainer = new Trainer();
        newTrainer.setFirstName("update");
        newTrainer.setLastName("test");

        trainerService.updateTrainer(1L, newTrainer);

        Trainer updatedTrainer = trainerService.getTrainerById(1L).get();


        assertEquals(1L, updatedTrainer.getId());
        assertEquals("update", updatedTrainer.getFirstName());
        assertEquals("test", updatedTrainer.getLastName());
        assertEquals(TrainingType.FITNESS, updatedTrainer.getSpecialization());
        assertFalse(updatedTrainer.isActive());
    }

    @Test
    public void getAllTrainersTest() {
        Collection<Trainer> trainers = (Collection<Trainer>) trainerService.getAllTrainers();
        assertTrue(trainers.isEmpty());
        trainerService.createTrainer(testTrainer);
        assertEquals(1, trainers.size());
        assertTrue(trainers.contains(testTrainer));
    }

    @Test
    public void getTrainerByIdTest() {
        trainerService.createTrainer(testTrainer);

        String message = assertThrows(IllegalArgumentException.class, () -> trainerService.getTrainerById(null)).getMessage();
        assertEquals("Cannot find trainer by Id: id is null", message);

        Optional<Trainer> result = trainerService.getTrainerById(2L);
        assertFalse(result.isPresent());

        result = trainerService.getTrainerById(1L);
        assertEquals(testTrainer, result.get());
    }

    @Test
    public void existsTrainerTest() {
        assertFalse(trainerService.existsTrainer(10L));
        assertFalse(trainerService.existsTrainer(null));
        trainerService.createTrainer(testTrainer);
        assertTrue(trainerService.existsTrainer(1L));
    }
}
