package example.repositories;

import example.entities.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository {
    Trainer save(Trainer trainee);

    List<Trainer> findAll();

    Optional<Trainer> findById(Long id);

    Optional<Trainer> findByUsername(String username);

    Optional<Trainer> findByUsernameAndPassword(String username, String password);
}
