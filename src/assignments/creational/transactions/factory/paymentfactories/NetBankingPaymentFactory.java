package assignments.creational.transactions.factory.paymentfactories;

import assignments.creational.transactions.factory.paymentprocessors.NetBankingPaymentProcessor;
import assignments.creational.transactions.factory.paymentprocessors.PaymentProcessor;

public class NetBankingPaymentFactory extends PaymentProcessorFactory {
    @Override
    public PaymentProcessor createPaymentProcessor() {
        return new NetBankingPaymentProcessor();
    }
}
