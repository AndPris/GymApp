package example.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "traineeId")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainerId")
    private Trainer trainer;

    @Column(nullable = false)
    @Size(min = 2, max = 20, message = "Training name must be from 2 to 20 characters")
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "trainingTypeId")
    private TrainingType trainingType;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date trainingDate;

    @Column(nullable = false)
    @Min(value = 20, message = "Minimal training duration is 20 minutes")
    @Max(value = 180, message = "Maximal training duration is 180 minutes")
    private Integer trainingDuration;

    public Training(Trainee trainee, Trainer trainer, String trainingName, TrainingType trainingType, Date trainingDate, Integer trainingDuration) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Trainee Id: " + trainee.getId() + "\n" +
                "Trainer Id: " + trainer.getId() + "\n" +
                "Training name: " + trainingName + "\n" +
                "Training type: " + trainingType + "\n" +
                "Training date: " + trainingDate + "\n" +
                "Training duration: " + trainingDuration + "\n";
    }
}
