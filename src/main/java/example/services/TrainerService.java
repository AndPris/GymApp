package example.services;

import example.dtos.trainer.TrainerUpdateDTO;
import example.entities.Trainer;
import example.entities.Training;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Trainer createTrainer(Trainer trainer);

    Trainer updateTrainer(String username, TrainerUpdateDTO trainerUpdateDTO);

    Trainer patchTrainer(Trainer updates);

    Iterable<Trainer> getAllTrainers();

    Optional<Trainer> getTrainerById(Long id);

    Optional<Trainer> getTrainerByUsername(String username);

    boolean existsTrainerById(Long id);

    boolean existsTrainerByUsername(String username);

    boolean toggleTrainerIsActiveStatus(Long id);

    List<Training> findTrainerTrainingList(String username, Date fromDate, Date toDate,
                                           String traineeFirstName, String traineeLastName);
}
