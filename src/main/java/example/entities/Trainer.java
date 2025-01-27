package example.entities;

import lombok.Getter;
import lombok.Setter;

public class Trainer extends User {
    @Setter
    @Getter
    private TrainingType specialization;

    public Trainer(String firstName, String lastName, TrainingType specialization) {
        super(firstName, lastName);
        this.specialization = specialization;
    }
}
