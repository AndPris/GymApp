package example.services.imp;

import example.entities.Training;
import example.services.TrainingService;

import java.util.Optional;

public class TrainingServiceImp implements TrainingService {
    @Override
    public Training createTraining(Training training) {
        return null;
    }

    @Override
    public Iterable<Training> getAllTrainings() {
        return null;
    }

    @Override
    public Optional<Training> getTrainingById(Long id) {
        return Optional.empty();
    }
}
