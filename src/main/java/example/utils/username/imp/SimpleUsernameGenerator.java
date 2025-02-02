package example.utils.username.imp;

import example.entities.Trainee;
import example.entities.Trainer;
import example.entities.User;
import example.storages.Storage;
import example.utils.username.UsernameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SimpleUsernameGenerator implements UsernameGenerator {
    private Storage<Trainer> trainerStorage;
    private Storage<Trainee> traineeStorage;

    @Autowired
    public void setTrainerStorage(Storage<Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Autowired
    public void setTraineeStorage(Storage<Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
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
        return Math.max(getUsernameSerialNumber(traineeStorage, user),
                getUsernameSerialNumber(trainerStorage, user));
    }

    private <T extends User> int getUsernameSerialNumber(Storage<T> storage, User user) {
        int maxSerial = -1;

        for (User currentUser : storage.values()) {
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
