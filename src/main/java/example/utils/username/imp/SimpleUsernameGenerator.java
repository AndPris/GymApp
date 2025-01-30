package example.utils.username.imp;

import example.entities.User;
import example.storages.imp.TrainerStorage;
import example.storages.imp.TraineeStorage;
import example.utils.username.UsernameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SimpleUsernameGenerator implements UsernameGenerator {
    private TrainerStorage trainerStorage;
    private TraineeStorage traineeStorage;

    @Autowired
    public void setTrainerStorage(TrainerStorage trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Autowired
    public void setTraineeStorage(TraineeStorage traineeStorage) {
        this.traineeStorage = traineeStorage;
    }


    @Override
    public String generateUsername(User user) {
        StringBuilder username = new StringBuilder();
        username.append(user.getFirstName());
        username.append(".");
        username.append(user.getLastName());

        int serialNumber = getUsernameSerialNumber(user);
        if(serialNumber != 0)
            username.append(serialNumber);

        return username.toString();
    }

    private int getUsernameSerialNumber(User user) {
        return Math.max(getUsernameSerialNumber(traineeStorage.getData(), user),
                getUsernameSerialNumber(trainerStorage.getData(), user));
    }

    private <T extends User> int getUsernameSerialNumber(Map<Long, T> users, User user) {
        int serialNumber = -1;

        for(User currentUser : users.values()) {
            if(!areFullNameEquals(currentUser, user))
                continue;

            String actualUsername = currentUser.getUsername();
            String expectedUsername = currentUser.getFirstName() + "." + currentUser.getLastName();
            String stringSerialNumber = actualUsername.substring(expectedUsername.length());

            if(stringSerialNumber.isEmpty())
                stringSerialNumber = "0";

            if(Integer.parseInt(stringSerialNumber) > serialNumber)
                serialNumber = Integer.parseInt(stringSerialNumber);
        }

        return serialNumber + 1;
    }

    private boolean areFullNameEquals(User user1, User user2) {
        return user1.getFirstName().equals(user2.getFirstName()) && user1.getLastName().equals(user2.getLastName());
    }
}
