package example.repositories.imp;

import example.entities.Training;
import example.repositories.TrainingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;

import static example.logs.TransactionLogger.getTransactionId;

@Repository
public class TrainingRepositoryImp implements TrainingRepository {
    private static final Logger logger = LogManager.getLogger(TrainingRepositoryImp.class);

    private final EntityManager entityManager;

    public TrainingRepositoryImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Training save(Training training) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            if (training.getId() == null) {
                entityManager.persist(training);
                logger.info("[Transaction ID: {}] Creating a new training: {}", getTransactionId(), training);
            } else {
                entityManager.merge(training);
                logger.info("[Transaction ID: {}] Updating a training {}", getTransactionId(), training);
            }

            transaction.commit();
            logger.info("[Transaction ID: {}] Operation successfully performed", getTransactionId());
            return training;
        } catch (Exception e) {
            transaction.rollback();
            logger.error("[Transaction ID: {}] {}", getTransactionId(), e.getMessage());
        }

        return null;
    }

    @Override
    public List<Training> findAll() {
        logger.info("[Transaction ID: {}] Find all trainings", getTransactionId());
        return entityManager.createQuery("select t from Training t")
                .getResultList();
    }
}
