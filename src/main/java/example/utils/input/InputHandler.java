package example.utils.input;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

@Component
public class InputHandler {
    public Date getDate(String message, boolean allowEmpty) {
        boolean run = true;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Scanner scanner = new Scanner(System.in);
        Date date = null;

        while (run) {
            try {
                System.out.print(message);

                String dateLine = scanner.nextLine();
                if (allowEmpty && StringUtils.isBlank(dateLine)) {
                    return null;
                }

                date = format.parse(dateLine);
                run = false;
            } catch (ParseException e) {
                System.out.println("Invalid date format, try again");
            }
        }

        return date;
    }

    public Integer getInputInRange(int minimalValue, int maximalValue, boolean allowEmpty) {
        Scanner scanner = new Scanner(System.in);
        Integer userInput = 0;

        while (userInput < minimalValue || userInput > maximalValue) {
            userInput = getInteger(scanner, allowEmpty);
            if(allowEmpty && userInput == null) {
                return null;
            }

            if (userInput < minimalValue || userInput > maximalValue) {
                System.out.println("Number must be from " + minimalValue + " to " + maximalValue);
            }
        }

        return userInput;
    }

    public Long getLong() {
        Scanner scanner = new Scanner(System.in);

        while (!scanner.hasNextLong()) {
            System.out.println("Please enter a long number");
            scanner.next();
        }

        return scanner.nextLong();
    }

    public Integer getInteger(boolean allowEmpty) {
        Scanner scanner = new Scanner(System.in);
        return getInteger(scanner, allowEmpty);
    }

    private Integer getInteger(Scanner scanner, boolean allowEmpty) {
        boolean run = true;
        Integer result = null;

        while (run) {
            String line = scanner.nextLine();
            if(allowEmpty && StringUtils.isBlank(line)) {
                return null;
            }

            try {
                result = Integer.parseInt(line);
                run = false;
            } catch (NumberFormatException exception) {
                System.out.println("Please enter an integer number");
            }
        }

        return result;
    }

    public String getLine(boolean allowEmpty) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        if (allowEmpty) {
            return line;
        }

        while (StringUtils.isBlank(line)) {
            System.out.println("Please enter a non empty line");
            line = scanner.nextLine();
        }

        return line;
    }
}
