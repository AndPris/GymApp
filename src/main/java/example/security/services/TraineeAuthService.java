package example.security.services;

import example.entities.Trainee;
import example.exceptions.TraineeNotFoundException;
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
        return optionalTrainee.orElseThrow(() -> new TraineeNotFoundException("Trainee not found"));
    }
}
