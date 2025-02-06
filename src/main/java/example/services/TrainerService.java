package example.services;

import example.entities.Trainer;

import java.util.Optional;

public interface TrainerService {
    Trainer createTrainer(Trainer trainer);

    Trainer updateTrainer(Long id, Trainer trainer);

    Iterable<Trainer> getAllTrainers();

    Optional<Trainer> getTrainerById(Long id);

    Optional<Trainer> getTrainerByUsername(String username);

    boolean existsTrainer(Long id);

    void changeTrainerPassword(String username, String oldPassword, String newPassword);

    Trainer authenticateTrainer(String username, String password);
}
