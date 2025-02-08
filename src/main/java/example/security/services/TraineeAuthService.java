package example.security.services;

import example.entities.Trainee;
import example.repositories.TraineeRepository;
import example.utils.input.InputHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TraineeAuthService {
    private final TraineeRepository traineeRepository;
    private final InputHandler inputHandler;

    public TraineeAuthService(TraineeRepository traineeRepository, InputHandler inputHandler) {
        this.traineeRepository = traineeRepository;
        this.inputHandler = inputHandler;
    }

    public Trainee authenticateTrainee() {
        String username = inputHandler.getLine("Trainee username: ", false);
        String password = inputHandler.getLine("Trainee password: ", false);

        Optional<Trainee> optionalTrainee = traineeRepository.findByUsernameAndPassword(username, password);
        if (optionalTrainee.isPresent()) {
            return optionalTrainee.get();
        } else {
            throw new RuntimeException("Trainee not found");
        }
    }
}
