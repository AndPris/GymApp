package example.utils.password.imp;

import example.utils.password.PasswordGenerator;

public class SimplePasswordGenerator implements PasswordGenerator {
    private final static String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:',.<>?/\\";
    private final static int PASSWORD_LENGTH = 10;

    @Override
    public String generatePassword() {
        StringBuilder password = new StringBuilder();

        for(int i = 0; i < PASSWORD_LENGTH; i++)
            password.append(CHARS.charAt((int) (Math.random() * CHARS.length())));

        return password.toString();
    }
}
