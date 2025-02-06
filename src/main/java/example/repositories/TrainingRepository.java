package example.repositories;

import example.entities.Training;

import java.util.List;

public interface TrainingRepository {
    Training save(Training training);

    List<Training> findAll();
}
