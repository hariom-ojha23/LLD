package assignments.creational.transactions.factory.paymentprocessors;

import assignments.creational.transactions.singleton.TransactionLogger;

public class CreditCardPaymentProcessor implements PaymentProcessor {
    @Override
    public void processPayment(double amount) {
        System.out.println("Process credit card payment of: $" + amount);

        if (amount < 0) {
            String message = "Credit Card payment of $" + amount + " failed, amount cannot be negative";
            TransactionLogger.getLogger().log("Error", message);
            return;
        }

        String message = "Credit Card payment of $" + amount + " processed successfully";
        TransactionLogger.getLogger().log("Info", message);
    }
}
