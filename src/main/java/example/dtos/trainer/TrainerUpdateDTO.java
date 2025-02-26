package example.dtos.trainer;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrainerUpdateDTO extends TrainerCreateDTO {
    @NotNull(message = "Active must be provided")
    private Boolean active;

    public Boolean isActive() {
        return active;
    }
}
