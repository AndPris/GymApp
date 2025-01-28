package example.utils.input;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

@Component
public class InputHandler {
    public Date getDate(String message) {
        boolean run = true;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Scanner scanner = new Scanner(System.in);
        Date birthday = null;

        while(run) {
            try {
                System.out.print(message);
                birthday = format.parse(scanner.nextLine());
                run = false;
            } catch (ParseException e) {
                System.out.println("Invalid date format, try again");
            }
        }

        return birthday;
    }

    public int getUserInput(int minimalValue, int maximalValue) {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        while (userInput < minimalValue || userInput > maximalValue) {
            if (!scanner.hasNextInt()) {
                System.out.println("Please enter an integer");
                scanner.next();
                continue;
            }

            userInput = scanner.nextInt();
            if (userInput < minimalValue || userInput > maximalValue)
                System.out.println("Number must be from " + minimalValue + " to " + maximalValue);
        }

        return userInput;
    }

    public Long getLong() {
        Scanner scanner = new Scanner(System.in);

        while(!scanner.hasNextLong()) {
            System.out.println("Please enter an integer");
            scanner.next();
        }

        return scanner.nextLong();
    }

    public float getFloat() {
        Scanner scanner = new Scanner(System.in);

        while(!scanner.hasNextFloat()) {
            System.out.println("Please enter a float number");
            scanner.next();
        }

        return scanner.nextFloat();
    }
}
