package example.daos;

import example.entities.Training;
import example.storages.TrainingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TrainingDAO {
    private TrainingStorage trainingStorage;

    @Autowired
    public void setTrainingStorage(final TrainingStorage trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    public Training save(Training training) {
        return null;
    }

    public Iterable<Training> findAll() {
        return null;
    }

    public Optional<Training> findById(Long id) {
        return Optional.empty();
    }
}
