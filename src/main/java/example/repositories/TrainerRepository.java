package example.repositories;

import example.entities.Trainer;
import example.entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUsername(String username);

    Optional<Trainer> findByUsernameAndPassword(String username, String password);

    @Query("select t from Training t " +
                  "where t.trainer.username=:username " +
                  "and (:fromDate is null or t.trainingDate > :fromDate) " +
                  "and (:toDate is null or t.trainingDate < :toDate) " +
                  "and (:traineeFirstName is null or t.trainee.firstName=:traineeFirstName) " +
                  "and (:traineeLastName is null or t.trainee.lastName=:traineeLastName)")
    List<Training> findTrainingList(String username, Date fromDate, Date toDate,
                                    String traineeFirstName, String traineeLastName);
}
