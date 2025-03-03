package example.repositories.imp;

import example.entities.TrainingType;
import example.repositories.TrainingTypeRepository;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static example.logs.TransactionLogger.getTransactionId;

@Repository
public class TrainingTypeRepositoryImp implements TrainingTypeRepository {
    private static final Logger logger = LogManager.getLogger(TrainingTypeRepositoryImp.class);

    private EntityManager entityManager;

    public TrainingTypeRepositoryImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<TrainingType> findAll() {
        logger.info("[Transaction ID: {}] Find all training types", getTransactionId());
        return entityManager.createQuery("select t from TrainingType t")
                .getResultList();
    }

    @Override
    public Optional<TrainingType> findById(Long id) {
        TrainingType trainingType = entityManager.find(TrainingType.class, id);
        logger.info("[Transaction ID: {}] Find training type by id {}. Result: {}", getTransactionId(), id, trainingType);
        return Optional.ofNullable(trainingType);
    }
}
