package example.services.imp;

import example.entities.Trainer;
import example.entities.Training;
import example.repositories.TrainerRepository;
import example.services.TrainerService;
import example.utils.password.PasswordGenerator;
import example.utils.username.UsernameGenerator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImp implements TrainerService {
    @Autowired
    @Setter
    private TrainerRepository trainerRepository;

    private PasswordGenerator passwordGenerator;
    private UsernameGenerator usernameGenerator;
    private Validator validator;

    public TrainerServiceImp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

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
        if (trainer == null) {
            throw new IllegalArgumentException("Cannot create a trainer: trainer is null");
        }

        validateTrainer(trainer);

        trainer.setPassword(passwordGenerator.generatePassword());
        trainer.setUsername(usernameGenerator.generateUsername(trainer));
        return trainerRepository.save(trainer);
    }

    private void validateTrainer(Trainer trainer) {
        for (ConstraintViolation<Trainer> violation : validator.validate(trainer)) {
            throw new ValidationException("Validation error: " + violation.getMessage());
        }
    }

    @Override
    public Trainer updateTrainer(Trainer updates) {
        validateTrainerForUpdate(updates);

        Trainer existing = trainerRepository.findById(updates.getId()).get();
        updateTrainerFields(existing, updates);

        return trainerRepository.save(existing);
    }

    private void validateTrainerForUpdate(Trainer trainer) {
        if (trainer == null) {
            throw new IllegalArgumentException("Cannot update a trainer: trainer is null");
        }

        Long trainerId = trainer.getId();
        if (trainerId == null) {
            throw new IllegalArgumentException("Cannot update a trainer: id is null");
        }

        if (!trainerRepository.findById(trainerId).isPresent()) {
            throw new IllegalArgumentException("Cannot update a trainer: there is no trainer with id " + trainerId);
        }

        validateTrainer(trainer);
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
    public boolean existsTrainerById(Long id) {
        Optional<Trainer> trainer = trainerRepository.findById(id);
        return trainer.isPresent();
    }

    @Override
    public boolean existsTrainerByUsername(String username) {
        Optional<Trainer> trainer = trainerRepository.findByUsername(username);
        return trainer.isPresent();
    }

    @Override
    public void changeTrainerPassword(String username, String oldPassword, String newPassword) {
        Optional<Trainer> optionalTrainer = trainerRepository.findByUsernameAndPassword(username, oldPassword);
        if (!optionalTrainer.isPresent()) {
            throw new IllegalArgumentException("There's no trainer with such username and password");
        }

        Trainer trainer = optionalTrainer.get();
        trainer.setPassword(newPassword);
        validateTrainer(trainer);
        trainerRepository.save(trainer);
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

    @Override
    public List<Training> findTrainerTrainingList(String username, Date fromDate, Date toDate,
                                                  String traineeFirstName, String traineeLastName) {
        return trainerRepository.findTrainingList(username, fromDate, toDate, traineeFirstName, traineeLastName);
    }
}
