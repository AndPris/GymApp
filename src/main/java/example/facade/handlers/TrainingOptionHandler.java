package example.facade.handlers;

import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;
import example.entities.TrainingType;
import example.facade.handlers.utils.TrainingTypeUtil;
import example.services.TraineeService;
import example.services.TrainerService;
import example.services.TrainingService;
import example.utils.input.InputHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class TrainingOptionHandler {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeUtil trainingTypeUtil;
    private final InputHandler inputHandler;

    public TrainingOptionHandler(TraineeService traineeService, TrainerService trainerService,
                                 TrainingService trainingService, InputHandler inputHandler,
                                 TrainingTypeUtil trainingTypeUtil) {

        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.trainingTypeUtil = trainingTypeUtil;
        this.inputHandler = inputHandler;
    }

    public void createTraining() {
        Training data = getTrainingData();
        if (data == null)
            return;
        trainingService.createTraining(data);
        System.out.println("Training has been successfully created");
    }

    private Training getTrainingData() {
        System.out.print("Trainee id: ");
        Long traineeId = inputHandler.getLong();
        Optional<Trainee> optionalTrainee = traineeService.getTraineeById(traineeId);
        if (!optionalTrainee.isPresent()) {
            System.out.println("No such trainee");
            return null;
        }

        System.out.print("Trainer id: ");
        Long trainerId = inputHandler.getLong();
        Optional<Trainer> optionalTrainer = trainerService.getTrainerById(trainerId);
        if (!optionalTrainer.isPresent()) {
            System.out.println("No such trainer");
            return null;
        }

        boolean allowEmpty = false;
        String trainingName = inputHandler.getLine("Training name: ", allowEmpty);
        TrainingType trainingType = trainingTypeUtil.getTrainingType(allowEmpty);
        Date trainingDate = inputHandler.getDate("Training date (dd-MM-yyyy): ", allowEmpty);
        System.out.print("Training duration: ");
        Integer trainingDuration = inputHandler.getInteger(allowEmpty);

        return new Training(optionalTrainee.get(), optionalTrainer.get(), trainingName,
                trainingType, trainingDate, trainingDuration);
    }


    public void selectAllTrainings() {
        System.out.println("Trainings:");
        trainingService.getAllTrainings().forEach(System.out::println);
        System.out.println("================================");
    }
}
