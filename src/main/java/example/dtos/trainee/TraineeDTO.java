package example.dtos.trainee;

import example.dtos.trainer.TrainerSummaryDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TraineeDTO {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private Boolean active;
    private List<TrainerSummaryDTO> trainersList;
}
