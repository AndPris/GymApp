package example.daos;

import example.entities.Training;
import example.storages.Storage;
import example.utils.id.IdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TrainingDAO {
    private static final Logger logger = LogManager.getLogger(TrainingDAO.class);

    private Storage<Training> trainingStorage;
    private IdGenerator idGenerator;

    @Autowired
    public void setTrainingStorage(final Storage<Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    @Autowired
    public void setIdGenerator(final IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public Training save(Training training) {
        checkForNull(training, "Cannot perform save: training is null");

        assignIdIfNecessary(training);
        trainingStorage.put(training.getId(), training);

        logger.info("Create new training: {}", training);
        return training;
    }

    private void checkForNull(Object obj, String errorMessage) {
        if (obj == null) {
            logger.warn(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void assignIdIfNecessary(Training training) {
        if (training.getId() == null) {
            Long id = idGenerator.generateId(trainingStorage.keySet());
            training.setId(id);
        }
    }


    public Iterable<Training> findAll() {
        logger.info("Get info about all trainings");
        return trainingStorage.values();
    }

    public Optional<Training> findById(Long id) {
        checkForNull(id, "Cannot find training by Id: id is null");
        logger.info("Get info about training with id {}", id);
        return Optional.ofNullable(trainingStorage.get(id));
    }

    public boolean existsById(Long id) {
        logger.info("Check existence of training with id {}", id);
        return trainingStorage.containsKey(id);
    }
}
