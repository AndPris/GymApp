package example.services;

import example.dtos.trainee.TraineeTrainerListUpdateDTO;
import example.dtos.trainee.TraineeUpdateDTO;
import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TraineeService {
    Trainee createTrainee(Trainee trainee);

    Trainee updateTrainee(String username, TraineeUpdateDTO traineeUpdateDTO);

    boolean deleteTraineeByUsername(String username);

    Iterable<Trainee> getAllTrainees();

    Optional<Trainee> getTraineeById(Long id);

    Optional<Trainee> getTraineeByUsername(String username);

    boolean existsTraineeById(Long id);

    boolean toggleTraineeIsActiveStatus(String username);

    List<Training> findTraineeTrainingList(String username, Date fromDate, Date toDate, String trainerFirstName,
                                           String trainerLastName, Long trainingType);

    List<Trainer> updateTraineeTrainerList(String username, TraineeTrainerListUpdateDTO traineeTrainerListUpdateDTO);

    void addTrainerToList(Trainee trainee, Trainer trainer);

    void removeTrainerFromList(Trainee trainee, Trainer trainer);

    void clearTraineeTrainerList(Trainee trainee);

    List<Trainer> findTraineeTrainers(String username, Boolean assigned, Boolean active);
}
