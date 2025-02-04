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
        String result = inputHandler.getLine(true);
        assertEquals(" ", result);
    }

    @Test
    public void getLineEmptyNotAllowedTest() {
        String input = " \ntest\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String result = inputHandler.getLine(false);
        assertEquals("test", result);
    }

    @Test
    public void getFloatTest() {
        String input = "test\n3,14\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Float result = inputHandler.getFloat();
        assertEquals(3.14f, result);
    }

    @Test
    public void getLongTest() {
        String input = "test\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Long result = inputHandler.getLong();
        assertEquals(5L, result);
    }

    @Test
    public void getInputInRangeTest() {
        String input = "test\n-1\n10\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        int result = inputHandler.getInputInRange(1, 7);
        assertEquals(5, result);
    }

    @Test
    public void getDateTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String input = "test\n10-10-1010\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Date result = inputHandler.getDate("test");
        assertEquals(format.parse("10-10-1010"), result);
    }
}
