package example.services.imp;

import example.entities.Training;
import example.repositories.TrainingRepository;
import example.services.TrainingService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingServiceImp implements TrainingService {
    private final TrainingRepository trainingRepository;
    private Validator validator;

    public TrainingServiceImp(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public Training createTraining(Training training) {
        if (training == null) {
            throw new IllegalArgumentException("Cannot create a training: training is null");
        }

        validateTraining(training);

        return trainingRepository.save(training);
    }

    private void validateTraining(Training training) {
        for (ConstraintViolation<Training> violation : validator.validate(training)) {
            throw new ValidationException("Validation error: " + violation.getMessage());
        }
    }

    @Override
    public Iterable<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }
}
