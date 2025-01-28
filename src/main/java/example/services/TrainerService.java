package example.services;

import example.entities.Trainer;

import java.util.Optional;

public interface TrainerService {
    Trainer createTrainer(Trainer trainer);
    Trainer updateTrainer(Trainer trainer);
    Iterable<Trainer> getAllTrainers();
    Optional<Trainer> getTrainerById(Long id);
    boolean existsTrainer(Long id);
}
