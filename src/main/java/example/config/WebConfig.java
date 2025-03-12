package example.config;

import example.logs.interceptors.LoggingInterceptor;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoggingInterceptor loggingInterceptor;

    public WebConfig(LoggingInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor).addPathPatterns("/**");
    }

    @Bean
    public GroupedOpenApi dafault() {
        return GroupedOpenApi.builder()
                .group("all")
                .packagesToScan("example.controllers")
                .pathsToMatch("/**").build();
    }

    @Bean
    public OpenAPI ApiInfo() {
        return new OpenAPI()
                .info(new Info().title("GymApp")
                        .description("REST Api documentation for Gym application")
                        .version("v1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
