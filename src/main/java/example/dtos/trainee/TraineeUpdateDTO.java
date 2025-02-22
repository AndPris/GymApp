package example.dtos.trainee;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TraineeUpdateDTO extends TraineeCreateDTO {
    private Boolean active;

    public Boolean isActive() {
        return active;
    }
}
