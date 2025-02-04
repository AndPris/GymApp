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

    public Trainer() {
        super();
    }


    @Override
    public String toString() {
        return super.toString() +
                "Specialization: " + specialization + "\n";
    }
}
