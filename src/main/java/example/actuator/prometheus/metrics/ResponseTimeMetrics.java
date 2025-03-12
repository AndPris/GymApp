package example.actuator.prometheus.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class ResponseTimeMetrics {
    private final Timer responseTimeTimer;

    public ResponseTimeMetrics(MeterRegistry meterRegistry) {
        responseTimeTimer = Timer.builder("custom_http_request_duration_seconds")
                .description("HTTP request durations in seconds.")
                .register(meterRegistry);

    }

    public void recordResponseTime(double duration) {
        responseTimeTimer.record(() -> duration);
    }
}

