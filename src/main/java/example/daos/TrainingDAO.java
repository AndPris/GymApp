package example.daos;

import example.entities.Training;
import example.storages.Storage;
import example.utils.storages.IdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class TrainingDAO {
    private static final Logger logger = LogManager.getLogger(TrainingDAO.class);

    private Storage<Training> trainingStorage;
    private IdGenerator idGenerator;
    private Map<Long, Training> trainingMap;

    @Autowired
    public void setTrainingStorage(final Storage<Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
        this.trainingMap = trainingStorage.getData();
    }

    @Autowired
    public void setIdGenerator(final IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public Training save(Training training) {
        if (training == null) {
            String message = "Can not perform save: training is null";
            logger.warn(message);
            throw new IllegalArgumentException(message);
        }

        Long id;

        if (training.getId() == null) {
            id = idGenerator.generateId(trainingMap.keySet());
            training.setId(id);
        } else {
            id = training.getId();
        }

        trainingMap.put(id, training);

        logger.info("Create new training: {}", training);
        return training;
    }

    public Iterable<Training> findAll() {
        logger.info("Get info about all trainings");
        return trainingMap.values();
    }

    public Optional<Training> findById(Long id) {
        if (id == null) {
            String message = "Can not find training by Id: id is null";
            logger.warn(message);
            throw new IllegalArgumentException(message);
        }

        logger.info("Get info about training with id {}", id);
        return trainingMap.containsKey(id) ? Optional.of(trainingMap.get(id)) : Optional.empty();
    }

    public boolean existsById(Long id) {
        logger.info("Check existence of training with id {}", id);
        return trainingMap.containsKey(id);
    }
}
