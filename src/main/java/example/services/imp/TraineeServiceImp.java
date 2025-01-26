package example.services.imp;

import example.daos.TraineeDAO;
import example.entities.Trainee;
import example.services.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TraineeServiceImp implements TraineeService {
    @Autowired
    private TraineeDAO traineeDAO;

    @Override
    public Trainee createTrainee(Trainee trainee) {
        return null;
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        return null;
    }

    @Override
    public void deleteTraineeById(Long id) {

    }

    @Override
    public Iterable<Trainee> getAllTrainees() {
        return null;
    }

    @Override
    public Optional<Trainee> getTraineeById(Long id) {
        return Optional.empty();
    }
}
