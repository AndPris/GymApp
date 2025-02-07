package example.services;

import example.daos.TraineeDAO;
import example.entities.Trainee;
import example.entities.Trainer;
import example.services.imp.TraineeServiceImp;
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

public class TraineeServiceTests {
    private Storage<Trainee> storage;
    private Trainee testTrainee;
    private TraineeServiceImp traineeService;

    @BeforeEach
    public void init() throws IOException {
        IdGenerator idGenerator = new SimpleIdGenerator();
        PasswordGenerator passwordGenerator = new SimplePasswordGenerator();
        SimpleUsernameGenerator usernameGenerator = new SimpleUsernameGenerator();
        TraineeDAO traineeDAO = new TraineeDAO();

        storage = new TraineeStorage("t");
        storage.init();

        Storage<Trainer> trainerStorage = new TrainerStorage("t");
        trainerStorage.init();
//        usernameGenerator.setTrainerStorage(trainerStorage);
//        usernameGenerator.setTraineeStorage(storage);

        traineeDAO.setIdGenerator(idGenerator);
        traineeDAO.setTraineeStorage(storage);

        traineeService = new TraineeServiceImp();
        traineeService.setTraineeDAO(traineeDAO);
        traineeService.setPasswordGenerator(passwordGenerator);
        traineeService.setUsernameGenerator(usernameGenerator);
        testTrainee = new Trainee();
    }

    @Test
    public void createTraineeNullTest() {
        String message = assertThrows(IllegalArgumentException.class, () -> traineeService.createTrainee(null)).getMessage();
        assertEquals("Cannot perform createTrainee: trainee is null", message);
    }

    @Test
    public void createTraineeNoIdTest() {
        Trainee result = traineeService.createTrainee(testTrainee);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(storage.containsKey(1L));
        assertTrue(storage.values().contains(testTrainee));
    }

    @Test
    public void createTraineeWithIdTest() {
        testTrainee.setId(5L);
        Trainee result = traineeService.createTrainee(testTrainee);
        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertTrue(storage.containsKey(5L));
        assertTrue(storage.values().contains(testTrainee));
    }

    @Test
    public void createTraineePasswordUsernameGenerationTest() {
        testTrainee.setFirstName("test");
        testTrainee.setLastName("test");
        Trainee result = traineeService.createTrainee(testTrainee);
        assertNotNull(result);
        assertEquals("test.test", result.getUsername());
        assertEquals(10, result.getPassword().length());
    }

    @Test
    public void updateTraineeInvalidParamsTest() {
//        String message = assertThrows(IllegalArgumentException.class, () -> traineeService.updateTrainee(null, null)).getMessage();
//        assertEquals("Cannot perform update: no trainee with such id: null", message);
//
//        message = assertThrows(IllegalArgumentException.class, () -> traineeService.updateTrainee(10L, null)).getMessage();
//        assertEquals("Cannot perform update: no trainee with such id: 10", message);
//
//        traineeService.createTrainee(testTrainee);
//
//        message = assertThrows(IllegalArgumentException.class, () -> traineeService.updateTrainee(1L, null)).getMessage();
//        assertEquals("Cannot perform update: trainee is null", message);
    }

    @Test
    public void updateTraineeValidParamsTest() {
//        testTrainee.setFirstName("first");
//        testTrainee.setAddress("addr");
//        traineeService.createTrainee(testTrainee);
//
//        Trainee newTrainee = new Trainee();
//        newTrainee.setFirstName("update");
//        newTrainee.setLastName("test");
//
//        traineeService.updateTrainee(1L, newTrainee);
//
//        Trainee updatedTrainee = traineeService.getTraineeById(1L).get();
//
//
//        assertEquals(1L, updatedTrainee.getId());
//        assertEquals("update", updatedTrainee.getFirstName());
//        assertEquals("test", updatedTrainee.getLastName());
//        assertEquals("addr", updatedTrainee.getAddress());
//        assertNull(updatedTrainee.getDateOfBirth());
//        assertFalse(updatedTrainee.isActive());
    }

    @Test
    public void getAllTraineesTest() {
        Collection<Trainee> trainees = (Collection<Trainee>) traineeService.getAllTrainees();
        assertTrue(trainees.isEmpty());
        traineeService.createTrainee(testTrainee);
        assertEquals(1, trainees.size());
        assertTrue(trainees.contains(testTrainee));
    }

    @Test
    public void getTraineeByIdTest() {
        traineeService.createTrainee(testTrainee);

        String message = assertThrows(IllegalArgumentException.class, () -> traineeService.getTraineeById(null)).getMessage();
        assertEquals("Cannot find trainee by Id: id is null", message);

        Optional<Trainee> result = traineeService.getTraineeById(2L);
        assertFalse(result.isPresent());

        result = traineeService.getTraineeById(1L);
        assertEquals(testTrainee, result.get());
    }

    @Test
    public void existsTraineeByIdTest() {
        assertFalse(traineeService.existsTraineeById(10L));
        assertFalse(traineeService.existsTraineeById(null));
        traineeService.createTrainee(testTrainee);
        assertTrue(traineeService.existsTraineeById(1L));
    }

    @Test
    public void deleteTraineeByIdTest() {
        String message = assertThrows(IllegalArgumentException.class, () -> traineeService.deleteTraineeById(null)).getMessage();
        assertEquals("Cannot perform deleteById: id is null", message);

        assertFalse(traineeService.deleteTraineeById(1L));

        traineeService.createTrainee(testTrainee);
        assertTrue(traineeService.existsTraineeById(1L));

        traineeService.deleteTraineeById(1L);
        assertFalse(traineeService.existsTraineeById(1L));

        Collection<Trainee> trainees = (Collection<Trainee>) traineeService.getAllTrainees();
        assertTrue(trainees.isEmpty());
    }
}
