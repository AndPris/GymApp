package example.services;

import example.entities.TrainingType;

import java.util.List;

public interface TrainingTypeService {
    List<TrainingType> findAll();

    TrainingType findById(Long id);
}
