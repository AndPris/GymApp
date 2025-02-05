package example.services.imp;

import example.daos.TraineeDAO;
import example.entities.Trainee;
import example.repositories.TraineeRepository;
import example.services.TraineeService;
import example.utils.password.PasswordGenerator;
import example.utils.username.UsernameGenerator;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TraineeServiceImp implements TraineeService {
    @Autowired
    @Setter
    private TraineeRepository traineeRepository;

    @Autowired
    @Setter
    private TraineeDAO traineeDAO;
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
        if (trainee == null)
            throw new IllegalArgumentException("Cannot perform createTrainee: trainee is null");

        trainee.setPassword(passwordGenerator.generatePassword());
        trainee.setUsername(usernameGenerator.generateUsername(trainee));
        return traineeRepository.save(trainee);
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        return traineeRepository.save(trainee);
    }

    @Override
    public boolean deleteTraineeById(Long id) {
        return traineeDAO.deleteById(id);
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
        return traineeDAO.findById(id);
    }

    @Override
    public Optional<Trainee> getTraineeByUsername(String username) {
        return traineeRepository.findByUsername(username);
    }

    @Override
    public boolean existsTrainee(Long id) {
        return traineeDAO.existsById(id);
    }
}
