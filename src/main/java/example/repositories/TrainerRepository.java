package example.repositories;

import example.entities.Trainer;

import java.util.List;

public interface TrainerRepository {
    Trainer save(Trainer trainee);

    List<Trainer> findAll();
}
