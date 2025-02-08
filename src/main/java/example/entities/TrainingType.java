package example.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Size(min = 2, max = 20, message = "Training type name must be from 2 to 20 characters")
    private String name;

    @OneToMany(mappedBy = "specialization")
    private List<Trainer> trainers = new ArrayList<>();

    @OneToMany(mappedBy = "trainingType")
    private List<Training> trainings = new ArrayList<>();

    public TrainingType() {

    }

    public TrainingType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
