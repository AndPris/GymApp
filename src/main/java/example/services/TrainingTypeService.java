package example.services;

import example.entities.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeService {
    List<TrainingType> findAll();

    Optional<TrainingType> findById(Long id);
}
