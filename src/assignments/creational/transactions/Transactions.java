package assignments.creational.transactions;

import java.time.LocalDate;

import assignments.creational.transactions.builder.Transaction;
import assignments.creational.transactions.factory.paymentfactories.CreditCardPaymentFactory;
import assignments.creational.transactions.factory.paymentfactories.NetBankingPaymentFactory;
import assignments.creational.transactions.factory.paymentfactories.PaymentProcessorFactory;
import assignments.creational.transactions.factory.paymentfactories.UpiPaymentFactory;
import assignments.creational.transactions.factory.paymentprocessors.PaymentProcessor;
import assignments.creational.transactions.singleton.TransactionLogger;

public class Transactions {
    public static void main(String[] args) {
        TransactionLogger.getLogger().log("Info", "Transaction started");

        System.out.println("");

        /**
         * UPI Payments
         */

        TransactionLogger.getLogger().log("Info", "UPI payment");
        PaymentProcessorFactory upiFactory = new UpiPaymentFactory();
        PaymentProcessor upiProcessor = upiFactory.createPaymentProcessor();

        Transaction upiTransaction = new Transaction.TransactionBuilder("TXN-001", "CUS-001", 2000)
                .setCurrency("USD")
                .setTimestamp(LocalDate.now().toString())
                .build();

        upiProcessor.processPayment(upiTransaction.getAmount());
        TransactionLogger.getLogger().log("Info", "UPI payment completed");
        TransactionLogger.getLogger().log("Info", upiTransaction.getTransactionDetails());

        System.out.println("");

        /**
         * Credit Card Payments
         */

        TransactionLogger.getLogger().log("Info", "Credit card payment");
        PaymentProcessorFactory creditCardFactory = new CreditCardPaymentFactory();
        PaymentProcessor creditCardProcessor = creditCardFactory.createPaymentProcessor();

        Transaction creditCardTransaction = new Transaction.TransactionBuilder("TXN-002", "CUS-001", -500)
                .setCurrency("USD")
                .setTimestamp(LocalDate.now().toString())
                .setNotes("Sending negative amount")
                .build();

        creditCardProcessor.processPayment(creditCardTransaction.getAmount());
        TransactionLogger.getLogger().log("Info", "Credit card payment completed");
        TransactionLogger.getLogger().log("Info", creditCardTransaction.getTransactionDetails());

        System.out.println("");

        /**
         * Net Banking Payments
         */

        TransactionLogger.getLogger().log("Info", "Net banking payment");
        PaymentProcessorFactory netBankingFactory = new NetBankingPaymentFactory();
        PaymentProcessor netBankingProcessor = netBankingFactory.createPaymentProcessor();

        Transaction netBankingTransaction = new Transaction.TransactionBuilder("TXN-003", "CUS-001", 1500).build();

        netBankingProcessor.processPayment(netBankingTransaction.getAmount());
        TransactionLogger.getLogger().log("Info", "Net banking payment completed");

        System.out.println("");

        TransactionLogger.getLogger().log("Info", "Transaction completed");

    }
}
