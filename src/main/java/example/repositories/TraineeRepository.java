package example.repositories;

import example.entities.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository {
    Trainee save(Trainee trainee);

    List<Trainee> findAll();

    Optional<Trainee> findByUsername(String username);

    boolean deleteByUsername(String username);
}
