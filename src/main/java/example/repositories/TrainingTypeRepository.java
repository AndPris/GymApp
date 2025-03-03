package example.repositories;

import example.entities.TrainingType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeRepository extends CrudRepository<TrainingType, Long> {
}
