package example.dtos.training;

import lombok.Data;

import java.util.Date;

@Data
public class TrainingDTO {
    private String trainingName;
    private Date trainingDate;
    private Long trainingType;
    private Integer trainingDuration;
    private String trainerFirstName;
    private String trainerLastName;
}
