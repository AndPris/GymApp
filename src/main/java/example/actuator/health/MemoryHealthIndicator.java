package example.actuator.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

@Component
public class MemoryHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();

        long usedMemory = heapMemoryUsage.getUsed();
        long maxMemory = heapMemoryUsage.getMax();

        double usagePercentage = ((double) usedMemory / maxMemory) * 100;

        if (usagePercentage < 80) {
            return Health.up().withDetail("heapMemoryUsage", usagePercentage + "% used").build();
        } else {
            return Health.down().withDetail("heapMemoryUsage", usagePercentage + "% used, nearing limit!").build();
        }
    }
}
