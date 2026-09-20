package assignments.creational.transactions.factory.paymentfactories;

import assignments.creational.transactions.factory.paymentprocessors.CreditCardPaymentProcessor;
import assignments.creational.transactions.factory.paymentprocessors.PaymentProcessor;

public class CreditCardPaymentFactory extends PaymentProcessorFactory {
    @Override
    public PaymentProcessor createPaymentProcessor() {
        return new CreditCardPaymentProcessor();
    }
}
