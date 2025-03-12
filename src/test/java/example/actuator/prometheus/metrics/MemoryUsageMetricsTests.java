package example.actuator.prometheus.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryUsageMetricsTests {

    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        new MemoryUsageMetrics(meterRegistry);
    }

    @Test
    void shouldRegisterMemoryUsageGauge() {
        Gauge memoryGauge = meterRegistry.find("custom_jvm_memory_used").gauge();

        assertNotNull(memoryGauge, "Memory usage gauge should be registered");
        assertEquals("custom_jvm_memory_used", memoryGauge.getId().getName());
        assertEquals("megabytes", memoryGauge.getId().getBaseUnit());

        double value = memoryGauge.value();
        assertTrue(value >= 0, "Memory usage should be non-negative");
        assertTrue(value <= (double) Runtime.getRuntime().maxMemory() / 1024 / 1024, "Memory usage should not exceed max memory");
    }
}
