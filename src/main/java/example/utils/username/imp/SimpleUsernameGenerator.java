package example.utils.username.imp;

import example.entities.User;
import example.repositories.UserRepository;
import example.utils.username.UsernameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SimpleUsernameGenerator implements UsernameGenerator {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String generateUsername(User user) {
        String baseUsername = formatUsername(user);
        int serialNumber = getUsernameSerialNumber(userRepository.findAll(), user);
        return serialNumber == 0 ? baseUsername : baseUsername + serialNumber;
    }

    private String formatUsername(User user) {
        return user.getFirstName() + "." + user.getLastName();
    }

    private int getUsernameSerialNumber(List<User> users, User user) {
        int maxSerial = -1;

        for (User currentUser : users) {
            String expectedUsername = formatUsername(user);
            String actualUsername = currentUser.getUsername();

            if (!actualUsername.startsWith(expectedUsername)) {
                continue;
            }

            int serial = extractSerialNumber(actualUsername, expectedUsername);
            maxSerial = Math.max(maxSerial, serial);
        }

        return maxSerial + 1;
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
