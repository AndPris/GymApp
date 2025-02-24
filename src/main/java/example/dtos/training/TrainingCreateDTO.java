package example.dtos.training;

import lombok.Data;

import java.util.Date;

@Data
public class TrainingCreateDTO {
    private String traineeUsername;
    private String trainingName;
    private Long trainingType;
    private Date trainingDate;
    private Integer trainingDuration;
}
