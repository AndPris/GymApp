package example.services.imp;

import example.entities.Trainer;
import example.services.TrainerService;

import java.util.Optional;

public class TrainerServiceImp implements TrainerService {
    @Override
    public Trainer createTrainer(Trainer trainer) {
        return null;
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        return null;
    }

    @Override
    public Iterable<Trainer> getAllTrainers() {
        return null;
    }

    @Override
    public Optional<Trainer> getTrainerById(Long id) {
        return Optional.empty();
    }
}
