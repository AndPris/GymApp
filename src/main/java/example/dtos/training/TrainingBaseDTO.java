package example.dtos.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public abstract class TrainingBaseDTO {
    private String trainingName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd.MM.yyyy")
    private Date trainingDate;
    private Long trainingType;
    private Integer trainingDuration;
}
