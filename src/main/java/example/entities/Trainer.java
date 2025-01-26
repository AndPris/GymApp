package example.entities;

import lombok.Getter;
import lombok.Setter;

public class Trainer extends User {
    @Setter
    @Getter
    private TrainingType specialization;

    public Trainer(Long id, String firstName, String lastName, String username, String password, boolean isActive, TrainingType specialization) {
        super(id, firstName, lastName, username, password, isActive);
        this.specialization = specialization;
    }
}
