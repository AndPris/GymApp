package example.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
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
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "trainingTypeId")
    private TrainingType trainingType;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date trainingDate;

    @Column(nullable = false)
    private Integer trainingDuration;

    public Training(Trainee trainee, Trainer trainer, String trainingName, TrainingType trainingType, Date trainingDate, Integer trainingDuration) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public Training() {
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
