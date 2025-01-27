package example.services.imp;

import example.daos.TraineeDAO;
import example.entities.Trainee;
import example.services.TraineeService;
import example.utils.password.PasswordGenerator;
import example.utils.username.UsernameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TraineeServiceImp implements TraineeService {
    @Autowired
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
        trainee.setPassword(passwordGenerator.generatePassword());
        return null;
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        return null;
    }

    @Override
    public void deleteTraineeById(Long id) {

    }

    @Override
    public Iterable<Trainee> getAllTrainees() {
        return null;
    }

    @Override
    public Optional<Trainee> getTraineeById(Long id) {
        return Optional.empty();
    }
}
