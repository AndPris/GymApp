package example.repositories.imp;

import example.entities.Trainee;
import example.repositories.TraineeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TraineeRepositoryImp implements TraineeRepository {
    private static final Logger logger = LogManager.getLogger(TraineeRepositoryImp.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Trainee save(Trainee trainee) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            if (trainee.getId() == null) {
                entityManager.persist(trainee);
                logger.info("Creating a new trainee: {}", trainee);
            } else {
                entityManager.merge(trainee);
                logger.info("Updating a trainee {}", trainee);
            }

            transaction.commit();
            logger.info("Operation successfully performed");
            return trainee;
        } catch (Exception e) {
            transaction.rollback();
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        Query query = entityManager.createQuery("select t from Trainee t where t.username=:username");
        query.setParameter("username", username);

        List<Trainee> result = query.getResultList();
        logger.info("Find trainee with username " + username + ". Result: " + result);
        return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
    }

    @Override
    public Optional<Trainee> findByUsernameAndPassword(String username, String password) {
        Query query = entityManager.createQuery("select t from Trainee t where t.username=:username and t.password=:password");
        query.setParameter("username", username);
        query.setParameter("password", password);

        List<Trainee> result = query.getResultList();
        logger.info("Find trainee with username " + username + " and password " + password + ". Result: " + result);
        return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
    }

    @Override
    public List<Trainee> findAll() {
        logger.info("Find all trainees");
        return entityManager.createQuery("select t from Trainee t")
                .getResultList();
    }

    @Override
    public boolean deleteByUsername(String username) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            logger.info("Deleting a trainee with username " + username);
            int result = entityManager.createQuery("delete from Trainee t where t.username=:username")
                    .setParameter("username", username)
                    .executeUpdate();

            transaction.commit();
            logger.info("Result: " + result);
            return result != 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean toggleIsActiveStatus(Long id) {
        Trainee trainee = entityManager.find(Trainee.class, id);
        if(trainee == null) {
            String message = "No trainee with such id";
            logger.error("toggleIsActiveStatus: {}", message);
            throw new IllegalArgumentException(message);
        }

        trainee.setActive(!trainee.isActive());
        boolean isActive = trainee.isActive();
        logger.info("Toggle isActive status of trainee with id {}. Current status: {}", id, isActive);
        return isActive;
    }
}
