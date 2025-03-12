package example.repositories;

import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Optional<Trainee> findByUsername(String username);

    Integer deleteByUsername(String username);

    @Query("select t from Training t " +
            "where t.trainee.username=:username " +
            "and (:fromDate is null or t.trainingDate > :fromDate) " +
            "and (:toDate is null or t.trainingDate < :toDate) " +
            "and (:trainerFirstName is null or t.trainer.firstName=:trainerFirstName) " +
            "and (:trainerLastName is null or t.trainer.lastName=:trainerLastName) " +
            "and (:trainingType is null or t.trainingType.id=:trainingType)")
    List<Training> findTrainingList(String username, Date fromDate, Date toDate, String trainerFirstName,
                                    String trainerLastName, Long trainingType);

    @Query("select tr from Trainer tr " +
            "where tr not in (select t.trainers from Trainee t where t.username=:username) " +
            "and tr.active=:active")
    List<Trainer> findTrainersNotAssignedToTrainee(String username, Boolean active);
}
