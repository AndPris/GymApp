package example.entities;

import lombok.Data;

@Data
public abstract class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean active;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = true;
    }

    public User() {}

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
