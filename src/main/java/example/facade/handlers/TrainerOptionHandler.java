package example.facade.handlers;

import example.entities.Trainer;
import example.entities.Training;
import example.facade.handlers.utils.TrainingTypeUtil;
import example.services.TrainerService;
import example.utils.input.InputHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class TrainerOptionHandler {
    private final TrainerService trainerService;
    private final InputHandler inputHandler;
    private final TrainingTypeUtil trainingTypeUtil;

    public TrainerOptionHandler(TrainerService trainerService, InputHandler inputHandler,
                                TrainingTypeUtil trainingTypeUtil) {
        this.trainerService = trainerService;
        this.inputHandler = inputHandler;
        this.trainingTypeUtil = trainingTypeUtil;
    }


    public void createTrainer() {
        trainerService.createTrainer(getTrainerData(false));
        System.out.println("Trainer has been successfully created");
    }

    private Trainer getTrainerData(boolean allowEmpty) {
        String firstName = inputHandler.getLine("First name: ", allowEmpty);
        String lastName = inputHandler.getLine("Last name: ", allowEmpty);

        try {
            return new Trainer(firstName, lastName, trainingTypeUtil.getTrainingType(allowEmpty));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public void updateTrainer() {
        Long id = getTrainerId();
        if (id == null)
            return;

        Trainer updates = getTrainerData(true);
        updates.setId(id);
        updates.setActive(null);
        trainerService.updateTrainer(updates);
        System.out.println("Trainer has been successfully updated");
    }

    private Long getTrainerId() {
        System.out.print("Trainer id: ");
        Long trainerId = inputHandler.getLong();
        if (!trainerService.existsTrainerById(trainerId)) {
            System.out.println("There's no trainer with such ID");
            return null;
        }

        return trainerId;
    }


    public void selectAllTrainers() {
        System.out.println("Trainers:");
        trainerService.getAllTrainers().forEach(System.out::println);
        System.out.println("================================");
    }


    public void selectTrainerByUsername() {
        String username = inputHandler.getLine("Trainer username: ", false);
        Optional<Trainer> optionalTrainer = trainerService.getTrainerByUsername(username);

        if (optionalTrainer.isPresent()) {
            System.out.println(optionalTrainer.get());
        } else {
            System.out.println("There is no trainer with such username");
        }
    }


    public void changeTrainerPassword() {
        String username = inputHandler.getLine("Trainer username: ", false);
        String oldPassword = inputHandler.getLine("Old password: ", false);
        String newPassword = inputHandler.getLine("New password: ", false);

        try {
            trainerService.changeTrainerPassword(username, oldPassword, newPassword);
            System.out.println("Password successfully changed");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void toggleTrainerIsActiveStatus() {
        System.out.print("Trainer id: ");
        Long id = inputHandler.getLong();

        try {
            boolean result = trainerService.toggleTrainerIsActiveStatus(id);
            System.out.println("Status was successfully changed. Current value: " + result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void displayTrainerTrainingList() {
        String username = inputHandler.getLine("Trainer username: ", false);
        if (!trainerService.existsTrainerByUsername(username)) {
            System.out.println("There's no trainer with such username");
            return;
        }

        Date fromDate = inputHandler.getDate("From date (dd-MM-yyyy): ", true);
        Date toDate = inputHandler.getDate("To date (dd-MM-yyyy): ", true);
        String trainerFirstName = inputHandler.getLine("Trainee's first name: ", true);
        String trainerLastName = inputHandler.getLine("Trainee's last name: ", true);

        List<Training> trainings = trainerService.findTrainerTrainingList(username, fromDate, toDate,
                trainerFirstName, trainerLastName);
        System.out.println("Trainings list:");
        trainings.forEach(System.out::println);
    }
}
