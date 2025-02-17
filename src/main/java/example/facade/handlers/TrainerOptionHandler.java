package example.facade.handlers;

import example.entities.Trainer;
import example.entities.Training;
import example.exceptions.TrainerNotFoundException;
import example.facade.handlers.utils.TrainingTypeUtil;
import example.services.TrainerService;
import example.utils.input.InputHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

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
        return new Trainer(firstName, lastName, trainingTypeUtil.getTrainingType(allowEmpty));
    }


    public void updateTrainer(Trainer trainer) {
        Trainer updates = getTrainerData(true);
        updates.setId(trainer.getId());
        updates.setActive(null);
        trainerService.updateTrainer(updates);
        System.out.println("Trainer has been successfully updated");
    }


    public void selectAllTrainers() {
        System.out.println("Trainers:");
        trainerService.getAllTrainers().forEach(System.out::println);
        System.out.println("================================");
    }


    public void selectTrainerByUsername(Trainer trainer) {
        String username = trainer.getUsername();
        Trainer resultTrainer = trainerService.getTrainerByUsername(username)
                .orElseThrow(() -> new TrainerNotFoundException("There is no trainer with such username"));
        System.out.println(resultTrainer);
    }


    public void changeTrainerPassword(Trainer trainer) {
        String username = trainer.getUsername();
        String oldPassword = inputHandler.getLine("Old password: ", false);
        String newPassword = inputHandler.getLine("New password: ", false);

        trainerService.changeTrainerPassword(username, oldPassword, newPassword);
        System.out.println("Password successfully changed");
    }


    public void toggleTrainerIsActiveStatus(Trainer trainer) {
        Long id = trainer.getId();

        boolean result = trainerService.toggleTrainerIsActiveStatus(id);
        System.out.println("Status was successfully changed. Current value: " + result);
    }


    public void displayTrainerTrainingList(Trainer trainer) {
        String username = trainer.getUsername();

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
