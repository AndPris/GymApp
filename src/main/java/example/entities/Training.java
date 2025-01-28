package example.entities;

import lombok.Data;

import java.util.Date;

@Data
public class Training {
    private Long id;
    private Trainee trainee;
    private Trainer trainer;
    private String trainingName;
    private TrainingType trainingType;
    private Date trainingDate;
    private float trainingDuration;

    public Training(Trainee trainee, Trainer trainer, String trainingName, TrainingType trainingType, Date trainingDate, float trainingDuration) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public Training() {}

    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Trainee:\n\t " + trainee + "\n" +
                "Trainer:\n\t " + trainer + "\n" +
                "Training name: " + trainingName + "\n" +
                "Training type: " + trainingType + "\n" +
                "Training date: " + trainingDate + "\n" +
                "Training duration: " + trainingDuration + "\n";
    }
}
