package example.entities;

import lombok.Data;

import java.util.Date;

@Data
public class Training {
    private Long id;
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private Date trainingDate;
    private float trainingDuration;

    public Training(Long traineeId, Long trainerId, String trainingName, TrainingType trainingType, Date trainingDate, float trainingDuration) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
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
                "Trainee Id: " + traineeId + "\n" +
                "Trainer Id: " + trainerId + "\n" +
                "Training name: " + trainingName + "\n" +
                "Training type: " + trainingType + "\n" +
                "Training date: " + trainingDate + "\n" +
                "Training duration: " + trainingDuration + "\n";
    }
}
