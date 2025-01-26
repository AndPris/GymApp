package example.services.imp;

import example.daos.TrainerDAO;
import example.entities.Trainer;
import example.services.TrainerService;
import example.utils.password.imp.SimplePasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainerServiceImp implements TrainerService {
    @Autowired
    private TrainerDAO trainerDAO;
    private SimplePasswordGenerator simplePasswordGenerator;

    @Autowired
    public void setSimplePasswordGenerator(SimplePasswordGenerator simplePasswordGenerator) {
        this.simplePasswordGenerator = simplePasswordGenerator;
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        return null;
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        return null;
    }

    @Override
    public Iterable<Trainer> getAllTrainers() {
        return null;
    }

    @Override
    public Optional<Trainer> getTrainerById(Long id) {
        return Optional.empty();
    }
}
