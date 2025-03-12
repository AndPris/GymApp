package example.actuator.prometheus.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MemoryUsageMetrics {
    public MemoryUsageMetrics(MeterRegistry meterRegistry) {
        Gauge.builder("custom_jvm_memory_used", Runtime.getRuntime(), runtime ->
                    (double) (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024
                )
                .description("Used memory in JVM")
                .baseUnit("megabytes")
                .register(meterRegistry);
    }
}
