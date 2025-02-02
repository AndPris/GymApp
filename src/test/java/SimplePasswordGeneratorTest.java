import example.utils.password.PasswordGenerator;
import example.utils.password.imp.SimplePasswordGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimplePasswordGeneratorTest {
    private PasswordGenerator passwordGenerator = new SimplePasswordGenerator();
    private final static String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:',.<>?/\\";

    @Test
    public void generatePasswordTest() {
        String password = passwordGenerator.generatePassword();
        assertEquals(10, password.length());
        for (String ch : password.split(""))
            assertTrue(CHARS.contains(ch));
    }
}
