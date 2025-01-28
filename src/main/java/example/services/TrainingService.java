package example.services;

import example.entities.Training;

import java.util.Optional;

public interface TrainingService {
    Training createTraining(Training training);
    Iterable<Training> getAllTrainings();
    Optional<Training> getTrainingById(Long id);
    boolean existsTraining(Long trainingId);
}
