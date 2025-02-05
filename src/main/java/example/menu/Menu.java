package example.menu;

import org.springframework.stereotype.Component;

@Component
public class Menu {
    public void displayMenu() {
        System.out.println("Choose an option, please:");
        System.out.println("\t1 -> Create a new trainee");
        System.out.println("\t2 -> Create a new trainer");
        System.out.println("\t3 -> Create a new training\n");

        System.out.println("\t4 -> Update a trainee");
        System.out.println("\t5 -> Update a trainer\n");

        System.out.println("\t6 -> Delete a trainee\n");

        System.out.println("\t7 -> Select all trainees");
        System.out.println("\t8 -> Select all trainers");
        System.out.println("\t9 -> Select all trainings\n");

        System.out.println("\t10 -> Select a trainee");
        System.out.println("\t11 -> Select a trainer");
        System.out.println("\t12 -> Select a training\n");

        System.out.println("\t13 -> Select a trainee by username\n");

        System.out.println("\t14 -> Delete a trainee by username\n");

        System.out.println("\t15 -> Change trainee's password\n");

        System.out.println("\t16 -> Exit\n");

        System.out.print("->");
    }

    public void displayTrainingTypeMenu() {
        System.out.println("Training type:");

        System.out.println("\t1 -> Fitness");
        System.out.println("\t2 -> Pilates");
        System.out.println("\t3 -> Athletics");
    }
}
