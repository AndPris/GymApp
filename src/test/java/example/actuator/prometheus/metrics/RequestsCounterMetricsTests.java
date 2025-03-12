package example.actuator.prometheus.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestsCounterMetricsTests {

    private MeterRegistry meterRegistry;
    private RequestsCounterMetrics requestsCounterMetrics;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        requestsCounterMetrics = new RequestsCounterMetrics(meterRegistry);
    }

    @Test
    void shouldRegisterHttpRequestsCounter() {
        Counter counter = meterRegistry.find("http_requests_total").counter();

        assertNotNull(counter, "HTTP requests counter should be registered");
        assertEquals("http_requests_total", counter.getId().getName());
        assertEquals(0, counter.count(), "Initial counter value should be 0");
    }

    @Test
    void shouldIncrementHttpRequestsCounter() {
        requestsCounterMetrics.incrementRequests();
        requestsCounterMetrics.incrementRequests();

        Counter counter = meterRegistry.find("http_requests_total").counter();

        assertNotNull(counter, "HTTP requests counter should still be registered");
        assertEquals(2, counter.count(), "Counter value should be incremented to 2");
    }
}
