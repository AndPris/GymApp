package example.dtos.trainee;

import com.fasterxml.jackson.annotation.JsonFormat;
import example.dtos.trainer.TrainerSummaryDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TraineeDTO {
    private String firstName;
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date dateOfBirth;
    private String address;
    private Boolean active;
    private List<TrainerSummaryDTO> trainersList;
}
