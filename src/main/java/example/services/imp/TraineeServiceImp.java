package example.services.imp;

import example.dtos.trainee.TraineeUpdateDTO;
import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;
import example.exceptions.TraineeNotFoundException;
import example.repositories.TraineeRepository;
import example.validation.Validator;
import example.services.TraineeService;
import example.utils.password.PasswordGenerator;
import example.utils.username.UsernameGenerator;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImp implements TraineeService {
    @Autowired
    @Setter
    private TraineeRepository traineeRepository;

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
    public Trainee createTrainee(Trainee trainee) {
        if (trainee == null) {
            throw new IllegalArgumentException("Cannot create a trainee: trainee is null");
        }

        Validator.validate(trainee);

        trainee.setPassword(passwordGenerator.generatePassword());
        trainee.setUsername(usernameGenerator.generateUsername(trainee));
        return traineeRepository.save(trainee);
    }

    @Override
    public Trainee updateTrainee(String username, TraineeUpdateDTO traineeUpdateDTO) {
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new TraineeNotFoundException("There's no trainee with such username: " + username));

        trainee.setFirstName(traineeUpdateDTO.getFirstName());
        trainee.setLastName(traineeUpdateDTO.getLastName());
        trainee.setDateOfBirth(traineeUpdateDTO.getDateOfBirth());
        trainee.setAddress(traineeUpdateDTO.getAddress());
        trainee.setActive(traineeUpdateDTO.isActive());

        Validator.validate(trainee);

        return traineeRepository.save(trainee);
    }

    @Override
    public Trainee patchTrainee(Trainee patch) {
        validateTraineeForPatch(patch);

        Trainee existing = traineeRepository.findById(patch.getId()).get();
        updateTraineeFields(existing, patch);

        return traineeRepository.save(existing);
    }

    private void validateTraineeForPatch(Trainee trainee) {
        if (trainee == null || trainee.getId() == null || !traineeRepository.findById(trainee.getId()).isPresent()) {
            throw new IllegalArgumentException("Cannot update a trainee: invalid data");
        }

        Validator.validate(trainee);
    }

    private void updateTraineeFields(Trainee existing, Trainee updates) {
        Optional.ofNullable(updates.getFirstName()).filter(StringUtils::isNoneBlank).ifPresent(existing::setFirstName);
        Optional.ofNullable(updates.getLastName()).filter(StringUtils::isNoneBlank).ifPresent(existing::setLastName);
        Optional.ofNullable(updates.getUsername()).filter(StringUtils::isNoneBlank).ifPresent(existing::setUsername);
        Optional.ofNullable(updates.getPassword()).filter(StringUtils::isNoneBlank).ifPresent(existing::setPassword);
        Optional.ofNullable(updates.getAddress()).filter(StringUtils::isNoneBlank).ifPresent(existing::setAddress);
        Optional.ofNullable(updates.getDateOfBirth()).ifPresent(existing::setDateOfBirth);
        Optional.ofNullable(updates.isActive()).ifPresent(existing::setActive);
    }

    @Override
    public boolean deleteTraineeByUsername(String username) {
        return traineeRepository.deleteByUsername(username);
    }

    @Override
    public Iterable<Trainee> getAllTrainees() {
        return traineeRepository.findAll();
    }

    @Override
    public Optional<Trainee> getTraineeById(Long id) {
        return traineeRepository.findById(id);
    }

    @Override
    public Optional<Trainee> getTraineeByUsername(String username) {
        return traineeRepository.findByUsername(username);
    }

    @Override
    public boolean existsTraineeById(Long id) {
        Optional<Trainee> trainee = traineeRepository.findById(id);
        return trainee.isPresent();
    }

    @Override
    public boolean toggleTraineeIsActiveStatus(Long id) {
        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new TraineeNotFoundException("No trainee with such id: " + id));

        trainee.setActive(!trainee.isActive());
        traineeRepository.save(trainee);
        return trainee.isActive();
    }

    @Override
    public List<Training> findTraineeTrainingList(String username, Date fromDate, Date toDate, String trainerFirstName,
                                                  String trainerLastName, String trainingType) {

        return traineeRepository.findTrainingList(username, fromDate, toDate,
                trainerFirstName, trainerLastName, trainingType);
    }

    @Override
    public void addTrainerToList(Trainee trainee, Trainer trainer) {
        trainee.addTrainer(trainer);
        traineeRepository.save(trainee);
    }

    @Override
    public void removeTrainerFromList(Trainee trainee, Trainer trainer) {
        trainee.removeTrainer(trainer);
        traineeRepository.save(trainee);
    }

    @Override
    public void clearTraineeTrainerList(Trainee trainee) {
        trainee.clearTrainers();
        traineeRepository.save(trainee);
    }

    @Override
    public List<Trainer> findTrainersNotAssignedToTrainee(String username) {
        return traineeRepository.findTrainersNotAssignedToTrainee(username);
    }
}
