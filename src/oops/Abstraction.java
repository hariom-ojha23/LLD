package oops;

/**
 * 
 * Abstraction
 * 
 * Abstraction means hiding implementation details
 * and exposing only the essential functionality to the user.
 *
 * Abstraction can be achieved using:
 * 1. Abstract classes
 * 2. Interfaces
 *
 * Abstract Class:
 * - It is created using the 'abstract' keyword.
 * - An abstract class cannot be instantiated directly.
 * - It can contain both abstract and non-abstract (concrete) methods.
 * - An abstract method has no implementation/body.
 * - A concrete subclass must implement all inherited abstract methods,
 * unless the subclass is also abstract.
 * 
 * Why Abstraction is needed?
 * 
 * - Clients should not depend on internal logic
 * - Makes code easy to change and extend
 * - Improves readability and intent
 * - Enables plug and play behaviour
 * - Prepares code for future growth
 */

abstract class Payment {
    protected double amount;

    public abstract void pay();

    public void generateReceipt() {
        System.out.println("Receipt generated for the payment of $" + amount);
    }
}

class CreditCardPayment extends Payment {
    public CreditCardPayment(double amount) {
        this.amount = amount;
    }

    @Override 
    public void pay() {
        System.out.println("Processing credit card payment of $" + amount);
    }
}

class UPIPayment extends Payment {
    public UPIPayment(double amount) {
        this.amount = amount;
    }

    @Override 
    public void pay() {
        System.out.println("Processing UPI payment of $" + amount);
    }
}

public class Abstraction {
    public static void main(String[] args) {
        double creditCardAmount = 100.00;
        double upiAmount = 250.00;

        System.out.println("Payment Processing using Abstraction:\n");
        
        // Upcasting
        Payment cardPayment = new CreditCardPayment(creditCardAmount);
        cardPayment.pay();
        cardPayment.generateReceipt();

        System.out.println("=================================");

        // Upcasting
        Payment upiPayment = new UPIPayment(upiAmount);
        upiPayment.pay();
        upiPayment.generateReceipt();
    }
}
