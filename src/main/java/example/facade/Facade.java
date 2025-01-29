package example.facade;

import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;
import example.entities.TrainingType;
import example.menu.Menu;
import example.services.TraineeService;
import example.services.TrainerService;
import example.services.TrainingService;
import example.utils.input.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Scanner;

@Component
public class Facade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    private Menu menu;
    private InputHandler inputHandler;

    private boolean run;

    public Facade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
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
            userInput = inputHandler.getInputInRange(1, 13);
            handleUserInput(userInput);
        }
    }

    private void handleUserInput(int userInput) {
        switch (userInput) {
            case 1:
                createTrainee();
                break;
            case 2:
                createTrainer();
                break;
            case 3:
                createTraining();
                break;
            case 4:
                updateTrainee();
                break;
            case 5:
                updateTrainer();
                break;
            case 6:
                deleteTrainee();
                break;
            case 7:
                selectAllTrainees();
                break;
            case 8:
                selectAllTrainers();
                break;
            case 9:
                selectAllTrainings();
                break;
            case 10:
                selectTrainee();
                break;
            case 11:
                selectTrainer();
                break;
            case 12:
                selectTraining();
                break;
            default:
                run = false;
                break;
        }
    }


    private void createTrainee() {
        traineeService.createTrainee(getTraineeData(false));
        System.out.println("Trainee has been successfully created");
    }

    private Trainee getTraineeData(boolean allowEmpty) {
        System.out.print("First name: ");
        String firstName = inputHandler.getLine(allowEmpty);
        System.out.print("Last name: ");
        String lastName = inputHandler.getLine(allowEmpty);
        System.out.print("Address: ");
        String address = inputHandler.getLine(allowEmpty);

        return new Trainee(firstName, lastName, address, inputHandler.getDate("Birthday (dd-MM-yyyy): "));
    }


    private void createTrainer() {
        trainerService.createTrainer(getTrainerData(false));
        System.out.println("Trainer has been successfully created");
    }

    private Trainer getTrainerData(boolean allowEmpty) {
        System.out.print("First name: ");
        String firstName = inputHandler.getLine(allowEmpty);
        System.out.print("Last name: ");
        String lastName = inputHandler.getLine(allowEmpty);

        return new Trainer(firstName, lastName, getTrainingType());
    }

    private TrainingType getTrainingType() {
        menu.displayTrainingTypeMenu();
        int choice = inputHandler.getInputInRange(1, 3);

        switch (choice) {
            case 1:
                return TrainingType.FITNESS;
            case 2:
                return TrainingType.PILATES;
            default:
                return TrainingType.ATHLETICS;
        }
    }


    private void createTraining() {
        try {
            trainingService.createTraining(getTrainingData());
            System.out.println("Training has been successfully created");
        } catch (Exception e) {}
    }

    private Training getTrainingData() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Trainee id: ");
        Long traineeId = inputHandler.getLong();
        if(!traineeService.existsTrainee(traineeId)) {
            System.out.println("No such trainee");
            return null;
        }

        System.out.print("Trainer id: ");
        Long trainerId = inputHandler.getLong();
        if(!trainerService.existsTrainer(trainerId)) {
            System.out.println("No such trainer");
            return null;
        }

        System.out.print("Training name: ");
        String trainingName = scanner.nextLine();
        TrainingType trainingType = getTrainingType();
        Date trainingDate = inputHandler.getDate("Training date (dd-MM-yyyy): ");
        System.out.print("Training duration: ");
        float trainingDuration = inputHandler.getFloat();

        return new Training(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
    }


    private void updateTrainee() {
        Long id = getTraineeId();
        if(id == null)
            return;

        traineeService.updateTrainee(id, getTraineeData(true));
        System.out.println("Trainee has been successfully updated");
    }

    private Long getTraineeId() {
        System.out.print("Trainee id: ");
        Long traineeId = inputHandler.getLong();
        if(!traineeService.existsTrainee(traineeId)) {
            System.out.println("There's no trainee with such ID");
            return null;
        }

        return traineeId;
    }

    private void updateTrainer() {
        Long id = getTrainerId();
        if(id == null)
            return;

        trainerService.updateTrainer(id, getTrainerData(true));
        System.out.println("Trainer has been successfully updated");
    }

    private Long getTrainerId() {
        System.out.print("Trainer id: ");
        Long trainerId = inputHandler.getLong();
        if(!trainerService.existsTrainer(trainerId)) {
            System.out.println("There's no trainer with such ID");
            return null;
        }

        return trainerId;
    }

    private void deleteTrainee() {
        System.out.print("Trainee id: ");
        Long traineeId = inputHandler.getLong();

        if(traineeService.deleteTraineeById(traineeId))
            System.out.println("Trainee has been successfully deleted");
        else
            System.out.println("There is no trainee with such id");
    }


    private void selectAllTrainees() {
        System.out.println("Trainees:");
        traineeService.getAllTrainees().forEach(System.out::println);
        System.out.println("================================");
    }


    private void selectAllTrainers() {
        System.out.println("Trainers:");
        trainerService.getAllTrainers().forEach(System.out::println);
        System.out.println("================================");
    }


    private void selectAllTrainings() {
        System.out.println("Trainings:");
        trainingService.getAllTrainings().forEach(System.out::println);
        System.out.println("================================");
    }


    private void selectTrainee() {
        Long traineeId = getTraineeId();
        if(traineeId == null)
            return;

        System.out.println(traineeService.getTraineeById(traineeId).get());
        System.out.println("================================");
    }


    private void selectTrainer() {
        Long trainerId = getTrainerId();
        if(trainerId == null)
            return;

        System.out.println(trainerService.getTrainerById(trainerId).get());
        System.out.println("================================");
    }


    private void selectTraining() {
        Long trainingId = getTrainingId();
        if(trainingId == null)
            return;

        System.out.println(trainingService.getTrainingById(trainingId).get());
        System.out.println("================================");
    }

    private Long getTrainingId() {
        System.out.print("Training id: ");
        Long trainingId = inputHandler.getLong();
        if(!trainingService.existsTraining(trainingId)) {
            System.out.println("There's no training with such ID");
            return null;
        }

        return trainingId;
    }
}
