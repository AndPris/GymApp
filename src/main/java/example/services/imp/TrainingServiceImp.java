package example.services.imp;

import example.entities.Training;
import example.repositories.TrainingRepository;
import example.services.TrainingService;
import example.validation.Validator;
import org.springframework.stereotype.Service;

@Service
public class TrainingServiceImp implements TrainingService {
    private final TrainingRepository trainingRepository;

    public TrainingServiceImp(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Training createTraining(Training training) {
        if (training == null) {
            throw new IllegalArgumentException("Cannot create a training: training is null");
        }

        Validator.validate(training);

        return trainingRepository.save(training);
    }

    @Override
    public Iterable<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }
}
