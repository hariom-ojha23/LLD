package assignments.creational.transactions.factory.paymentprocessors;

import assignments.creational.transactions.singleton.TransactionLogger;

public class UpiPaymentProcessor implements PaymentProcessor {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing UPI payment of amount: $" + amount);

        if (amount < 0) {
            String message = "UPI payment of: $" + amount + " failed, amount cannot be negative";
            TransactionLogger.getLogger().log("Error", message);
            return;
        }

        String message = "UPI payment of: $" + amount + " processed successfully";
        TransactionLogger.getLogger().log("Info", message);
    }
}
