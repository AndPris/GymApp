package example.exceptions;

public class TraineeNotFoundException extends RuntimeException {
    public TraineeNotFoundException(String message) {
        super(message);
    }
}
