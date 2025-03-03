package example.dtos.training;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrainingTrainerDTO extends TrainingBaseDTO {
    private String traineeFirstName;
    private String traineeLastName;
}
