package example.daos;

import example.entities.Trainee;
import example.storages.Storage;
import example.utils.id.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TraineeDAO {
    private static final Logger logger = LogManager.getLogger(TraineeDAO.class);

    private Storage<Trainee> traineeStorage;
    private IdGenerator idGenerator;

    @Autowired
    public void setTraineeStorage(final Storage<Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    @Autowired
    public void setIdGenerator(final IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }


    public Trainee save(Trainee trainee) {
        checkForNull(trainee, "Cannot perform save: trainee is null");
        assignIdIfNecessary(trainee);
        traineeStorage.put(trainee.getId(), trainee);

        logger.info("Created new trainee: {}", trainee);
        return trainee;
    }

    private void checkForNull(Object obj, String errorMessage) {
        if (obj == null) {
            logger.warn(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void assignIdIfNecessary(Trainee trainee) {
        if (trainee.getId() == null) {
            Long id = idGenerator.generateId(traineeStorage.keySet());
            trainee.setId(id);
        }
    }


    public Trainee update(Long id, Trainee trainee) {
        Trainee existingTrainee = traineeStorage.get(id);
        checkForNull(existingTrainee, "Cannot perform update: no trainee with such id: " + id);
        checkForNull(trainee, "Cannot perform update: trainee is null");

        updateTraineeFields(existingTrainee, trainee);

        logger.info("Updated trainee: {}", existingTrainee);
        return existingTrainee;
    }

    private void updateTraineeFields(Trainee existing, Trainee updates) {
        Optional.ofNullable(updates.getFirstName()).filter(StringUtils::isNoneBlank).ifPresent(existing::setFirstName);
        Optional.ofNullable(updates.getLastName()).filter(StringUtils::isNoneBlank).ifPresent(existing::setLastName);
        Optional.ofNullable(updates.getUsername()).filter(StringUtils::isNoneBlank).ifPresent(existing::setUsername);
        Optional.ofNullable(updates.getPassword()).filter(StringUtils::isNoneBlank).ifPresent(existing::setPassword);
        Optional.ofNullable(updates.getAddress()).filter(StringUtils::isNoneBlank).ifPresent(existing::setAddress);
        Optional.ofNullable(updates.getDateOfBirth()).ifPresent(existing::setDateOfBirth);

        existing.setActive(updates.isActive());
    }


    public boolean deleteById(Long id) {
        checkForNull(id, "Cannot perform deleteById: id is null");
        logger.info("Delete a trainee with id {}", id);
        return Optional.ofNullable(traineeStorage.remove(id)).isPresent();
    }

    public Iterable<Trainee> findAll() {
        logger.info("Get info about all trainees");
        return traineeStorage.values();
    }

    public Optional<Trainee> findById(Long id) {
        checkForNull(id, "Cannot find trainee by Id: id is null");
        logger.info("Get info about trainee with id {}", id);
        return Optional.ofNullable(traineeStorage.get(id));
    }

    public boolean existsById(Long id) {
        logger.info("Check existence of trainee with id {}", id);
        return traineeStorage.containsKey(id);
    }
}
