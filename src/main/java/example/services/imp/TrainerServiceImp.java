package example.services.imp;

import example.daos.TrainerDAO;
import example.entities.Trainer;
import example.entities.Trainer;
import example.repositories.TrainerRepository;
import example.services.TrainerService;
import example.utils.password.PasswordGenerator;
import example.utils.username.UsernameGenerator;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainerServiceImp implements TrainerService {
    @Autowired
    @Setter
    private TrainerRepository trainerRepository;

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
        if (trainer == null)
            throw new IllegalArgumentException("Cannot perform createTrainer: trainer is null");

        trainer.setPassword(passwordGenerator.generatePassword());
        trainer.setUsername(usernameGenerator.generateUsername(trainer));
        return trainerRepository.save(trainer);
    }

    @Override
    public Trainer updateTrainer(Trainer updates) {
        validateTrainerForUpdate(updates);

        Trainer existing = trainerRepository.findById(updates.getId()).get();
        updateTrainerFields(existing, updates);

        return trainerRepository.save(existing);
    }

    private void validateTrainerForUpdate(Trainer trainer) {
        Long trainerId = trainer.getId();
        if(trainerId == null) {
            throw new IllegalArgumentException("Cannot update a trainer: id is null");
        }

        if(!trainerRepository.findById(trainerId).isPresent()) {
            throw new IllegalArgumentException("Cannot update a trainer: there is no trainer with id " + trainerId);
        }
    }

    private void updateTrainerFields(Trainer existing, Trainer updates) {
        Optional.ofNullable(updates.getFirstName()).filter(StringUtils::isNoneBlank).ifPresent(existing::setFirstName);
        Optional.ofNullable(updates.getLastName()).filter(StringUtils::isNoneBlank).ifPresent(existing::setLastName);
        Optional.ofNullable(updates.getUsername()).filter(StringUtils::isNoneBlank).ifPresent(existing::setUsername);
        Optional.ofNullable(updates.getPassword()).filter(StringUtils::isNoneBlank).ifPresent(existing::setPassword);
        Optional.ofNullable(updates.getSpecialization()).ifPresent(existing::setSpecialization);
        Optional.ofNullable(updates.isActive()).ifPresent(existing::setActive);
    }

    @Override
    public Iterable<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    @Override
    public Optional<Trainer> getTrainerById(Long id) {
        return trainerRepository.findById(id);
    }

    @Override
    public Optional<Trainer> getTrainerByUsername(String username) {
        return trainerRepository.findByUsername(username);
    }

    @Override
    public boolean existsTrainer(Long id) {
        Optional<Trainer> trainer = trainerRepository.findById(id);
        return trainer.isPresent();
    }

    @Override
    public void changeTrainerPassword(String username, String oldPassword, String newPassword) {
        Trainer trainer = authenticateTrainer(username, oldPassword);
        trainer.setPassword(newPassword);
        trainerRepository.save(trainer);
    }

    @Override
    public Trainer authenticateTrainer(String username, String password) {
        Optional<Trainer> optionalTrainer = trainerRepository.findByUsernameAndPassword(username, password);
        if(optionalTrainer.isPresent()) {
            return optionalTrainer.get();
        } else {
            throw new RuntimeException("Trainer not found");
        }
    }

    @Override
    public boolean toggleTrainerIsActiveStatus(Long id) {
        Optional<Trainer> optionalTrainer = trainerRepository.findById(id);
        if (!optionalTrainer.isPresent()) {
            throw new IllegalArgumentException("No trainer with such id: " + id);
        }

        Trainer trainer = optionalTrainer.get();
        trainer.setActive(!trainer.isActive());
        trainerRepository.save(trainer);
        return trainer.isActive();
    }
}
