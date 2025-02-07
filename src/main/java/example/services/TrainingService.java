package example.services;

import example.entities.Training;

public interface TrainingService {
    Training createTraining(Training training);

    Iterable<Training> getAllTrainings();
}
