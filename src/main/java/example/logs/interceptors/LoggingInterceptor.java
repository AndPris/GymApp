package example.logs.interceptors;

import example.logs.TransactionLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LogManager.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TransactionLogger.startTransaction();
        String transactionId = TransactionLogger.getTransactionId();

        logger.info("[Transaction ID: {}] Incoming request: {} {}", transactionId, request.getMethod(), request.getRequestURI());

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            logger.info("[Transaction ID: {}] Header: {} = {}", transactionId, headerName, request.getHeader(headerName));
        }

        if (!request.getParameterMap().isEmpty()) {
            request.getParameterMap().forEach((key, value) ->
                    logger.info("[Transaction ID: {}] Query Param: {} = {}", transactionId, key, String.join(", ", value))
            );
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("[Transaction ID: {}] Response status: {}", TransactionLogger.getTransactionId(), response.getStatus());
        TransactionLogger.clearTransaction();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            logger.warn("[Transaction ID: {}] Request failed with exception: {}", TransactionLogger.getTransactionId(), ex.getMessage());
        }
        TransactionLogger.clearTransaction();
    }
}

