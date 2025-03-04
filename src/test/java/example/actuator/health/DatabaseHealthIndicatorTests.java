package example.actuator.health;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DatabaseHealthIndicatorTests {

    private JdbcTemplate jdbcTemplate;
    private DatabaseHealthIndicator healthIndicator;

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        healthIndicator = new DatabaseHealthIndicator(jdbcTemplate);
    }

    @Test
    void shouldReturnHealthUpWhenDatabaseIsAvailable() {
        when(jdbcTemplate.queryForObject("SELECT 1", Integer.class)).thenReturn(1);

        Health health = healthIndicator.health();

        assertEquals("UP", health.getStatus().getCode());
        assertTrue(health.getDetails().containsKey("database"));
        assertTrue(health.getDetails().containsKey("query"));
        assertTrue(health.getDetails().containsValue("MySQL"));
        assertTrue(health.getDetails().containsValue("SELECT 1"));
    }

    @Test
    void shouldReturnHealthDownWhenDatabaseIsUnavailable() {
        when(jdbcTemplate.queryForObject("SELECT 1", Integer.class))
                .thenThrow(new RuntimeException("Database connection failed"));

        Health health = healthIndicator.health();

        assertEquals("DOWN", health.getStatus().getCode());
        assertTrue(health.getDetails().containsKey("database"));
        assertTrue(health.getDetails().containsKey("error"));
        assertTrue(health.getDetails().containsValue("MySQL"));
        assertEquals("Database connection failed", health.getDetails().get("error"));
    }
}
