package example.services.imp;

import example.entities.Training;
import example.repositories.TrainingRepository;
import example.services.TrainingService;
import example.validation.CustomValidator;
import org.springframework.stereotype.Service;

@Service
public class TrainingServiceImp implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final CustomValidator customValidator;

    public TrainingServiceImp(TrainingRepository trainingRepository, CustomValidator customValidator) {
        this.trainingRepository = trainingRepository;
        this.customValidator = customValidator;
    }

    @Override
    public Training createTraining(Training training) {
        if (training == null) {
            throw new IllegalArgumentException("Cannot create a training: training is null");
        }

        customValidator.validate(training);

        return trainingRepository.save(training);
    }

    @Override
    public Iterable<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }
}
