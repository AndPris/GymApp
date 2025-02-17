package example.utils.input;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InputHandlerTests {
    private InputStream originalInputStream;
    private InputHandler inputHandler;

    @BeforeEach
    public void init() {
        inputHandler = new InputHandler();
        originalInputStream = System.in;
    }

    @AfterEach
    public void destroy() {
        System.setIn(originalInputStream);
    }

    @Test
    public void getLineEmptyAllowedTest() {
        String input = " \ntest\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String result = inputHandler.getLine("", true);
        assertEquals(null, result);
    }

    @Test
    public void getLineEmptyNotAllowedTest() {
        String input = " \ntest\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String result = inputHandler.getLine("", false);
        assertEquals("test", result);
    }

    @Test
    public void getIntegerEmptyNotAllowedTest() {
        String input = " \ntest\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Integer result = inputHandler.getInteger(false);
        assertEquals(5, result);
    }

    @Test
    public void getIntegerEmptyAllowedTest() {
        String input = "test\n \n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Integer result = inputHandler.getInteger(true);
        assertNull(result);
    }

    @Test
    public void getLongTest() {
        String input = "test\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Long result = inputHandler.getLong();
        assertEquals(5L, result);
    }

    @Test
    public void getInputInRangeEmptyNotAllowedTest() {
        String input = " \ntest\n-1\n10\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Integer result = inputHandler.getInputInRange(1, 7, false);
        assertEquals(5, result);
    }

    @Test
    public void getInputInRangeEmptyAllowedTest() {
        String input = "test\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Integer result = inputHandler.getInputInRange(1, 7, true);
        assertNull(result);
    }

    @Test
    public void getDateEmptyNotAllowedTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String input = " \ntest\n10-10-1010\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Date result = inputHandler.getDate("test", false);
        assertEquals(format.parse("10-10-1010"), result);
    }

    @Test
    public void getDateEmptyAllowedTest() {
        String input = "test\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Date result = inputHandler.getDate("test", true);
        assertNull(result);
    }
}
