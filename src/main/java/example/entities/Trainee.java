package example.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Trainee extends User {
    private String address;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @OneToMany(mappedBy = "trainee")
    private List<Training> trainings = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "traineeId"),
            inverseJoinColumns = @JoinColumn(name = "trainerId")
    )
    private List<Trainer> trainers = new ArrayList<>();

    public Trainee(String firstName, String lastName, String address, Date dateOfBirth) {
        super(firstName, lastName);
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public Trainee() {
        super();
    }

    @Override
    public String toString() {
        return super.toString() +
                "Address: " + address + "\n" +
                "Date of birth: " + dateOfBirth + "\n";
    }

    public void addTrainer(Trainer trainer) {
        trainers.add(trainer);
    }

    public void removeTrainer(Trainer trainer) {
        trainers.remove(trainer);
    }

    public void clearTrainers() {
        trainers.clear();
    }
}
