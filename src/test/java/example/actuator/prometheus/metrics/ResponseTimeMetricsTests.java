package example.actuator.prometheus.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTimeMetricsTests {

    private MeterRegistry meterRegistry;
    private ResponseTimeMetrics responseTimeMetrics;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        responseTimeMetrics = new ResponseTimeMetrics(meterRegistry);
    }

    @Test
    void shouldRegisterResponseTimeTimer() {
        Timer timer = meterRegistry.find("custom_http_request_duration_seconds").timer();

        assertNotNull(timer, "Response time timer should be registered");
        assertEquals("custom_http_request_duration_seconds", timer.getId().getName());
        assertEquals(0, timer.count(), "Initial timer count should be 0");
    }

    @Test
    void shouldRecordResponseTime() {
        responseTimeMetrics.recordResponseTime(0.5);
        responseTimeMetrics.recordResponseTime(1.2);

        Timer timer = meterRegistry.find("custom_http_request_duration_seconds").timer();

        assertNotNull(timer, "Response time timer should still be registered");
        assertEquals(2, timer.count(), "Timer count should be incremented to 2");
    }
}
