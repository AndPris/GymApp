package example.dtos.trainer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TrainerCreateDTO {
    @NotNull(message = "First name must be provided")
    @Size(min = 2, max = 20, message = "First name must be from 2 to 20 characters")
    private String firstName;

    @NotNull(message = "Last name must be provided")
    @Size(min = 2, max = 20, message = "Last name must be from 2 to 20 characters")
    private String lastName;

    @NotNull(message = "Specialization must be provided")
    private Long specialization;
}
