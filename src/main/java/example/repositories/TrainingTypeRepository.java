package example.repositories;

import example.entities.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeRepository {
    List<TrainingType> findAll();

    Optional<TrainingType> findById(Long id);
}
