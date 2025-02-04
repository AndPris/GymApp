package example.daos;

import example.entities.Trainer;
import example.storages.Storage;
import example.utils.id.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TrainerDAO {
    private static final Logger logger = LogManager.getLogger(TrainerDAO.class);

    private Storage<Trainer> trainerStorage;
    private IdGenerator idGenerator;

    @Autowired
    public void setTrainerStorage(final Storage<Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Autowired
    public void setIdGenerator(final IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public Trainer save(Trainer trainer) {
        checkForNull(trainer, "Cannot perform save: trainer is null");

        assignIdIfNecessary(trainer);
        trainerStorage.put(trainer.getId(), trainer);

        logger.info("Created new trainer: {}", trainer);
        return trainer;
    }

    private void checkForNull(Object obj, String errorMessage) {
        if (obj == null) {
            logger.warn(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void assignIdIfNecessary(Trainer trainer) {
        if (trainer.getId() == null) {
            Long id = idGenerator.generateId(trainerStorage.keySet());
            trainer.setId(id);
        }
    }

    public Trainer update(Long id, Trainer trainer) {
        Trainer existingTrainer = trainerStorage.get(id);
        checkForNull(existingTrainer, "Cannot perform update: no trainer with such id: " + id);
        checkForNull(id, "Cannot perform update: id is null");
        checkForNull(trainer, "Cannot perform update: trainer is null");

        updateTrainerFields(existingTrainer, trainer);

        logger.info("Updated trainer: {}", existingTrainer);
        return existingTrainer;
    }

    private void updateTrainerFields(Trainer existing, Trainer updates) {
        Optional.ofNullable(updates.getFirstName()).filter(StringUtils::isNoneBlank).ifPresent(existing::setFirstName);
        Optional.ofNullable(updates.getLastName()).filter(StringUtils::isNoneBlank).ifPresent(existing::setLastName);
        Optional.ofNullable(updates.getUsername()).filter(StringUtils::isNoneBlank).ifPresent(existing::setUsername);
        Optional.ofNullable(updates.getPassword()).filter(StringUtils::isNoneBlank).ifPresent(existing::setPassword);
        Optional.ofNullable(updates.getSpecialization()).ifPresent(existing::setSpecialization);

        existing.setActive(updates.isActive());
    }


    public Iterable<Trainer> findAll() {
        logger.info("Get info about all trainers");
        return trainerStorage.values();
    }

    public Optional<Trainer> findById(Long id) {
        checkForNull(id, "Cannot find trainer by Id: id is null");
        logger.info("Get info about trainer with id {}", id);
        return Optional.ofNullable(trainerStorage.get(id));
    }

    public boolean existsById(Long id) {
        logger.info("Check existence of trainer with id {}", id);
        return trainerStorage.containsKey(id);
    }
}