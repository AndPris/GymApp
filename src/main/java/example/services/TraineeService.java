package example.services;

import example.entities.Trainee;
import example.entities.Training;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TraineeService {
    Trainee createTrainee(Trainee trainee);

    Trainee updateTrainee(Trainee updates);

    boolean deleteTraineeById(Long id);

    boolean deleteTraineeByUsername(String username);

    Iterable<Trainee> getAllTrainees();

    Optional<Trainee> getTraineeById(Long id);

    Optional<Trainee> getTraineeByUsername(String username);

    boolean existsTrainee(Long id);

    void changeTraineePassword(String username, String oldPassword, String newPassword);

    Trainee authenticateTrainee(String username, String password);

    boolean toggleTraineeIsActiveStatus(Long id);

    List<Training> findTraineeTrainingList(String username, Date fromDate, Date toDate, String trainerFirstName,
                                           String trainerLastName, String trainingType);
}
