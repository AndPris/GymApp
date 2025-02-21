package example.dtos.trainee;

import lombok.Data;

import java.util.Date;

@Data
public class TraineeCreateDTO {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
}
