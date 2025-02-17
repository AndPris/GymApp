package example.facade.handlers;

import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;
import example.entities.TrainingType;
import example.exceptions.TraineeNotFoundException;
import example.facade.handlers.utils.TrainingTypeUtil;
import example.services.TraineeService;
import example.services.TrainingService;
import example.utils.input.InputHandler;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TrainingOptionHandler {
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    private final TrainingTypeUtil trainingTypeUtil;
    private final InputHandler inputHandler;

    public TrainingOptionHandler(TraineeService traineeService, TrainingService trainingService,
                                 InputHandler inputHandler, TrainingTypeUtil trainingTypeUtil) {

        this.traineeService = traineeService;
        this.trainingService = trainingService;
        this.trainingTypeUtil = trainingTypeUtil;
        this.inputHandler = inputHandler;
    }

    public void createTraining(Trainer trainer) {
        Training data = getTrainingData(trainer);

        trainingService.createTraining(data);
        System.out.println("Training has been successfully created");
    }

    private Training getTrainingData(Trainer trainer) {
        System.out.print("Trainee id: ");
        Long traineeId = inputHandler.getLong();
        Trainee trainee = traineeService.getTraineeById(traineeId)
                .orElseThrow(() -> new TraineeNotFoundException("No such trainee"));

        boolean allowEmpty = false;
        String trainingName = inputHandler.getLine("Training name: ", allowEmpty);
        TrainingType trainingType = trainingTypeUtil.getTrainingType(allowEmpty);
        Date trainingDate = inputHandler.getDate("Training date (dd-MM-yyyy): ", allowEmpty);
        System.out.print("Training duration: ");
        Integer trainingDuration = inputHandler.getInteger(allowEmpty);

        return new Training(trainee, trainer, trainingName,
                trainingType, trainingDate, trainingDuration);
    }


    public void selectAllTrainings() {
        System.out.println("Trainings:");
        trainingService.getAllTrainings().forEach(System.out::println);
        System.out.println("================================");
    }
}
