package example.security.aspects;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;

@Aspect
@Component
public class UserUsernameConsistencyAspect {
    private final UserAuthAspect userAuthAspect;

    public UserUsernameConsistencyAspect(UserAuthAspect userAuthAspect) {
        this.userAuthAspect = userAuthAspect;
    }

    @Around("@annotation(example.security.annotations.Authorized)")
    public Object checkUsernameConsistency(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String pathUsername = (String) args[0];

        String authorizedUsername = userAuthAspect.getAuthorizedUsername();

        if (authorizedUsername == null) {
            return new ResponseEntity<>("Authorization header is missing or invalid", HttpStatus.UNAUTHORIZED);
        }

        if (!authorizedUsername.equals(pathUsername)) {
            return new ResponseEntity<>("Forbidden: You can not perform this operation", HttpStatus.FORBIDDEN);
        }

        return joinPoint.proceed();
    }
}
