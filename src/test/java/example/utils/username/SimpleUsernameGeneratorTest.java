package example.utils.username;

import example.entities.Trainee;
import example.entities.Trainer;
import example.repositories.TraineeRepository;
import example.repositories.TrainerRepository;
import example.utils.username.imp.SimpleUsernameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimpleUsernameGeneratorTest {
    private SimpleUsernameGenerator usernameGenerator;
    private TraineeRepository traineeRepository;
    private TrainerRepository trainerRepository;

    @BeforeEach
    public void init() {
        traineeRepository = mock(TraineeRepository.class);
        trainerRepository = mock(TrainerRepository.class);

        usernameGenerator = new SimpleUsernameGenerator();
        usernameGenerator.setTraineeRepository(traineeRepository);
        usernameGenerator.setTrainerRepository(trainerRepository);
    }

    @Test
    public void generateUsernameTest_ShouldGenerateWithoutSerialNumber() {
        when(traineeRepository.findAll()).thenReturn(new ArrayList<>());
        when(trainerRepository.findAll()).thenReturn(new ArrayList<>());

        Trainee trainee = new Trainee();
        trainee.setFirstName("test");
        trainee.setLastName("test");

        assertEquals("test.test", usernameGenerator.generateUsername(trainee));
    }

    @Test
    public void generateUsernameTest_ShouldGenerateWithSerialNumber() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("test");
        trainee.setLastName("test");
        trainee.setUsername("test.test");

        Trainer trainer = new Trainer();
        trainer.setFirstName("test");
        trainer.setLastName("test");
        trainer.setUsername("test.test2");

        when(traineeRepository.findAll()).thenReturn(Arrays.asList(trainee));
        when(trainerRepository.findAll()).thenReturn(Arrays.asList(trainer));

        Trainee user = new Trainee();
        user.setFirstName("test");
        user.setLastName("test");

        assertEquals("test.test3", usernameGenerator.generateUsername(trainee));
    }
}
