package example.daos;

import example.entities.Trainee;
import example.storages.TraineeStorage;
import example.utils.storages.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class TraineeDAO {
    private TraineeStorage traineeStorage;
    private IdGenerator idGenerator;
    private Map<Long, Trainee> traineeMap;

    @Autowired
    public void setTraineeStorage(final TraineeStorage traineeStorage) {
        this.traineeStorage = traineeStorage;
        this.traineeMap = traineeStorage.getTrainees();
    }

    @Autowired
    public void setIdGenerator(final IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }


    public Trainee save(Trainee trainee) {
        if(trainee == null)
            throw new IllegalArgumentException("Can not perform save: trainee is null");

        Long id;

        if(trainee.getId() == null) {
            id = idGenerator.generateId(traineeMap.keySet());
            trainee.setId(id);
        } else {
            id = trainee.getId();
        }

        traineeMap.put(id, trainee);

        return trainee;
    }

    public void deleteById(Long id) {
        if(id == null)
            throw new IllegalArgumentException("Can not perform deleteById: id is null");

        traineeMap.remove(id);
    }

    public Iterable<Trainee> findAll() {
        return traineeMap.values();
    }

    public Optional<Trainee> findById(Long id) {
        if(id == null)
            throw new IllegalArgumentException("Can not perform findById: id is null");

        return traineeMap.containsKey(id) ? Optional.of(traineeMap.get(id)) : Optional.empty();
    }

    public boolean existsById(Long id) {
        return traineeMap.containsKey(id);
    }
}
