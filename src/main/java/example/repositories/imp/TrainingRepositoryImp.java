package example.repositories.imp;

import example.entities.Training;
import example.repositories.TrainingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                logger.info("Creating a new training: {}", training);
            } else {
                entityManager.merge(training);
                logger.info("Updating a training {}", training);
            }

            transaction.commit();
            logger.info("Operation successfully performed");
            return training;
        } catch (Exception e) {
            transaction.rollback();
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public List<Training> findAll() {
        logger.info("Find all trainings");
        return entityManager.createQuery("select t from Training t")
                .getResultList();
    }
}
