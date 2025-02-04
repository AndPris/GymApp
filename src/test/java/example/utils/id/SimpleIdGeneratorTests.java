package example.utils.id;

import example.utils.id.imp.SimpleIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleIdGeneratorTests {
    private IdGenerator idGenerator;

    @BeforeEach
    public void init() {
        idGenerator = new SimpleIdGenerator();
    }

    @Test
    public void generateIdEmptySetTest() {
        Set<Long> ids = new HashSet<>();
        assertEquals(1L, idGenerator.generateId(ids));
        assertEquals(1L, idGenerator.generateId(null));
    }

    @Test
    public void generateIdNonEmptySetTest() {
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        assertEquals(2L, idGenerator.generateId(ids));
        ids.add(2L);
        assertEquals(3L, idGenerator.generateId(ids));
    }
}
