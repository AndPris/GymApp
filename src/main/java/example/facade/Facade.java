package example.facade;

import example.facade.handlers.TraineeOptionHandler;
import example.facade.handlers.TrainerOptionHandler;
import example.facade.handlers.TrainingOptionHandler;
import example.menu.Menu;
import example.utils.input.InputHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Facade {
    private static final Logger logger = LogManager.getLogger(Facade.class);

    private final TrainerOptionHandler trainerOptionHandler;
    private final TraineeOptionHandler traineeOptionHandler;
    private final TrainingOptionHandler trainingOptionHandler;

    private Menu menu;
    private InputHandler inputHandler;

    private boolean run;

    public Facade(TrainerOptionHandler trainerOptionHandler, TraineeOptionHandler traineeOptionHandler,
                  TrainingOptionHandler trainingOptionHandler) {
        this.traineeOptionHandler = traineeOptionHandler;
        this.trainingOptionHandler = trainingOptionHandler;
        this.trainerOptionHandler = trainerOptionHandler;
        this.run = true;
    }

    @Autowired
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Autowired
    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }


    public void run() {
        int userInput;

        while (run) {
            menu.displayMenu();
            userInput = inputHandler.getInputInRange(1, 20, false);
            logger.info("User selected option '{}'", userInput);

            try {
                handleUserInput(userInput);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void handleUserInput(int userInput) {
        switch (userInput) {
            case 1:
                traineeOptionHandler.createTrainee();
                break;
            case 2:
                trainerOptionHandler.createTrainer();
                break;
            case 3:
                trainingOptionHandler.createTraining(null);
                break;
            case 4:
                traineeOptionHandler.updateTrainee(null);
                break;
            case 5:
                trainerOptionHandler.updateTrainer(null);
                break;
            case 6:
                traineeOptionHandler.selectAllTrainees();
                break;
            case 7:
                trainerOptionHandler.selectAllTrainers();
                break;
            case 8:
                trainingOptionHandler.selectAllTrainings();
                break;
            case 9:
                traineeOptionHandler.selectTraineeByUsername(null);
                break;
            case 10:
                trainerOptionHandler.selectTrainerByUsername(null);
                break;
            case 11:
                traineeOptionHandler.deleteTraineeByUsername(null);
                break;
            case 12:
                traineeOptionHandler.changeTraineePassword(null);
                break;
            case 13:
                trainerOptionHandler.changeTrainerPassword(null);
                break;
            case 14:
                traineeOptionHandler.toggleTraineeIsActiveStatus(null);
                break;
            case 15:
                trainerOptionHandler.toggleTrainerIsActiveStatus(null);
                break;
            case 16:
                traineeOptionHandler.displayTraineeTrainingList(null);
                break;
            case 17:
                trainerOptionHandler.displayTrainerTrainingList(null);
                break;
            case 18:
                traineeOptionHandler.displayTrainersNotAssignedToTrainee(null);
                break;
            case 19:
                traineeOptionHandler.updateTraineeTrainerList(null);
                break;
            default:
                run = false;
                break;
        }
    }
}
