package example.repositories.imp;

import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;
import example.repositories.TraineeRepository;
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
public class TraineeRepositoryImp implements TraineeRepository {
    private static final Logger logger = LogManager.getLogger(TraineeRepositoryImp.class);

    private final EntityManager entityManager;

    public TraineeRepositoryImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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
    public Optional<Trainee> findById(Long id) {
        Trainee trainee = entityManager.find(Trainee.class, id);
        logger.info("Find trainer by id: {}. Result: {}", id, trainee);
        return Optional.ofNullable(trainee);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        Query query = entityManager.createQuery("select t from Trainee t where t.username=:username");
        query.setParameter("username", username);

        List<Trainee> result = query.getResultList();
        logger.info("Find trainee with username {}. Result: {}", username, result);
        return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
    }

    @Override
    public Optional<Trainee> findByUsernameAndPassword(String username, String password) {
        Query query = entityManager.createQuery("select t from Trainee t where t.username=:username and t.password=:password");
        query.setParameter("username", username);
        query.setParameter("password", password);

        List<Trainee> result = query.getResultList();
        logger.info("Find trainee with username {} and password {}. Result: {}", username, password, result);
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
            logger.info("Deleting a trainee with username {}", username);
            int result = entityManager.createQuery("delete from Trainee t where t.username=:username")
                    .setParameter("username", username)
                    .executeUpdate();

            transaction.commit();
            logger.info("Result: {}", result);
            return result != 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Training> findTrainingList(String username, Date fromDate, Date toDate, String trainerFirstName,
                                           String trainerLastName, String trainingType) {

        Query query = entityManager.createQuery("select t from Training t " +
                "where t.trainee.username=:username " +
                "and (:fromDate is null or t.trainingDate > :fromDate) " +
                "and (:toDate is null or t.trainingDate < :toDate) " +
                "and (:trainerFirstName is null or t.trainer.firstName=:trainerFirstName) " +
                "and (:trainerLastName is null or t.trainer.lastName=:trainerLastName) " +
                "and (:trainingType is null or t.trainingType.name=:trainingType)");

        query.setParameter("username", username);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        query.setParameter("trainerFirstName", trainerFirstName);
        query.setParameter("trainerLastName", trainerLastName);
        query.setParameter("trainingType", trainingType);

        List<Training> result = query.getResultList();
        logger.info("Find training list of trainee with username {}, from date {}, to date {}," +
                        " trainer first name {}, trainer last name {}, training type {}. Result: {}",
                username, fromDate, toDate, trainerFirstName, trainerLastName, trainingType, result);
        return result;
    }

    @Override
    public List<Trainer> findTrainersNotAssignedToTrainee(String username) {
        Query query = entityManager.createQuery("select tr from Trainer tr " +
                "where tr not in (select t.trainers from Trainee t where t.username=:username)");
        query.setParameter("username", username);

        List<Trainer> result = query.getResultList();
        logger.info("Find trainers not assigned to trainee with username {}. Result: {}", username, result);
        return result;
    }
}
