package example.validation;

import example.entities.*;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomValidatorTests {
    private CustomValidator customValidator;

    @BeforeEach
    public void setUp() {
        this.customValidator = new CustomValidator();
    }

    @ParameterizedTest
    @MethodSource("invalidUsers")
    public void validateUserTest_ShouldThrow(User user, String errorMessage) {
        Exception e = assertThrows(ValidationException.class, () -> customValidator.validate(user));
        assertEquals(errorMessage, e.getMessage());
    }

    private static Stream<Arguments> invalidUsers() {
        Trainee trainee1 = new Trainee("test", "test", null, null);
        trainee1.setPassword("1");

        Trainee trainee2 = new Trainee("test", "test", null, null);
        trainee2.setPassword("1111111111111111111111111111111111111111111");

        Trainee trainee3 = new Trainee("test", "test", null, null);
        trainee3.setUsername("1");

        Trainee trainee4 = new Trainee("test", "test", null, null);
        trainee4.setUsername("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");

        Trainee trainee5 = new Trainee("test", "test", null, null);
        trainee5.setPassword("123456");
        trainee5.setUsername("test.test");
        trainee5.setActive(null);

        return Stream.of(
                Arguments.of(new Trainee(null, "last", null, null),
                        "Validation error: First name must be provided"),
                Arguments.of(new Trainee("test", null, null, null),
                        "Validation error: Last name must be provided"),
                Arguments.of(new Trainee("f", "last", null, null),
                        "Validation error: First name must be from 2 to 20 characters"),
                Arguments.of(new Trainee("fgggggggggggggggggggggggggggggggggggggggggggggggg", "last", null, null),
                        "Validation error: First name must be from 2 to 20 characters"),
                Arguments.of(new Trainee("first", "l", null, null),
                        "Validation error: Last name must be from 2 to 20 characters"),
                Arguments.of(new Trainee("first", "lllllllllllllllllllllllllllllllllllllllllllllll", null, null),
                        "Validation error: Last name must be from 2 to 20 characters"),
                Arguments.of(trainee1, "Validation error: Password must be from 6 to 20 characters"),
                Arguments.of(trainee2, "Validation error: Password must be from 6 to 20 characters"),
                Arguments.of(trainee3, "Validation error: Username must be from 2 to 50 characters"),
                Arguments.of(trainee4, "Validation error: Username must be from 2 to 50 characters"),
                Arguments.of(trainee5, "Validation error: Active must be provided")
        );
    }


    @ParameterizedTest
    @MethodSource("invalidTrainings")
    public void validateTrainingsTest_ShouldThrow(Training training, String errorMessage) {
        Exception e = assertThrows(ValidationException.class, () -> customValidator.validate(training));
        assertEquals(errorMessage, e.getMessage());
    }

    private static Stream<Arguments> invalidTrainings() {
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        TrainingType trainingType = new TrainingType();
        Date trainingDate = new Date();

        return Stream.of(
                Arguments.of(new Training(null, trainer, "test", trainingType, trainingDate, 120),
                        "Validation error: Trainee must be provided"),
                Arguments.of(new Training(trainee, null, "test", trainingType, trainingDate, 120),
                        "Validation error: Trainer must be provided"),
                Arguments.of(new Training(trainee, trainer, null, trainingType, trainingDate, 120),
                        "Validation error: Training name must be provided"),
                Arguments.of(new Training(trainee, trainer, "t", trainingType, trainingDate, 120),
                        "Validation error: Training name must be from 2 to 20 characters"),
                Arguments.of(new Training(trainee, trainer, "ttttttttttttttttttttttttttttttttttttttttttttttttttttt", trainingType, trainingDate, 120),
                        "Validation error: Training name must be from 2 to 20 characters"),
                Arguments.of(new Training(trainee, trainer, "test", null, trainingDate, 120),
                        "Validation error: Training type must be provided"),
                Arguments.of(new Training(trainee, trainer, "test", trainingType, null, 120),
                        "Validation error: Training date must be provided"),
                Arguments.of(new Training(trainee, trainer, "test", trainingType, trainingDate, null),
                        "Validation error: Training duration must be provided"),
                Arguments.of(new Training(trainee, trainer, "test", trainingType, trainingDate, -10),
                        "Validation error: Minimal training duration is 20 minutes"),
                Arguments.of(new Training(trainee, trainer, "test", trainingType, trainingDate, 500),
                        "Validation error: Maximal training duration is 180 minutes")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidTrainers")
    public void validateTrainersTest_ShouldThrow(Trainer trainer, String errorMessage) {
        Exception e = assertThrows(ValidationException.class, () -> customValidator.validate(trainer));
        assertEquals(errorMessage, e.getMessage());
    }

    private static Stream<Arguments> invalidTrainers() {
        Trainer trainer = new Trainer("test", "test", null);
        trainer.setPassword("123456");
        trainer.setUsername("test.test");

        return Stream.of(
                Arguments.of(trainer, "Validation error: Specialization must be provided")
        );
    }
}