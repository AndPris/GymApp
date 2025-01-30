package example.daos;

import example.entities.Trainer;
import example.storages.Storage;
import example.utils.storages.IdGenerator;
import example.utils.string.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class TrainerDAO {
    private static final Logger logger = LogManager.getLogger(TrainerDAO.class);

    private Storage<Trainer> trainerStorage;
    private IdGenerator idGenerator;
    private Map<Long, Trainer> trainerMap;

    @Autowired
    public void setTrainerStorage(final Storage<Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
        this.trainerMap = trainerStorage.getData();
    }

    @Autowired
    public void setIdGenerator(final IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public Trainer save(Trainer trainer) {
        if (trainer == null) {
            String message = "Can not perform save: trainer is null";
            logger.warn(message);
            throw new IllegalArgumentException(message);
        }

        Long id;
        if (trainer.getId() == null) {
            id = idGenerator.generateId(trainerMap.keySet());
            trainer.setId(id);
        } else {
            id = trainer.getId();
        }

        trainerMap.put(id, trainer);
        logger.info("Create new trainer: {}", trainer);
        return trainer;
    }

    public Trainer update(Long id, Trainer trainer) {
        Trainer oldTrainer = trainerMap.get(id);

        if (oldTrainer == null) {
            String message = "Can not perform update: no trainer with such id: " + id;
            logger.warn(message);
            throw new IllegalArgumentException(message);
        }

        if (trainer == null) {
            String message = "Can not perform update: trainer is null";
            logger.warn(message);
            throw new IllegalArgumentException(message);
        }

        if (StringUtils.isNotEmpty(trainer.getFirstName()))
            oldTrainer.setFirstName(trainer.getFirstName());

        if (StringUtils.isNotEmpty(trainer.getLastName()))
            oldTrainer.setLastName(trainer.getLastName());

        if (StringUtils.isNotEmpty(trainer.getUsername()))
            oldTrainer.setUsername(trainer.getUsername());

        if (StringUtils.isNotEmpty(trainer.getPassword()))
            oldTrainer.setPassword(trainer.getPassword());

        oldTrainer.setActive(trainer.isActive());
        oldTrainer.setSpecialization(trainer.getSpecialization());

        logger.info("Update a trainer: {}", oldTrainer);
        return oldTrainer;
    }

    public boolean deleteById(Long id) {
        if (id == null) {
            String message = "Can not perform deleteById: id is null";
            logger.warn(message);
            throw new IllegalArgumentException(message);
        }

        logger.info("Delete a trainer with id {}", id);
        return trainerMap.remove(id) != null;
    }

    public Iterable<Trainer> findAll() {
        logger.info("Get info about all trainers");
        return trainerMap.values();
    }

    public Optional<Trainer> findById(Long id) {
        if (id == null) {
            String message = "Can not find trainer by id: id is null";
            logger.warn(message);
            throw new IllegalArgumentException(message);
        }

        logger.info("Get info about trainer with id {}", id);
        return trainerMap.containsKey(id) ? Optional.of(trainerMap.get(id)) : Optional.empty();
    }

    public boolean existsById(Long id) {
        logger.info("Check existence of trainer with id {}", id);
        return trainerMap.containsKey(id);
    }
}