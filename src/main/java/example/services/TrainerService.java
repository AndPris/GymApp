package example.services;

import example.entities.Trainer;
import example.entities.Training;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Trainer createTrainer(Trainer trainer);

    Trainer updateTrainer(Trainer updates);

    Iterable<Trainer> getAllTrainers();

    Optional<Trainer> getTrainerById(Long id);

    Optional<Trainer> getTrainerByUsername(String username);

    boolean existsTrainerById(Long id);

    boolean existsTrainerByUsername(String username);

    void changeTrainerPassword(String username, String oldPassword, String newPassword);

    Trainer authenticateTrainer(String username, String password);

    boolean toggleTrainerIsActiveStatus(Long id);

    List<Training> findTrainerTrainingList(String username, Date fromDate, Date toDate,
                                           String traineeFirstName, String traineeLastName);
}
