package example.dtos.training;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrainingTraineeDTO extends TrainingBaseDTO {
    private String trainerFirstName;
    private String trainerLastName;
}
