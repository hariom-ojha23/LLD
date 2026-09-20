package assignments.creational.transactions.factory.paymentfactories;

import assignments.creational.transactions.factory.paymentprocessors.PaymentProcessor;

public abstract class PaymentProcessorFactory {

    public abstract PaymentProcessor createPaymentProcessor();
}
