package example.facade.handlers;

import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;
import example.menu.Menu;
import example.services.TraineeService;
import example.services.TrainerService;
import example.utils.input.InputHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        traineeService.createTrainee(getTraineeData(false));
        System.out.println("Trainee has been successfully created");
    }

    private Trainee getTraineeData(boolean allowEmpty) {
        String firstName = inputHandler.getLine("First name: ", allowEmpty);
        String lastName = inputHandler.getLine("Last name: ", allowEmpty);
        String address = inputHandler.getLine("Address: ", allowEmpty);

        return new Trainee(firstName, lastName, address, inputHandler.getDate("Birthday (dd-MM-yyyy): ", allowEmpty));
    }


    public void updateTrainee() {
        Long id = getTraineeId();
        if (id == null)
            return;

        Trainee updates = getTraineeData(true);
        updates.setId(id);
        updates.setActive(null);
        traineeService.updateTrainee(updates);
        System.out.println("Trainee has been successfully updated");
    }

    private Long getTraineeId() {
        System.out.print("Trainee id: ");
        Long traineeId = inputHandler.getLong();
        if (!traineeService.existsTraineeById(traineeId)) {
            System.out.println("There's no trainee with such ID");
            return null;
        }

        return traineeId;
    }


    public void selectAllTrainees() {
        System.out.println("Trainees:");
        traineeService.getAllTrainees().forEach(System.out::println);
        System.out.println("================================");
    }


    public void selectTraineeByUsername(Trainee trainee) {
        String username = trainee.getUsername();
        Optional<Trainee> optionalTrainee = traineeService.getTraineeByUsername(username);

        if (optionalTrainee.isPresent()) {
            System.out.println(optionalTrainee.get());
        } else {
            System.out.println("There is no trainee with such username");
        }
    }


    public void deleteTraineeByUsername() {
        String username = inputHandler.getLine("Trainee username: ", false);

        if (traineeService.deleteTraineeByUsername(username)) {
            System.out.println("Trainee successfully deleted");
        } else {
            System.out.println("There is no trainee with such username");
        }
    }


    public void changeTraineePassword() {
        String username = inputHandler.getLine("Trainee username: ", false);
        String oldPassword = inputHandler.getLine("Old password: ", false);
        String newPassword = inputHandler.getLine("New password: ", false);

        try {
            traineeService.changeTraineePassword(username, oldPassword, newPassword);
            System.out.println("Password successfully changed");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void toggleTraineeIsActiveStatus() {
        System.out.print("Trainee id: ");
        Long id = inputHandler.getLong();

        try {
            boolean result = traineeService.toggleTraineeIsActiveStatus(id);
            System.out.println("Status was successfully changed. Current value: " + result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void displayTraineeTrainingList() {
        String username = inputHandler.getLine("Trainee username: ", false);
        if (!traineeService.existsTraineeByUsername(username)) {
            System.out.println("There's no trainee with such username");
            return;
        }

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


    public void displayTrainersNotAssignedToTrainee() {
        String username = inputHandler.getLine("Trainee username: ", false);
        if (!traineeService.existsTraineeByUsername(username)) {
            System.out.println("There's no trainee with such username");
            return;
        }

        System.out.println("Trainers:");
        traineeService.findTrainersNotAssignedToTrainee(username).forEach(System.out::println);
        System.out.println("================================");
    }


    public void updateTraineeTrainerList() {
        String username = inputHandler.getLine("Trainee username: ", false);
        if (!traineeService.existsTraineeByUsername(username)) {
            System.out.println("There's no trainee with such username");
            return;
        }

        Trainee trainee = traineeService.getTraineeByUsername(username).get();
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
        if (trainer != null) {
            traineeService.addTrainerToList(trainee, trainer);
            System.out.println("Trainer successfully added");
        } else {
            System.out.println("Cannot add trainer");
        }
    }

    private Trainer getTrainer() {
        String username = inputHandler.getLine("Trainer username: ", false);
        if (!trainerService.existsTrainerByUsername(username)) {
            System.out.println("There's no trainer with such username");
            return null;
        }

        return trainerService.getTrainerByUsername(username).get();
    }

    private void removeTrainerFromTraineeTrainerList(Trainee trainee) {
        Trainer trainer = getTrainer();
        if (trainer != null) {
            traineeService.removeTrainerFromList(trainee, trainer);
            System.out.println("Trainer successfully removed");
        } else {
            System.out.println("Cannot remove trainer");
        }
    }

    private void clearTraineeTrainerList(Trainee trainee) {
        traineeService.clearTraineeTrainerList(trainee);
        System.out.println("List successfully cleared");
    }
}
