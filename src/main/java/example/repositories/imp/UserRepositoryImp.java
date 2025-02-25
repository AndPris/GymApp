package example.repositories.imp;

import example.entities.User;
import example.repositories.UserRepository;
import static example.logs.TransactionLogger.getTransactionId;
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
    private static final Logger logger = LogManager.getLogger(UserRepositoryImp.class);

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
                logger.info("[Transaction ID: {}] Creating a new user: {}", getTransactionId(), user);
            } else {
                entityManager.merge(user);
                logger.info("[Transaction ID: {}] Updating a user {}", getTransactionId(), user);
            }

            transaction.commit();
            logger.info("[Transaction ID: {}] Operation successfully performed", getTransactionId());
            return user;
        } catch (Exception e) {
            transaction.rollback();
            logger.error("[Transaction ID: {}] {}", getTransactionId(), e.getMessage());
        }

        return null;
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        Query query = entityManager.createQuery("select u from User u where u.username=:username and u.password=:password");
        query.setParameter("username", username);
        query.setParameter("password", password);

        List<User> result = query.getResultList();
        logger.info("[Transaction ID: {}] Find user with username {} and password {}. Result: {}",
                getTransactionId(), username, password, result);
        return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
    }
}
