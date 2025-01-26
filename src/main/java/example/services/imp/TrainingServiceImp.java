package example.services.imp;

import example.daos.TrainingDAO;
import example.entities.Training;
import example.services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainingServiceImp implements TrainingService {
    @Autowired
    private TrainingDAO trainingDAO;

    @Override
    public Training createTraining(Training training) {
        return null;
    }

    @Override
    public Iterable<Training> getAllTrainings() {
        return null;
    }

    @Override
    public Optional<Training> getTrainingById(Long id) {
        return Optional.empty();
    }
}
