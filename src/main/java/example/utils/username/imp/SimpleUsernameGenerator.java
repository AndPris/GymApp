package example.utils.username.imp;

import example.entities.User;
import example.repositories.TraineeRepository;
import example.repositories.TrainerRepository;
import example.utils.username.UsernameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
public class SimpleUsernameGenerator implements UsernameGenerator {
    private TrainerRepository trainerRepository;
    private TraineeRepository traineeRepository;

    @Autowired
    public void setTrainerRepository(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Autowired
    public void setTraineeRepository(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }


    @Override
    public String generateUsername(User user) {
        String baseUsername = formatUsername(user);
        int serialNumber = getUsernameSerialNumber(user);
        return serialNumber == 0 ? baseUsername : baseUsername + serialNumber;
    }

    private String formatUsername(User user) {
        return user.getFirstName() + "." + user.getLastName();
    }

    private int getUsernameSerialNumber(User user) {
        return Math.max(getUsernameSerialNumber(traineeRepository.findAll(), user),
                getUsernameSerialNumber(trainerRepository.findAll(), user));
    }

    private <T extends User> int getUsernameSerialNumber(Collection<T> users, User user) {
        int maxSerial = -1;

        for (User currentUser : users) {
            if (!hasSameFullName(currentUser, user)) {
                continue;
            }

            String actualUsername = currentUser.getUsername();
            String expectedUsername = formatUsername(user);

            if (!actualUsername.startsWith(expectedUsername)) {
                continue;
            }

            int serial = extractSerialNumber(actualUsername, expectedUsername);
            maxSerial = Math.max(maxSerial, serial);
        }

        return maxSerial + 1;
    }

    private boolean hasSameFullName(User user1, User user2) {
        boolean hasSameFirstName = user1.getFirstName().equals(user2.getFirstName());
        boolean hasSameLastName = user1.getLastName().equals(user2.getLastName());
        return hasSameFirstName && hasSameLastName;
    }

    private int extractSerialNumber(String actualUsername, String expectedUsername) {
        String serialPart = actualUsername.substring(expectedUsername.length()).trim();
        return serialPart.isEmpty() ? 0 : parseSerialNumber(serialPart);
    }

    private int parseSerialNumber(String serialPart) {
        try {
            return Integer.parseInt(serialPart);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
