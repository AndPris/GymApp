package example.repositories.imp;

import example.entities.Trainer;
import example.repositories.TrainerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                logger.info("Creating a new trainer: " + trainer);
            } else {
                entityManager.merge(trainer);
                logger.info("Updating a trainer " + trainer);
            }

            transaction.commit();
            logger.info("Operation successfully performed");
            return trainer;
        } catch (Exception e) {
            transaction.rollback();
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public List<Trainer> findAll() {
        logger.info("Find all trainers");
        return entityManager.createQuery("select t from Trainer t")
                .getResultList();
    }
}
