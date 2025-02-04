package example.services;

import example.entities.Trainee;

import java.util.Optional;

public interface TraineeService {
    Trainee createTrainee(Trainee trainee);

    Trainee updateTrainee(Long id, Trainee trainee);

    boolean deleteTraineeById(Long id);

    Iterable<Trainee> getAllTrainees();

    Optional<Trainee> getTraineeById(Long id);

    boolean existsTrainee(Long id);
}
