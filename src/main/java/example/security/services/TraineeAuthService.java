package example.security.services;

import example.entities.Trainee;
import example.repositories.TraineeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TraineeAuthService {
    private final TraineeRepository traineeRepository;

    public TraineeAuthService(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    public boolean traineeExistsByUsernameAndPassword(String username, String password) {
        Optional<Trainee> optionalTrainee = traineeRepository.findByUsernameAndPassword(username, password);
        return optionalTrainee.isPresent();
    }
}
