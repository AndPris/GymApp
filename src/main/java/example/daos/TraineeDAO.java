package example.daos;

import example.entities.Trainee;
import example.storages.TraineeStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TraineeDAO {
    private TraineeStorage traineeStorage;

    @Autowired
    public void setTraineeStorage(final TraineeStorage traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    public Trainee save(Trainee trainee) {
        return null;
    }

    public void deleteById(Long id) {

    }

    public Iterable<Trainee> findAll() {
        return null;
    }

    public Optional<Trainee> findById(Long id) {
        return Optional.empty();
    }
}
