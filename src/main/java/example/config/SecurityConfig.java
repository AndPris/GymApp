package example.config;

import example.exceptions.IPAddressBlockedException;
import example.repositories.UserRepository;
import example.security.LoginAttemptService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/trainees").permitAll()
                        .requestMatchers(HttpMethod.POST, "/trainers").permitAll()
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("Logged out — close and reopen the browser to clear credentials.");
                        }));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(LoginAttemptService loginAttemptService, UserRepository userRepository) {
        return username -> {
            if(loginAttemptService.isBlocked()) {
                throw new IPAddressBlockedException("Try to login later, please");
            }

            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("There's no user with such username: " + username));
        };
    }
}
