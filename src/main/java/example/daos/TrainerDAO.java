package example.daos;

import example.entities.Trainer;
import example.storages.TrainerStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TrainerDAO {
    private TrainerStorage trainerStorage;

    @Autowired
    public void setTrainerStorage(final TrainerStorage trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    public Trainer save(Trainer trainer) {
        return null;
    }

    public Iterable<Trainer> findAll() {
        return null;
    }

    public Optional<Trainer> findById(Long id) {
        return Optional.empty();
    }
}
