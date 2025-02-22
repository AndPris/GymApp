package example.dtos.trainer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrainerUpdateDTO extends TrainerCreateDTO {
    private Boolean active;

    public Boolean isActive() {
        return active;
    }
}
