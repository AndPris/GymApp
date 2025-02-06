package example.services.imp;

import example.daos.TrainingDAO;
import example.entities.Training;
import example.repositories.TrainingRepository;
import example.services.TrainingService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Setter
@Service
public class TrainingServiceImp implements TrainingService {
    private final TrainingRepository trainingRepository;

    @Autowired
    private TrainingDAO trainingDAO;

    public TrainingServiceImp(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Training createTraining(Training training) {
        if (training == null)
            throw new IllegalArgumentException("Cannot perform createTraining: training is null");

        return trainingRepository.save(training);
    }

    @Override
    public Iterable<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public Optional<Training> getTrainingById(Long id) {
        return trainingDAO.findById(id);
    }

    @Override
    public boolean existsTraining(Long id) {
        return trainingDAO.existsById(id);
    }
}
