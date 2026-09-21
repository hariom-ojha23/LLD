package designpattern.behavioral.strategy;

/**
 * 
 * Strategy
 * 
 * Strategy pattern defines a family of interchangeable
 * algorithms/behaviors and allows them to be selected at runtime.
 * 
 * 
 * Main Idea:
 * - Encapsulate each behavior separately and make them interchangeable.
 * 
 * 
 * Benefits:
 * - Removes large if-else/switch blocks
 * - Follows Open/Closed principle
 * - Easy to add new strategies
 * - Uses composition instead of inheritance
 * - Separates different behaviors
 * 
 * 
 * Drawbacks:
 * - Increases the number of classes
 * - Client needs to choose/provide the strategy
 * - For simple behaviour, it can add unnecessary complexity
 * 
 * When to use:
 * - When we have multiple ways of performing the same operation
 * and we want to make those behaviors interchangeable.
 * 
 * - When the behavior of an object can be changed at runtime.
 * 
 * - When we have many conditional statements based on the behavior of an
 * object.
 * 
 * 
 */

interface PaymentStrategy {
    public void pay(double amount);
}

/**
 * 
 * UPIPayment
 * 
 * UPIPayment IS-A PaymentStrategy
 * 
 */
class UPIPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paying $" + amount + " through UPI");
    }
}

/**
 * 
 * CreditCardPayment
 * 
 * CreditCardPayment IS-A PaymentStrategy
 */
class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paying $" + amount + " through Credit Card");
    }
}

/**
 * 
 * PaymentService
 * 
 * PaymentService HAS-A PaymentStrategy
 * 
 */
class PaymentService {
    private PaymentStrategy paymentStrategy;

    public PaymentService(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void process(double amount) {
        paymentStrategy.pay(amount);
    }
}

public class Strategy {
    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService(new UPIPayment());
        paymentService.process(2000.00);

        paymentService.setPaymentStrategy(new CreditCardPayment());
        paymentService.process(1500);
    }
}
