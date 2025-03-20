package example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GymApp {
    public static void main(String[] args) {
        SpringApplication.run(GymApp.class, args);
    }
}
