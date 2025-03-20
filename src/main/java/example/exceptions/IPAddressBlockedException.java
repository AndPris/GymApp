package example.exceptions;

public class IPAddressBlockedException extends RuntimeException {
    public IPAddressBlockedException(String message) {
        super(message);
    }
}