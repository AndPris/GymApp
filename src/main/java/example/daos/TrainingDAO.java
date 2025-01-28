package example.daos;

import example.entities.Training;
import example.storages.TrainingStorage;
import example.utils.storages.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class TrainingDAO {
    private TrainingStorage trainingStorage;
    private IdGenerator idGenerator;
    private Map<Long, Training> trainingMap;

    @Autowired
    public void setTrainingStorage(final TrainingStorage trainingStorage) {
        this.trainingStorage = trainingStorage;
        this.trainingMap = trainingStorage.getTrainings();
    }

    @Autowired
    public void setIdGenerator(final IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }


    public Training save(Training training) {
        if(training == null)
            throw new IllegalArgumentException("Can not perform save: training is null");

        Long id;

        if(training.getId() == null) {
            id = idGenerator.generateId(trainingMap.keySet());
            training.setId(id);
        } else {
            id = training.getId();
        }

        trainingMap.put(id, training);

        return training;
    }

    public Iterable<Training> findAll() {
        return trainingMap.values();
    }

    public Optional<Training> findById(Long id) {
        if(id == null)
            throw new IllegalArgumentException("Can not perform findById: id is null");

        return trainingMap.containsKey(id) ? Optional.of(trainingMap.get(id)) : Optional.empty();
    }

    public boolean existsById(Long id) {
        return trainingMap.containsKey(id);
    }
}
