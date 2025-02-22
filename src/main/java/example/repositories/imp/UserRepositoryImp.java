package example.repositories.imp;

import example.entities.User;
import example.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImp implements UserRepository {
    private static final Logger logger = LogManager.getLogger(TraineeRepositoryImp.class);

    private final EntityManager entityManager;

    public UserRepositoryImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public User save(User user) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            if (user.getId() == null) {
                entityManager.persist(user);
                logger.info("Creating a new user: {}", user);
            } else {
                entityManager.merge(user);
                logger.info("Updating a user {}", user);
            }

            transaction.commit();
            logger.info("Operation successfully performed");
            return user;
        } catch (Exception e) {
            transaction.rollback();
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        Query query = entityManager.createQuery("select u from User u where u.username=:username and u.password=:password");
        query.setParameter("username", username);
        query.setParameter("password", password);

        List<User> result = query.getResultList();
        logger.info("Find user with username {} and password {}. Result: {}", username, password, result);
        return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
    }
}
