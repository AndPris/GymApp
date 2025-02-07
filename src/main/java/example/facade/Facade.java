package example.facade;

import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;
import example.entities.TrainingType;
import example.menu.Menu;
import example.services.TraineeService;
import example.services.TrainerService;
import example.services.TrainingService;
import example.services.TrainingTypeService;
import example.utils.input.InputHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class Facade {
    private static final Logger logger = LogManager.getLogger(Facade.class);

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    private Menu menu;
    private InputHandler inputHandler;

    private boolean run;

    public Facade(TraineeService traineeService, TrainerService trainerService,
                  TrainingService trainingService, TrainingTypeService trainingTypeService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
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
            userInput = inputHandler.getInputInRange(1, 21, false);
            logger.info("User selected option '{}'", userInput);
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
            case 13:
                selectTraineeByUsername();
                break;
            case 14:
                selectTrainerByUsername();
                break;
            case 15:
                deleteTraineeByUsername();
                break;
            case 16:
                changeTraineePassword();
                break;
            case 17:
                changeTrainerPassword();
                break;
            case 18:
                toggleTraineeIsActiveStatus();
                break;
            case 19:
                toggleTrainerIsActiveStatus();
                break;
            case 20:
                displayTraineeTrainingList();
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
        String firstName = inputHandler.getLine("First name: ", allowEmpty);
        String lastName = inputHandler.getLine("Last name: ", allowEmpty);
        String address = inputHandler.getLine("Address: ", allowEmpty);

        return new Trainee(firstName, lastName, address, inputHandler.getDate("Birthday (dd-MM-yyyy): ", allowEmpty));
    }


    private void createTrainer() {
        trainerService.createTrainer(getTrainerData(false));
        System.out.println("Trainer has been successfully created");
    }

    private Trainer getTrainerData(boolean allowEmpty) {
        String firstName = inputHandler.getLine("First name: ", allowEmpty);
        String lastName = inputHandler.getLine("Last name: ", allowEmpty);

        try {
            return new Trainer(firstName, lastName, getTrainingType(allowEmpty));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private TrainingType getTrainingType(boolean allowEmpty) {
        List<TrainingType> trainingTypes = trainingTypeService.findAll();
        menu.displayTrainingTypeMenu(trainingTypes);

        Integer choice = inputHandler.getInputInRange(1, trainingTypes.size(), allowEmpty);
        if (choice == null) {
            return null;
        }

        Optional<TrainingType> optionalTrainingType = trainingTypeService.findById((long) choice);
        if (optionalTrainingType.isPresent()) {
            return optionalTrainingType.get();
        } else {
            throw new RuntimeException("There's no training type with id " + choice);
        }
    }


    private void createTraining() {
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
        TrainingType trainingType = getTrainingType(allowEmpty);
        Date trainingDate = inputHandler.getDate("Training date (dd-MM-yyyy): ", allowEmpty);
        System.out.print("Training duration: ");
        Integer trainingDuration = inputHandler.getInteger(allowEmpty);

        return new Training(optionalTrainee.get(), optionalTrainer.get(), trainingName,
                trainingType, trainingDate, trainingDuration);
    }


    private void updateTrainee() {
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
        if (!traineeService.existsTrainee(traineeId)) {
            System.out.println("There's no trainee with such ID");
            return null;
        }

        return traineeId;
    }

    private void updateTrainer() {
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
        if (!trainerService.existsTrainer(trainerId)) {
            System.out.println("There's no trainer with such ID");
            return null;
        }

        return trainerId;
    }

    private void deleteTrainee() {
        System.out.print("Trainee id: ");
        Long traineeId = inputHandler.getLong();

        if (traineeService.deleteTraineeById(traineeId))
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
        if (traineeId == null)
            return;

        System.out.println(traineeService.getTraineeById(traineeId).get());
        System.out.println("================================");
    }


    private void selectTrainer() {
        Long trainerId = getTrainerId();
        if (trainerId == null)
            return;

        System.out.println(trainerService.getTrainerById(trainerId).get());
        System.out.println("================================");
    }


    private void selectTraining() {
        Long trainingId = getTrainingId();
        if (trainingId == null)
            return;

        System.out.println(trainingService.getTrainingById(trainingId).get());
        System.out.println("================================");
    }

    private Long getTrainingId() {
        System.out.print("Training id: ");
        Long trainingId = inputHandler.getLong();
        if (!trainingService.existsTraining(trainingId)) {
            System.out.println("There's no training with such ID");
            return null;
        }

        return trainingId;
    }


    private void selectTraineeByUsername() {
        String username = inputHandler.getLine("Trainee username: ", false);
        Optional<Trainee> optionalTrainee = traineeService.getTraineeByUsername(username);

        if (optionalTrainee.isPresent()) {
            System.out.println(optionalTrainee.get());
        } else {
            System.out.println("There is no trainee with such username");
        }
    }

    private void selectTrainerByUsername() {
        String username = inputHandler.getLine("Trainer username: ", false);
        Optional<Trainer> optionalTrainer = trainerService.getTrainerByUsername(username);

        if (optionalTrainer.isPresent()) {
            System.out.println(optionalTrainer.get());
        } else {
            System.out.println("There is no trainer with such username");
        }
    }


    private void deleteTraineeByUsername() {
        String username = inputHandler.getLine("Trainee username: ", false);

        if (traineeService.deleteTraineeByUsername(username)) {
            System.out.println("Trainee successfully deleted");
        } else {
            System.out.println("There is no trainee with such username");
        }
    }


    private void changeTraineePassword() {
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


    private void changeTrainerPassword() {
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


    private void toggleTraineeIsActiveStatus() {
        System.out.print("Trainee id: ");
        Long id = inputHandler.getLong();

        try {
            boolean result = traineeService.toggleTraineeIsActiveStatus(id);
            System.out.println("Status was successfully changed. Current value: " + result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void toggleTrainerIsActiveStatus() {
        System.out.print("Trainer id: ");
        Long id = inputHandler.getLong();

        try {
            boolean result = trainerService.toggleTrainerIsActiveStatus(id);
            System.out.println("Status was successfully changed. Current value: " + result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void displayTraineeTrainingList() {
        String username = inputHandler.getLine("Trainee username: ", false);
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
}
