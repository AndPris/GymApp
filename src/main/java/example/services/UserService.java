package example.services;

public interface UserService {
    boolean existsUserByUsernameAndPassword(String username, String password);

    void changeUserPassword(String username, String oldPassword, String newPassword);
}
