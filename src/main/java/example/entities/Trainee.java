package example.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Trainee extends User {
    private String address;
    private Date dateOfBirth;

    public Trainee(Long id, String firstName, String lastName, String username, String password, boolean isActive, String address, Date dateOfBirth) {
        super(id, firstName, lastName, username, password, isActive);
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
}
