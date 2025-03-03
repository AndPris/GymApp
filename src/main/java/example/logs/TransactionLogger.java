package example.logs;


import java.util.UUID;

public class TransactionLogger {
    private static final ThreadLocal<String> transactionId = new ThreadLocal<>();

    public static void startTransaction() {
        transactionId.set(UUID.randomUUID().toString());
    }

    public static String getTransactionId() {
        return transactionId.get();
    }

    public static void clearTransaction() {
        transactionId.remove();
    }
}
