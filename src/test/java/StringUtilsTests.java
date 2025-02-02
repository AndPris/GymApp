import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static example.utils.string.StringUtils.isNotEmpty;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringUtilsTests {
    @Test
    public void isNotEmptyTestTrue() {
        assertTrue(isNotEmpty("string"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "\n", "\n ", "\t", " \t "})
    public void isNotEmptyTestFalse(String testString) {
        assertFalse(isNotEmpty(testString));
    }
}
