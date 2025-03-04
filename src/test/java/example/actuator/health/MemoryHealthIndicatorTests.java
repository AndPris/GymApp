package example.actuator.health;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;

import java.lang.management.MemoryUsage;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MemoryHealthIndicatorTests {
    private MemoryHealthIndicator healthIndicator;
    private MemoryUsage memoryUsage;

    @BeforeEach
    void setUp() {
        healthIndicator = new MemoryHealthIndicator();
        memoryUsage = mock(MemoryUsage.class);
        healthIndicator.setHeapMemoryUsage(memoryUsage);
    }

    @Test
    void shouldReturnHealthUpWhenMemoryUsageIsBelowThreshold() {
        when(memoryUsage.getUsed()).thenReturn(400L);
        when(memoryUsage.getMax()).thenReturn(1000L);

        Health health = healthIndicator.health();

        assertEquals("UP", health.getStatus().getCode());
        assertTrue(health.getDetails().containsKey("heapMemoryUsage"));
        assertEquals("40.0% used", health.getDetails().get("heapMemoryUsage"));
    }

    @Test
    void shouldReturnHealthDownWhenMemoryUsageExceedsThreshold() {
        when(memoryUsage.getUsed()).thenReturn(900L);
        when(memoryUsage.getMax()).thenReturn(1000L);

        Health health = healthIndicator.health();

        assertEquals("DOWN", health.getStatus().getCode());
        assertTrue(health.getDetails().containsKey("heapMemoryUsage"));
        assertEquals("90.0% used, nearing limit!", health.getDetails().get("heapMemoryUsage"));
    }
}
