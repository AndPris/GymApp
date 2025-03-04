package example.actuator.prometheus.filters;

import example.actuator.prometheus.metrics.RequestsCounterMetrics;
import example.actuator.prometheus.metrics.ResponseTimeMetrics;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@WebFilter("/*")
public class CustomMetricsFilter implements Filter {
    private final RequestsCounterMetrics requestCounter;
    private final ResponseTimeMetrics responseTime;

    public CustomMetricsFilter(RequestsCounterMetrics requestCounter, ResponseTimeMetrics responseTime) {
        this.requestCounter = requestCounter;
        this.responseTime = responseTime;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        double startTime = System.currentTimeMillis();

        if (request instanceof HttpServletRequest) {
            requestCounter.incrementRequests();
        }

        chain.doFilter(request, response);

        double duration = (System.currentTimeMillis() - startTime) / 1_000;
        responseTime.recordResponseTime(duration);
    }

    @Override
    public void destroy() {
    }
}
