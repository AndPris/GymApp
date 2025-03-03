package example.dtos.trainer;

import example.dtos.trainee.TraineeSummaryDTO;
import lombok.Data;

import java.util.List;

@Data
public class TrainerDTO {
    private String firstName;
    private String lastName;
    private Long specialization;
    private Boolean active;
    private List<TraineeSummaryDTO> traineesList;
}
