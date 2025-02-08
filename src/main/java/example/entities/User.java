package example.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Size(min = 2, max = 20, message = "First name must be from 2 to 20 characters")
    private String firstName;

    @Column(nullable = false)
    @Size(min = 2, max = 20, message = "Last name must be from 2 to 20 characters")
    private String lastName;

    @Column(unique = true, nullable = false)
    @Size(min = 2, max = 50, message = "Username must be from 2 to 50 characters")
    private String username;

    @Column(nullable = false)
    @Size(min = 6, max = 20, message = "Password must be from 6 to 20 characters")
    private String password;

    @Column(nullable = false)
    private Boolean active;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = true;
    }

    public User() {
    }

    public Boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "First name: " + firstName + "\n" +
                "Last name: " + lastName + "\n" +
                "Username: " + username + "\n" +
                "Password: " + password + "\n" +
                "Is active: " + active + "\n";
    }
}
