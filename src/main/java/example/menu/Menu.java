package example.menu;

import example.entities.TrainingType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Menu {
    public void displayMenu() {
        System.out.println("Choose an option, please:");
        System.out.println("\t1 -> Create a new trainee");
        System.out.println("\t2 -> Create a new trainer");
        System.out.println("\t3 -> Create a new training\n");

        System.out.println("\t4 -> Update a trainee");
        System.out.println("\t5 -> Update a trainer\n");

        System.out.println("\t6 -> Select all trainees");
        System.out.println("\t7 -> Select all trainers");
        System.out.println("\t8 -> Select all trainings\n");

        System.out.println("\t9 -> Select a trainee by username");
        System.out.println("\t10 -> Select a trainer by username\n");

        System.out.println("\t11 -> Delete a trainee by username\n");

        System.out.println("\t12 -> Change trainee's password");
        System.out.println("\t13 -> Change trainer's password\n");

        System.out.println("\t14 -> Change trainee's isActive status");
        System.out.println("\t15 -> Change trainer's isActive status\n");

        System.out.println("\t16 -> Display trainee's training list");
        System.out.println("\t17 -> Display trainer's training list\n");

        System.out.println("\t18 -> Get trainers list that not assigned on trainee");
        System.out.println("\t19 -> Update trainee's trainer list\n");

        System.out.println("\t20 -> Exit\n");

        System.out.print("->");
    }

    public void displayTrainingTypeMenu(List<TrainingType> trainingTypes) {
        System.out.println("Training type:");

        for (TrainingType trainingType : trainingTypes) {
            System.out.println("\t" + trainingType.getId() + " -> " + trainingType.getName());
        }
    }

    public void displayUpdateTraineeTrainerListMenu() {
        System.out.println("\t1 -> Display list");
        System.out.println("\t2 -> Add trainer");
        System.out.println("\t3 -> Remove trainer");
        System.out.println("\t4 -> Clear list");
    }
}
