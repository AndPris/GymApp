package example.dtos.training;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class TrainingCreateDTO {
    @NotNull(message = "Trainee must be provided")
    private String traineeUsername;

    @NotNull(message = "Trainer must be provided")
    private String trainingName;

    @NotNull(message = "Training type must be provided")
    private Long trainingType;

    @NotNull(message = "Training date be provided")
    private Date trainingDate;

    @NotNull(message = "Training duration must be provided")
    private Integer trainingDuration;
}
