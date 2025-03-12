package example.actuator.prometheus.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class RequestsCounterMetrics {
    private final Counter requestsCounter;

    public RequestsCounterMetrics(MeterRegistry meterRegistry) {
        requestsCounter = Counter.builder("http_requests_total")
                .description("Total number of HTTP requests")
                .register(meterRegistry);
    }

    public void incrementRequests() {
        requestsCounter.increment();
    }
}
