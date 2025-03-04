package example.actuator.health;

import example.entities.TrainingType;
import example.mappers.TrainingTypeMapper;
import example.repositories.TrainingTypeRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingTypeHealthIndicator implements HealthIndicator {
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingTypeMapper trainingTypeMapper;

    public TrainingTypeHealthIndicator(TrainingTypeRepository trainingTypeRepository, TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingTypeMapper = trainingTypeMapper;
    }

    @Override
    public Health health() {
        try {
            List<TrainingType> trainingTypes = trainingTypeRepository.findAll();

            if (trainingTypes.isEmpty()) {
                return Health.down()
                        .withDetail("Training Types", "None found")
                        .build();
            }

            return Health.up()
                    .withDetail("Training Types Count", trainingTypes.size())
                    .withDetail("Training Types", trainingTypes.stream()
                            .map(trainingTypeMapper::trainingTypeToTrainingTypeDTO)
                            .toList()
                    )
                    .build();

        } catch (Exception e) {
            return Health.down()
                    .withDetail("Training Types", "Error accessing database")
                    .withDetail("Error", e.getMessage())
                    .build();
        }
    }
}
