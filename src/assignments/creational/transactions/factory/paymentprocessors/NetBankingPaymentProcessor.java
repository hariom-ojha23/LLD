package assignments.creational.transactions.factory.paymentprocessors;

import assignments.creational.transactions.singleton.TransactionLogger;

public class NetBankingPaymentProcessor implements PaymentProcessor {
    @Override
    public void processPayment(double amount) {
        System.out.println("Process Net Banking payment of: " + amount);

        if (amount < 0) {
            String message = "Net Banking payment of: " + amount + " failed (negative amount)";
            TransactionLogger.getLogger().log("Error", message);
            return;
        }

        String message = "Net Banking payment of: " + amount + " processed successfully";
        TransactionLogger.getLogger().log("Info", message);
    }
}
