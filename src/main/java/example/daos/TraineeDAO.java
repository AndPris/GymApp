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
        if (trainee == null) {
            String message = "Can not perform save: trainee is null";
            logger.warn(message);
            throw new IllegalArgumentException(message);
        }

        Long id;

        if (trainee.getId() == null) {
            id = idGenerator.generateId(traineeStorage.keySet());
            trainee.setId(id);
        } else {
            id = trainee.getId();
        }

        traineeStorage.put(id, trainee);

        logger.info("Create new trainee: {}", trainee);
        return trainee;
    }

    public Trainee update(Long id, Trainee trainee) {
        Trainee oldTrainee = traineeStorage.get(id);

        if (oldTrainee == null) {
            String message = "Can not perform update: no trainee with such id: " + id;
            logger.warn(message);
            throw new IllegalArgumentException(message);
        }

        if (trainee == null) {
            String message = "Can not perform update: trainee is null";
            logger.warn(message);
            throw new IllegalArgumentException(message);
        }


        if (StringUtils.isNoneBlank(trainee.getFirstName()))
            oldTrainee.setFirstName(trainee.getFirstName());

        if (StringUtils.isNoneBlank(trainee.getLastName()))
            oldTrainee.setLastName(trainee.getLastName());

        if (StringUtils.isNoneBlank(trainee.getUsername()))
            oldTrainee.setUsername(trainee.getUsername());

        if (StringUtils.isNoneBlank(trainee.getPassword()))
            oldTrainee.setPassword(trainee.getPassword());

        oldTrainee.setActive(trainee.isActive());

        if (StringUtils.isNoneBlank(trainee.getAddress()))
            oldTrainee.setAddress(trainee.getAddress());

        if (trainee.getDateOfBirth() != null)
            oldTrainee.setDateOfBirth(trainee.getDateOfBirth());

        logger.info("Update a trainee: {}", oldTrainee);
        return oldTrainee;
    }

    public boolean deleteById(Long id) {
        if (id == null) {
            String message = "Can not perform deleteById: id is null";
            logger.warn(message);
            throw new IllegalArgumentException(message);
        }

        logger.info("Delete a trainee with id {}", id);
        return traineeStorage.remove(id) != null;
    }

    public Iterable<Trainee> findAll() {
        logger.info("Get info about all trainees");
        return traineeStorage.values();
    }

    public Optional<Trainee> findById(Long id) {
        if (id == null) {
            String message = "Can not find trainee by Id: id is null";
            logger.warn(message);
            throw new IllegalArgumentException(message);
        }

        logger.info("Get info about trainee with id {}", id);
        return traineeStorage.containsKey(id) ? Optional.of(traineeStorage.get(id)) : Optional.empty();
    }

    public boolean existsById(Long id) {
        logger.info("Check existence of trainee with id {}", id);
        return traineeStorage.containsKey(id);
    }
}
