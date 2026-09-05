package oops;

/**
 *
 * Encapsulation
 *
 * Encapsulation is the process of bundling data (fields) and the
 * methods that operate on that data into a single unit called a class.
 *
 * It also helps achieve data hiding by restricting direct access
 * to the internal state of an object using access modifiers.
 * 
 * 
 * Why we need Encapsulation?
 * 
 * - Avoids direct data manipulation
 * - Centralizes validation logic
 * - Improves maintainability
 * - Makes code safe and predictable
 *
 *
 * Access Modifiers:
 *
 * 1. private:
 *    Accessible only within the same class.
 *
 * 2. default (no modifier):
 *    Accessible only within the same package.
 *
 * 3. protected:
 *    Accessible within the same package and by subclasses
 *    outside the package through inheritance.
 *
 * 4. public:
 *    Accessible from anywhere.
 *
 *
 * Data Hiding:
 *
 * Fields are commonly declared private so that they cannot be
 * accessed or modified directly from outside the class.
 *
 * Getter and Setter methods can be provided to control access
 * to these private fields.
 *
 * Getter:
 *    Used to read/access the value of a private field.
 *
 * Setter:
 *    Used to modify/update the value of a private field.
 *
 */

class BankAccount {
    // Private data member
    // cannot be accessed directly from outside this class.
    private String accountNumber;
    private double balance;

    // Constructor
    public BankAccount(String accountNumber, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }

        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    // Getter methods
    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance; 
    }

    // Methods that modify private data with validation
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Invalid withdrawal amount.");
        }
    }
}

public class Encapsulation {
    public static void main(String[] args) {
        String accountNumber = "873902874929094";
        double initialBalance = 10000.00;

        BankAccount account = new BankAccount(accountNumber, initialBalance);

        // Accessing private data using getter methods
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Initial Balance: " + account.getBalance());

        // Modifying private data using setter methods
        account.deposit(5000.00);

        System.out.println("Balance after deposit (5000.00): " + account.getBalance());

        account.withdraw(2000.00);
        System.out.println("Balance after withdrawal (2000.00): " + account.getBalance());
    }
}
