package example.repositories.imp;

import example.entities.Trainer;
import example.repositories.TrainerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

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
                logger.info("Creating a new trainer: {}", trainer);
            } else {
                entityManager.merge(trainer);
                logger.info("Updating a trainer {}", trainer);
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

    @Override
    public Optional<Trainer> findByUsername(String username) {
        Query query = entityManager.createQuery("select t from Trainer t where t.username=:username");
        query.setParameter("username", username);

        List<Trainer> result = query.getResultList();
        logger.info("Find trainer with username {}. Result: {}", username, result);
        return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
    }

    @Override
    public Optional<Trainer> findByUsernameAndPassword(String username, String password) {
        Query query = entityManager.createQuery("select t from Trainer t where t.username=:username and t.password=:password");
        query.setParameter("username", username);
        query.setParameter("password", password);

        List<Trainer> result = query.getResultList();
        logger.info("Find trainer with username {} and password {}. Result: {}", username, password, result);
        return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
    }
}
