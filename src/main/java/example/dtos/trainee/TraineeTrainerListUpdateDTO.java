package example.dtos.trainee;

import lombok.Data;

import java.util.List;

@Data
public class TraineeTrainerListUpdateDTO {
    private List<String> trainers;
}
