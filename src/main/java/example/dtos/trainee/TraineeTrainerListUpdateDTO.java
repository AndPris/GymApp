package example.dtos.trainee;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TraineeTrainerListUpdateDTO {
    @NotNull(message = "Trainers list must be provided")
    private List<String> trainers;
}
