package example.repositories.imp;

import example.entities.Trainee;
import example.repositories.TraineeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TraineeRepositoryImp implements TraineeRepository {
    private final EntityManager entityManager;

    public TraineeRepositoryImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Trainee save(Trainee trainee) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            if(trainee.getId() == null) {
                entityManager.persist(trainee);
            } else {
                entityManager.merge(trainee);
            }

            transaction.commit();
            return trainee;
        } catch (Exception e) {
            transaction.rollback();
        }

        return null;
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void deleteByUsername(String username) {

    }
}
