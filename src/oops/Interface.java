package oops;

/**
 *
 * INTERFACE
 *
 * An interface defines a contract that implementing classes must follow.
 *
 * Interfaces are mainly used for:
 * 1. Abstraction
 * 2. Loose coupling
 * 3. Multiple inheritance of type
 *
 *
 * INTERFACE MEMBERS
 *
 * 1. Abstract methods
 * - public and abstract by default
 *
 * 2. Variables
 * - public, static and final by default
 * - therefore, they are constants
 *
 * 3. Default methods
 * - have a method body
 * - inherited by implementing classes
 *
 * 4. Static methods
 * - belong to the interface
 * - called using InterfaceName.method()
 *
 * 5. Private methods
 * - used internally by default methods
 * - available only inside the interface
 *
 *
 * IMPLEMENTING AN INTERFACE
 *
 * A class uses the 'implements' keyword.
 *
 * A class can implement multiple interfaces.
 *
 *
 * INTERFACE INHERITANCE
 *
 * An interface can extend another interface using 'extends'.
 *
 *
 * POLYMORPHISM
 *
 * An interface reference can point to an object of
 * any class that implements that interface.
 *
 * Example:
 *
 * Payment payment = new CreditCardPayment();
 *
 */

interface Payment {
    // public static final by default
    double taxRate = 0.15;

    // public and abstract by default
    void pay(double amount);

    // Default method:
    // - Has an implementation/body.
    // - Implementing classes inherit this method automatically.
    // - A class can override it if required.
    default void generateReceipt(double amount) {
        System.out.println("Payment amount $" + amount);

        // calling private method
        System.out.println("Tax amount $" + calculateTax(amount));

        // calling private method
        System.out.println("Total amount $" + calculateTotal(amount));
        System.out.println("Receipt generated successfully.");
    }

    // Static method:
    // - Belongs to the interface itself.
    // - Called using the interface name.
    // - Cannot be called through an object/reference.
    static void paymentInfo() {
        System.out.println("Tax rate is " + (taxRate * 100) + "%");
    }

    // Private method:
    // - Can only be accessed inside this interface.
    // - Useful for sharing implementation between default methods.
    private double calculateTax(double amount) {
        return amount * taxRate;
    }

    private double calculateTotal(double amount) {
        return amount + calculateTax(amount);
    }
}

// A separate interface defining a refund capability.
interface Refundable {

    // Abstract method.
    // Any concrete class implementing Refundable must implement it.
    void refund(double amount);
}

// SecurePayment extends Payment.
// Therefore, SecurePayment inherits the pay() contract from Payment
// and also adds its own authenticate() contract.
interface SecurePayment extends Payment {

    // Every concrete class implementing SecurePayment
    // must implement authenticate().
    void authenticate();
}

// CreditCardPayment implements two interfaces:
// 1. SecurePayment
// 2. Refundable
//
// SecurePayment itself extends Payment,
// so CreditCardPayment must implement:
// - pay()
// - authenticate()
// - refund()
class CreditCardPayment implements SecurePayment, Refundable {

    // Implementation of the abstract pay() method
    // inherited from Payment.
    @Override
    public void pay(double amount) {
        System.out.println("Processing credit card payment of $" + amount);
    }

    // Implementation of the authenticate() method
    // defined in SecurePayment.
    @Override
    public void authenticate() {
        System.out.println("Authenticating credit card payment credentials...");
    }

    // Implementation of the refund() method
    // defined in Refundable.
    @Override
    public void refund(double amount) {
        System.out.println("Processing credit card refund of $" + amount);
    }
}

// UpiPayment implements Payment.
// Therefore, it must implement the pay() method.
class UpiPayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Processing UPI payment of $" + amount);
    }

    // Overriding the default generateReceipt() method
    // provided by the Payment interface.
    //
    // UPI has its own receipt implementation,
    // so it replaces the default implementation.
    @Override
    public void generateReceipt(double amount) {
        System.out.println("UPI Payment amount $" + amount);
        System.out.println("Receipt generated successfully.");
    }
}

public class Interface {
    public static void main(String[] args) {
        CreditCardPayment creditCardPayment = new CreditCardPayment();
        creditCardPayment.authenticate();
        creditCardPayment.pay(500.0);
        Payment.paymentInfo();
        creditCardPayment.generateReceipt(500.0);

        System.out.println("\nRequested payment refund of $100.0");
        creditCardPayment.refund(100.0);

        System.out.println("========================================");

        // Upcasting:
        // A parent interface reference points to a child class object.
        //
        // Reference type → Payment
        // Object type → UpiPayment
        Payment upiPayment = new UpiPayment();

        // Runtime polymorphism:
        // The overridden method that gets executed is determined
        // at runtime based on the actual object type.
        //
        // Here, UpiPayment's implementation of pay() is executed.
        upiPayment.pay(200.0);

        // UpiPayment has overridden the default generateReceipt()
        // method from Payment, so UpiPayment's version is executed.
        upiPayment.generateReceipt(200.0);
    }
}
