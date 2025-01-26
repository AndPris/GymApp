package example.entities;

import lombok.Data;

import java.util.Date;

@Data
public class Training {
    private Trainee trainee;
    private Trainer trainer;
    private String trainingName;
    private TrainingType trainingType;
    private Date trainingDate;
    private float trainingDuration;
}
