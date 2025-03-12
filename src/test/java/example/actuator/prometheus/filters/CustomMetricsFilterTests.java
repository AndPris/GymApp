package example.actuator.prometheus.filters;

import example.actuator.prometheus.metrics.RequestsCounterMetrics;
import example.actuator.prometheus.metrics.ResponseTimeMetrics;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class CustomMetricsFilterTests {

    private CustomMetricsFilter filter;
    private RequestsCounterMetrics requestCounter;
    private ResponseTimeMetrics responseTime;
    private HttpServletRequest request;
    private ServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        requestCounter = mock(RequestsCounterMetrics.class);
        responseTime = mock(ResponseTimeMetrics.class);
        request = mock(HttpServletRequest.class);
        response = mock(ServletResponse.class);
        chain = mock(FilterChain.class);

        filter = new CustomMetricsFilter(requestCounter, responseTime);
    }

    @Test
    void shouldIncrementRequestCounterForHttpServletRequest() throws IOException, ServletException {
        filter.doFilter(request, response, chain);

        verify(requestCounter, times(1)).incrementRequests();
    }

    @Test
    void shouldNotIncrementRequestCounterForNonHttpServletRequest() throws IOException, ServletException {
        ServletRequest nonHttpRequest = mock(ServletRequest.class);

        filter.doFilter(nonHttpRequest, response, chain);

        verify(requestCounter, never()).incrementRequests();
    }

    @Test
    void shouldRecordResponseTime() throws IOException, ServletException {
        filter.doFilter(request, response, chain);

        verify(responseTime, times(1)).recordResponseTime(any(Double.class));
    }

    @Test
    void shouldCallFilterChain() throws IOException, ServletException {
        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }
}
