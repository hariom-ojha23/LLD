package assignments.creational.transactions.singleton;

public class TransactionLogger {
    private TransactionLogger() {
    }

    private static class TransactionLoggerHolder {
        private static final TransactionLogger logger = new TransactionLogger();
    }

    public static TransactionLogger getLogger() {
        return TransactionLoggerHolder.logger;
    }

    public void log(String type, String message) {
        System.out.println(type + ": " + message);
    }
}
