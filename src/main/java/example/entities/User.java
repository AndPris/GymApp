package example.entities;

import lombok.Data;

@Data
public abstract class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = true;
    }
}
