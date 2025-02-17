package example.facade.handlers;

import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;
import example.exceptions.TraineeNotFoundException;
import example.exceptions.TrainerNotFoundException;
import example.menu.Menu;
import example.services.TraineeService;
import example.services.TrainerService;
import example.utils.input.InputHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TraineeOptionHandler {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final InputHandler inputHandler;
    private final Menu menu;

    public TraineeOptionHandler(TraineeService traineeService, TrainerService trainerService,
                                InputHandler inputHandler, Menu menu) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.inputHandler = inputHandler;
        this.menu = menu;
    }


    public void createTrainee() {
        if (traineeService.createTrainee(getTraineeData(false)) != null) {
            System.out.println("Trainee has been successfully created");
        } else {
            System.out.println("Error creating trainee");
        }
    }

    private Trainee getTraineeData(boolean allowEmpty) {
        String firstName = inputHandler.getLine("First name: ", allowEmpty);
        String lastName = inputHandler.getLine("Last name: ", allowEmpty);
        String address = inputHandler.getLine("Address: ", allowEmpty);

        return new Trainee(firstName, lastName, address, inputHandler.getDate("Birthday (dd-MM-yyyy): ", allowEmpty));
    }


    public void updateTrainee(Trainee trainee) {
        Trainee updates = getTraineeData(true);
        updates.setId(trainee.getId());
        updates.setActive(null);
        traineeService.updateTrainee(updates);
        System.out.println("Trainee has been successfully updated");
    }


    public void selectAllTrainees() {
        System.out.println("Trainees:");
        traineeService.getAllTrainees().forEach(System.out::println);
        System.out.println("================================");
    }


    public void selectTraineeByUsername(Trainee trainee) {
        String username = trainee.getUsername();
        Trainee resultTrainee = traineeService.getTraineeByUsername(username)
                .orElseThrow(() -> new TraineeNotFoundException("There is no trainee with such username"));
        System.out.println(resultTrainee);
    }


    public void deleteTraineeByUsername(Trainee trainee) {
        String username = trainee.getUsername();

        if (traineeService.deleteTraineeByUsername(username)) {
            System.out.println("Trainee successfully deleted");
        } else {
            System.out.println("There is no trainee with such username");
        }
    }


    public void changeTraineePassword(Trainee trainee) {
        String username = trainee.getUsername();
        String oldPassword = inputHandler.getLine("Old password: ", false);
        String newPassword = inputHandler.getLine("New password: ", false);

        traineeService.changeTraineePassword(username, oldPassword, newPassword);
        System.out.println("Password successfully changed");
    }


    public void toggleTraineeIsActiveStatus(Trainee trainee) {
        Long id = trainee.getId();

        boolean result = traineeService.toggleTraineeIsActiveStatus(id);
        System.out.println("Status was successfully changed. Current value: " + result);
    }


    public void displayTraineeTrainingList(Trainee trainee) {
        String username = trainee.getUsername();

        Date fromDate = inputHandler.getDate("From date (dd-MM-yyyy): ", true);
        Date toDate = inputHandler.getDate("To date (dd-MM-yyyy): ", true);
        String trainerFirstName = inputHandler.getLine("Trainer's first name: ", true);
        String trainerLastName = inputHandler.getLine("Trainer's last name: ", true);
        String trainingType = inputHandler.getLine("Training type: ", true);

        List<Training> trainings = traineeService.findTraineeTrainingList(username, fromDate, toDate,
                trainerFirstName, trainerLastName, trainingType);
        System.out.println("Trainings list:");
        trainings.forEach(System.out::println);
    }


    public void displayTrainersNotAssignedToTrainee(Trainee trainee) {
        String username = trainee.getUsername();

        System.out.println("Trainers:");
        traineeService.findTrainersNotAssignedToTrainee(username).forEach(System.out::println);
        System.out.println("================================");
    }


    public void updateTraineeTrainerList(Trainee trainee) {
        menu.displayUpdateTraineeTrainerListMenu();
        int choice = inputHandler.getInputInRange(1, 4, false);
        handleUpdateTraineeTrainerListChoice(choice, trainee);
    }

    private void handleUpdateTraineeTrainerListChoice(int choice, Trainee trainee) {
        switch (choice) {
            case 1:
                displayTrainerList(trainee);
                break;
            case 2:
                addTrainerToTraineeTrainerList(trainee);
                break;
            case 3:
                removeTrainerFromTraineeTrainerList(trainee);
                break;
            case 4:
                clearTraineeTrainerList(trainee);
                break;
        }
    }

    private void displayTrainerList(Trainee trainee) {
        System.out.println("Trainer list:");
        trainee.getTrainers().forEach(System.out::println);
        System.out.println("================================");
    }

    private void addTrainerToTraineeTrainerList(Trainee trainee) {
        Trainer trainer = getTrainer();
        traineeService.addTrainerToList(trainee, trainer);
        System.out.println("Trainer successfully added");
    }

    private Trainer getTrainer() {
        String username = inputHandler.getLine("Trainer username: ", false);

        return trainerService.getTrainerByUsername(username)
                .orElseThrow(() -> new TrainerNotFoundException("There's no trainer with such username"));
    }

    private void removeTrainerFromTraineeTrainerList(Trainee trainee) {
        Trainer trainer = getTrainer();
        traineeService.removeTrainerFromList(trainee, trainer);
        System.out.println("Trainer successfully removed");
    }

    private void clearTraineeTrainerList(Trainee trainee) {
        traineeService.clearTraineeTrainerList(trainee);
        System.out.println("List successfully cleared");
    }
}
