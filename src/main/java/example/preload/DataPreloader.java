package example.preload;

import example.entities.TrainingType;
import example.repositories.TrainingTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataPreloader {

    @Bean
    public CommandLineRunner preloadTrainingTypes(TrainingTypeRepository trainingTypeRepository) {
        return args -> {
            trainingTypeRepository.save(new TrainingType(1L,"Fitness"));
            trainingTypeRepository.save(new TrainingType(2L,"Pilates"));
            trainingTypeRepository.save(new TrainingType(3L,"Athletics"));
        };
    }
}
