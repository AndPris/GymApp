package example.services;

import example.entities.Trainee;

import java.util.Optional;

public interface TraineeService {
    Trainee createTrainee(Trainee trainee);

    Trainee updateTrainee(Trainee trainee);

    boolean deleteTraineeById(Long id);

    boolean deleteTraineeByUsername(String username);

    Iterable<Trainee> getAllTrainees();

    Optional<Trainee> getTraineeById(Long id);

    Optional<Trainee> getTraineeByUsername(String username);

    boolean existsTrainee(Long id);

    void changeTraineePassword(String username, String oldPassword, String newPassword);

    Trainee authenticateTrainee(String username, String password);

    boolean toggleTraineeIsActiveStatus(Long id);
}
