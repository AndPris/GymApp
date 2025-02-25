package example.services.imp;

import example.dtos.trainer.TrainerUpdateDTO;
import example.entities.Trainer;
import example.entities.Training;
import example.entities.TrainingType;
import example.exceptions.TrainerNotFoundException;
import example.repositories.TrainerRepository;
import example.services.TrainerService;
import example.services.TrainingTypeService;
import example.utils.password.PasswordGenerator;
import example.utils.username.UsernameGenerator;
import example.validation.CustomValidator;
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

    private TrainingTypeService trainingTypeService;

    private PasswordGenerator passwordGenerator;
    private UsernameGenerator usernameGenerator;

    private CustomValidator customValidator;

    @Autowired
    public void setTrainingTypeService(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @Autowired
    public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    @Autowired
    public void setUsernameGenerator(UsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }

    @Autowired
    public void setCustomValidator(CustomValidator customValidator) {
        this.customValidator = customValidator;
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        if (trainer == null) {
            throw new IllegalArgumentException("Cannot create a trainer: trainer is null");
        }

        customValidator.validate(trainer);

        trainer.setPassword(passwordGenerator.generatePassword());
        trainer.setUsername(usernameGenerator.generateUsername(trainer));
        return trainerRepository.save(trainer);
    }

    @Override
    public Trainer updateTrainer(String username, TrainerUpdateDTO trainerUpdateDTO) {
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new TrainerNotFoundException("There's no trainer with such username: " + username));

        trainer.setFirstName(trainerUpdateDTO.getFirstName());
        trainer.setLastName(trainerUpdateDTO.getLastName());
        trainer.setActive(trainerUpdateDTO.isActive());
        TrainingType trainingType = trainingTypeService.findById(trainerUpdateDTO.getSpecialization());
        trainer.setSpecialization(trainingType);

        customValidator.validate(trainer);

        return trainerRepository.save(trainer);
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
    public boolean toggleTrainerIsActiveStatus(String username) {
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new TrainerNotFoundException("No trainer with such username: " + username));

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
