package example.dtos.trainee;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TraineeUpdateDTO extends TraineeCreateDTO {
    @NotNull(message = "Active must be provided")
    private Boolean active;

    public Boolean isActive() {
        return active;
    }
}
