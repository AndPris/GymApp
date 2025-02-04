package example.services.imp;

import example.daos.TrainingDAO;
import example.entities.Training;
import example.services.TrainingService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Setter
@Service
public class TrainingServiceImp implements TrainingService {
    @Autowired
    private TrainingDAO trainingDAO;

    @Override
    public Training createTraining(Training training) {
        return trainingDAO.save(training);
    }

    @Override
    public Iterable<Training> getAllTrainings() {
        return trainingDAO.findAll();
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
