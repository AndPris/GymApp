package example.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Trainee extends User {
    private String address;
    private Date dateOfBirth;

    public Trainee(String firstName, String lastName, String address, Date dateOfBirth) {
        super(firstName, lastName);
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
}
