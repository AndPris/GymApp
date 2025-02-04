package example.utils.password;

import example.utils.password.imp.SimplePasswordGenerator;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimplePasswordGeneratorTest {
    private PasswordGenerator passwordGenerator = new SimplePasswordGenerator();
    private final static String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:',.<>?/\\";

    @Test
    public void generatePasswordTest() {
        String password = passwordGenerator.generatePassword();
        assertEquals(10, password.length());
        assertTrue(password.matches("[" + Pattern.quote(CHARS) + "]{10}"));
    }
}
