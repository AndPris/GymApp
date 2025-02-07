package example.repositories;

import example.entities.Trainee;
import example.entities.Training;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TraineeRepository {
    Trainee save(Trainee trainee);

    List<Trainee> findAll();

    Optional<Trainee> findById(Long id);

    Optional<Trainee> findByUsername(String username);

    Optional<Trainee> findByUsernameAndPassword(String username, String password);

    boolean deleteByUsername(String username);

    List<Training> findTrainingList(String username, Date fromDate, Date toDate, String trainerFirstName,
                                    String trainerLastName, String trainingType);
}
