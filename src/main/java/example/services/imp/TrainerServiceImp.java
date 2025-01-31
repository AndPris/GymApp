package example.services.imp;

import example.daos.TrainerDAO;
import example.entities.Trainer;
import example.services.TrainerService;
import example.utils.password.PasswordGenerator;
import example.utils.username.UsernameGenerator;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainerServiceImp implements TrainerService {
    @Autowired
    @Setter
    private TrainerDAO trainerDAO;
    private PasswordGenerator passwordGenerator;
    private UsernameGenerator usernameGenerator;

    @Autowired
    public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    @Autowired
    public void setUsernameGenerator(UsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }


    @Override
    public Trainer createTrainer(Trainer trainer) {
        if(trainer == null)
            throw new IllegalArgumentException("Can not perform createTrainer: trainer is null");

        trainer.setPassword(passwordGenerator.generatePassword());
        trainer.setUsername(usernameGenerator.generateUsername(trainer));
        return trainerDAO.save(trainer);
    }

    @Override
    public Trainer updateTrainer(Long id, Trainer trainer) {
        return trainerDAO.update(id, trainer);
    }

    @Override
    public Iterable<Trainer> getAllTrainers() {
        return trainerDAO.findAll();
    }

    @Override
    public Optional<Trainer> getTrainerById(Long id) {
        return trainerDAO.findById(id);
    }

    @Override
    public boolean existsTrainer(Long id) {
        return trainerDAO.existsById(id);
    }
}
