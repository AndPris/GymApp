package example.repositories.imp;

import example.entities.Trainer;
import example.entities.Training;
import example.repositories.TrainerRepository;
import static example.logs.TransactionLogger.getTransactionId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainerRepositoryImp implements TrainerRepository {
    private static final Logger logger = LogManager.getLogger(TrainerRepositoryImp.class);

    private final EntityManager entityManager;

    public TrainerRepositoryImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Trainer save(Trainer trainer) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            if (trainer.getId() == null) {
                entityManager.persist(trainer);
                logger.info("[Transaction ID: {}] Creating a new trainer: {}", getTransactionId(), trainer);
            } else {
                entityManager.merge(trainer);
                logger.info("[Transaction ID: {}] Updating a trainer {}", getTransactionId(), trainer);
            }

            transaction.commit();
            logger.info("[Transaction ID: {}] Operation successfully performed", getTransactionId());
            return trainer;
        } catch (Exception e) {
            transaction.rollback();
            logger.error("[Transaction ID: {}] {}", getTransactionId(), e.getMessage());
        }

        return null;
    }

    @Override
    public List<Trainer> findAll() {
        logger.info("[Transaction ID: {}] Find all trainers", getTransactionId());
        return entityManager.createQuery("select t from Trainer t " +
                        "left outer join Training tr on t.id=tr.trainee.id")
                .getResultList();
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        Trainer trainer = entityManager.find(Trainer.class, id);
        logger.info("[Transaction ID: {}] Find trainer by id: {}. Result: {}", getTransactionId(), id, trainer);
        return Optional.ofNullable(trainer);
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        Query query = entityManager.createQuery("select t from Trainer t where t.username=:username");
        query.setParameter("username", username);

        List<Trainer> result = query.getResultList();
        logger.info("[Transaction ID: {}] Find trainer with username {}. Result: {}", getTransactionId(), username, result);
        return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
    }

    @Override
    public Optional<Trainer> findByUsernameAndPassword(String username, String password) {
        Query query = entityManager.createQuery("select t from Trainer t where t.username=:username and t.password=:password");
        query.setParameter("username", username);
        query.setParameter("password", password);

        List<Trainer> result = query.getResultList();
        logger.info("[Transaction ID: {}] Find trainer with username {} and password {}. Result: {}",
                getTransactionId(), username, password, result);
        return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
    }

    @Override
    public List<Training> findTrainingList(String username, Date fromDate, Date toDate,
                                           String traineeFirstName, String traineeLastName) {

        Query query = entityManager.createQuery("select t from Training t " +
                "where t.trainer.username=:username " +
                "and (:fromDate is null or t.trainingDate > :fromDate) " +
                "and (:toDate is null or t.trainingDate < :toDate) " +
                "and (:traineeFirstName is null or t.trainee.firstName=:traineeFirstName) " +
                "and (:traineeLastName is null or t.trainee.lastName=:traineeLastName)");

        query.setParameter("username", username);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        query.setParameter("traineeFirstName", traineeFirstName);
        query.setParameter("traineeLastName", traineeLastName);

        List<Training> result = query.getResultList();
        logger.info("[Transaction ID: {}] Find training list of trainee with username {}, from date {}, to date {}," +
                        " trainee first name {}, trainee last name {}. Result: {}",
                getTransactionId(), username, fromDate, toDate, traineeFirstName, traineeLastName, result);
        return result;
    }
}
