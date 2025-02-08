package example.security.aspects;

import example.entities.Trainee;
import example.security.services.TraineeAuthService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TraineeAuthAspect {
    private final TraineeAuthService traineeAuthService;

    public TraineeAuthAspect(TraineeAuthService traineeAuthService) {
        this.traineeAuthService = traineeAuthService;
    }

    @Around("execution(* example.facade.handlers.TraineeOptionHandler.*(.., example.entities.Trainee)) && " +
            "!execution(* example.facade.handlers.TraineeOptionHandler.createTrainee(..)) && " +
            "!execution(* example.facade.handlers.TraineeOptionHandler.selectAllTrainees(..))")
    public Object authentication(ProceedingJoinPoint joinPoint) throws Throwable {
        Trainee trainee = traineeAuthService.authenticateTrainee();

        Object[] args = joinPoint.getArgs();
        args[args.length - 1] = trainee;

        return joinPoint.proceed(args);
    }
}
