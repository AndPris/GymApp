package example.services;

import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TraineeService {
    Trainee createTrainee(Trainee trainee);

    Trainee updateTrainee(Trainee updates);

    boolean deleteTraineeByUsername(String username);

    Iterable<Trainee> getAllTrainees();

    Optional<Trainee> getTraineeById(Long id);

    Optional<Trainee> getTraineeByUsername(String username);

    boolean existsTraineeById(Long id);

    boolean existsTraineeByUsername(String username);

    void changeTraineePassword(String username, String oldPassword, String newPassword);

    boolean toggleTraineeIsActiveStatus(Long id);

    List<Training> findTraineeTrainingList(String username, Date fromDate, Date toDate, String trainerFirstName,
                                           String trainerLastName, String trainingType);

    void addTrainerToList(Trainee trainee, Trainer trainer);

    void removeTrainerFromList(Trainee trainee, Trainer trainer);

    void clearTraineeTrainerList(Trainee trainee);

    List<Trainer> findTrainersNotAssignedToTrainee(String username);
}
