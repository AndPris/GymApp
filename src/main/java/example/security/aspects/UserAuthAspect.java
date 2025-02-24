package example.security.aspects;

import example.dtos.CredentialsDTO;
import example.security.services.UserAuthService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Aspect
@Component
public class UserAuthAspect {
    private final UserAuthService userAuthService;

    private final ThreadLocal<String> authorizedUsername = new ThreadLocal<>();

    public UserAuthAspect(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    public String getAuthorizedUsername() {
        return authorizedUsername.get();
    }

    @Around("execution(* example.controllers.TraineeRestController.*(.., String)) && " +
            "!execution(* example.controllers.TraineeRestController.createTrainee(..)) && " +
            "!execution(* example.controllers.TraineeRestController.getAllTrainees(..))")
    public Object authentication(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String authHeader = (String) args[args.length-1];

        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return new ResponseEntity<>("Authorization header is missing or invalid", HttpStatus.UNAUTHORIZED);
        }

        CredentialsDTO credentialsDTO = parseAuthHeader(authHeader);
        if(!userAuthService.userExistsByUsernameAndPassword(credentialsDTO.getUsername(),
                credentialsDTO.getPassword())) {
            return new ResponseEntity<>("Authorization fails: no such user", HttpStatus.UNAUTHORIZED);
        }

        authorizedUsername.set(credentialsDTO.getUsername());

        return joinPoint.proceed();
    }

    private CredentialsDTO parseAuthHeader(String authHeader) {
        String base64Credentials = authHeader.substring(authHeader.indexOf(' ') + 1);
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String decodedCredentials = new String(decodedBytes, StandardCharsets.UTF_8);

        String[] credentials = decodedCredentials.split(":", 2);

        CredentialsDTO credentialsDTO = new CredentialsDTO();
        credentialsDTO.setUsername(credentials[0]);
        credentialsDTO.setPassword(credentials[1]);

        return credentialsDTO;
    }
}
