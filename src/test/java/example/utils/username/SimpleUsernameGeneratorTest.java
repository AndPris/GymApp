package example.utils.username;

import example.entities.Trainee;
import example.entities.Trainer;
import example.repositories.UserRepository;
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
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        userRepository = mock(UserRepository.class);

        usernameGenerator = new SimpleUsernameGenerator();
        usernameGenerator.setUserRepository(userRepository);
    }

    @Test
    public void generateUsernameTest_ShouldGenerateWithoutSerialNumber() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        Trainee trainee = new Trainee();
        trainee.setFirstName("test");
        trainee.setLastName("test");

        assertEquals("test.test", usernameGenerator.generateUsername(trainee));
    }

    @Test
    public void generateUsernameTest_ShouldGenerateWithSerialNumber() {
        Trainee trainee1 = new Trainee();
        trainee1.setUsername("test.test");

        Trainee trainee2 = new Trainee();
        trainee2.setUsername("another.username");

        Trainer trainer1 = new Trainer();
        trainer1.setUsername("test.test2");

        Trainer trainer2 = new Trainer();
        trainer2.setUsername("test.testt3");

        when(userRepository.findAll()).thenReturn(Arrays.asList(trainee1, trainee2, trainer1, trainer2));

        Trainee user = new Trainee();
        user.setFirstName("test");
        user.setLastName("test");

        assertEquals("test.test3", usernameGenerator.generateUsername(user));
    }
}
