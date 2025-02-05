package example.repositories;

import example.entities.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository {
    Trainee save(Trainee trainee);

    List<Trainee> findAll();

    Optional<Trainee> findByUsername(String username);

    Optional<Trainee> findByUsernameAndPassword(String username, String password);

    boolean deleteByUsername(String username);

    boolean toggleIsActiveStatus(Long id);
}
