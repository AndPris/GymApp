package example.security.aspects;

import example.entities.Trainer;
import example.security.services.TrainerAuthService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TrainerAuthAspect {
    private final TrainerAuthService trainerAuthService;

    public TrainerAuthAspect(TrainerAuthService trainerAuthService) {
        this.trainerAuthService = trainerAuthService;
    }

    @Around("execution(* example.facade.handlers.TrainingOptionHandler.createTraining(.., example.entities.Trainer)) || " +
            "execution(* example.facade.handlers.TrainerOptionHandler.*(.., example.entities.Trainer)) && " +
            "!execution(* example.facade.handlers.TrainerOptionHandler.createTrainer(..)) && " +
            "!execution(* example.facade.handlers.TrainerOptionHandler.selectAllTrainers(..))")
    public Object authentication(ProceedingJoinPoint joinPoint) throws Throwable {
        Trainer trainer = trainerAuthService.authenticateTrainer();

        Object[] args = joinPoint.getArgs();
        args[args.length - 1] = trainer;

        return joinPoint.proceed(args);
    }
}
