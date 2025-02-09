//package example.utils.username;
//
//import example.entities.Trainee;
//import example.entities.Trainer;
//import example.storages.Storage;
//import example.storages.imp.TraineeStorage;
//import example.storages.imp.TrainerStorage;
//import example.utils.username.imp.SimpleUsernameGenerator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class SimpleUsernameGeneratorTest {
//    private Storage<Trainee> traineeStorage;
//    private Storage<Trainer> trainerStorage;
//    private SimpleUsernameGenerator usernameGenerator;
//
//    @BeforeEach
//    public void init() throws IOException {
//        traineeStorage = new TraineeStorage("t");
//        trainerStorage = new TrainerStorage("t");
//
//        trainerStorage.init();
//        traineeStorage.init();
//
//        usernameGenerator = new SimpleUsernameGenerator();
////        usernameGenerator.setTraineeStorage(traineeStorage);
////        usernameGenerator.setTrainerStorage(trainerStorage);
//    }
//
//    @Test
//    public void generateUsernameTest1() {
//        Trainee trainee = new Trainee();
//        trainee.setFirstName("test");
//        trainee.setLastName("test");
//
//        assertEquals("test.test", usernameGenerator.generateUsername(trainee));
//    }
//
//    @Test
//    public void generateUsernameTest2() {
//        Trainee trainee = new Trainee();
//        trainee.setFirstName("test");
//        trainee.setLastName("test");
//        trainee.setUsername("test.test");
//        traineeStorage.put(1L, trainee);
//
//        Trainer trainer = new Trainer();
//        trainer.setFirstName("test");
//        trainer.setLastName("test");
//        trainer.setUsername("test.test2");
//        trainerStorage.put(1L, trainer);
//
//
//        Trainee user = new Trainee();
//        user.setFirstName("test");
//        user.setLastName("test");
//
//        assertEquals("test.test3", usernameGenerator.generateUsername(trainee));
//    }
//}
