package example.security.services;

import example.entities.Trainer;
import example.repositories.TrainerRepository;
import example.utils.input.InputHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainerAuthService {
    private final TrainerRepository trainerRepository;
    private final InputHandler inputHandler;

    public TrainerAuthService(TrainerRepository trainerRepository, InputHandler inputHandler) {
        this.trainerRepository = trainerRepository;
        this.inputHandler = inputHandler;
    }

    public Trainer authenticateTrainer() {
        String username = inputHandler.getLine("Trainer username: ", false);
        String password = inputHandler.getLine("Trainer password: ", false);

        Optional<Trainer> optionalTrainer = trainerRepository.findByUsernameAndPassword(username, password);
        if (optionalTrainer.isPresent()) {
            return optionalTrainer.get();
        } else {
            throw new RuntimeException("Trainer not found");
        }
    }
}
