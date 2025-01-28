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
import java.util.Optional;
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
            userInput = inputHandler.getUserInput(1, 13);
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
            default:
                run = false;
                break;
        }
    }


    private void createTrainee() {
        traineeService.createTrainee(getTraineeData());
        System.out.println("Trainee has been successfully created");
    }

    private Trainee getTraineeData() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("First name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();

        return new Trainee(firstName, lastName, address, inputHandler.getDate("Birthday (dd-MM-yyyy): "));
    }


    private void createTrainer() {
        trainerService.createTrainer(getTrainerData());
        System.out.println("Trainer has been successfully created");
    }

    private Trainer getTrainerData() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("First name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine();

        return new Trainer(firstName, lastName, getTrainingType());
    }

    private TrainingType getTrainingType() {
        menu.displayTrainingTypeMenu();
        int choice = inputHandler.getUserInput(1, 3);

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
        trainingService.createTraining(getTrainingData());
        System.out.println("Training has been successfully created");
    }

    private Training getTrainingData() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Trainee id: ");
        Long traineeId = inputHandler.getLong();
        Optional<Trainee> trainee = traineeService.getTraineeById(traineeId);
        if(!trainee.isPresent()) {
            System.out.println("No such trainee");
            return null;
        }

        System.out.print("Trainer id: ");
        Long trainerId = inputHandler.getLong();
        Optional<Trainer> trainer = trainerService.getTrainerById(trainerId);
        if(!trainer.isPresent()) {
            System.out.println("No such trainer");
            return null;
        }

        System.out.print("Training name: ");
        String trainingName = scanner.nextLine();
        TrainingType trainingType = getTrainingType();
        Date trainingDate = inputHandler.getDate("Training date (dd-MM-yyyy): ");
        float trainingDuration = inputHandler.getFloat();

        return new Training(trainee.get(), trainer.get(), trainingName, trainingType, trainingDate, trainingDuration);
    }


    private void updateTrainee() {
        System.out.print("Trainee id: ");
        Long traineeId = inputHandler.getLong();
        if(!traineeService.existsTrainee(traineeId)) {
            System.out.println("There's no trainee with such ID");
            return;
        }

        traineeService.updateTrainee(getTraineeData());
        System.out.println("Trainee has been successfully updated");
    }


    private void updateTrainer() {
        System.out.print("Trainer id: ");
        Long trainerId = inputHandler.getLong();
        if(!trainerService.existsTrainer(trainerId)) {
            System.out.println("There's no trainer with such ID");
            return;
        }

        trainerService.updateTrainer(getTrainerData());
        System.out.println("Trainer has been successfully updated");
    }


    private void deleteTrainee() {
        System.out.print("Trainee id: ");
        Long traineeId = inputHandler.getLong();

        if(traineeService.deleteTraineeById(traineeId))
            System.out.println("Trainee has been successfully deleted");
        else
            System.out.println("There is no trainee with such id");
    }
}
