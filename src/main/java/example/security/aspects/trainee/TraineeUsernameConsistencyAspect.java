package example.security.aspects.trainee;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;

@Aspect
@Component
public class TraineeUsernameConsistencyAspect {

    @Around("execution(* example.controllers.TraineeRestController.deleteTraineeByUsername(String, ..)) ||" +
            "execution(* example.controllers.TraineeRestController.toggleTraineeActiveStatus(String, ..)) ||" +
            "execution(* example.controllers.TraineeRestController.updateTrainee(String, ..)) ||" +
            "execution(* example.controllers.TraineeRestController.updateTraineeTrainerList(String, ..))")
    public Object checkUsernameConsistency(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String pathUsername = (String) args[0];

        String authorizedUsername = TraineeAuthAspect.getAuthorizedUsername();

        if (authorizedUsername == null) {
            return new ResponseEntity<>("Authorization header is missing or invalid", HttpStatus.UNAUTHORIZED);
        }

        if (!authorizedUsername.equals(pathUsername)) {
            return new ResponseEntity<>("Forbidden: You can not perform this operation", HttpStatus.FORBIDDEN);
        }

        return joinPoint.proceed();
    }
}
