package example.services.imp;

import example.entities.Trainee;
import example.services.TraineeService;

import java.util.Optional;

public class TraineeServiceImp implements TraineeService {
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
