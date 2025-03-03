package example.dtos.trainee;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class TraineeCreateDTO {
    @NotNull(message = "First name must be provided")
    @Size(min = 2, max = 20, message = "First name must be from 2 to 20 characters")
    private String firstName;

    @NotNull(message = "Last name must be provided")
    @Size(min = 2, max = 20, message = "Last name must be from 2 to 20 characters")
    private String lastName;

    private Date dateOfBirth;
    private String address;
}
