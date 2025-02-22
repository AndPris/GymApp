package example.dtos.trainer;

import lombok.Data;

@Data
public class TrainerCreateDTO {
    private String firstName;
    private String lastName;
    private Long specialization;
}
