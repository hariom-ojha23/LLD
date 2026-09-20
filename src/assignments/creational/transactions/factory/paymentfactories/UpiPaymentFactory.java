package assignments.creational.transactions.factory.paymentfactories;

import assignments.creational.transactions.factory.paymentprocessors.PaymentProcessor;
import assignments.creational.transactions.factory.paymentprocessors.UpiPaymentProcessor;

public class UpiPaymentFactory extends PaymentProcessorFactory {
    @Override
    public PaymentProcessor createPaymentProcessor() {
        return new UpiPaymentProcessor();
    }
}
