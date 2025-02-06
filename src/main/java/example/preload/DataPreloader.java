package example.preload;

import example.entities.TrainingType;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class DataPreloader {
    private final EntityManager entityManager;

    public DataPreloader(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    public void preloadTrainingTypes() {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            if (entityManager.createQuery("select count(t) from TrainingType t", Long.class)
                    .getSingleResult() == 0) {

                String[] trainingTypes = {"Fitness", "Pilates", "Athletics"};

                for (String trainingType : trainingTypes) {
                    entityManager.persist(new TrainingType(trainingType));
                }
            }
            transaction.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            transaction.rollback();
        }
    }
}
