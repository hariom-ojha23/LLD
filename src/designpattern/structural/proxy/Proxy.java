package designpattern.structural.proxy;

/**
 * 
 * Proxy
 * 
 * Proxy creates a class that has the same interface as
 * real service object
 * 
 * A proxy object controls access to the real object without
 * the client knowing it is talking to a proxy
 * 
 * Proxy = Controlled access to the real object.
 * 
 * 
 * Common uses:
 * - Lazy loading: Create expensive objects when needed
 * - Access Control: Check whether a user is allowed to access something
 * - Caching: Return cached data instead of calling real object
 * - Logging: Log calls before forwarding them
 * - Remote object: Represnt an object that exist on another server
 * - Security: Control access to sensitive resources
 * 
 * 
 * Adapter: Makes incompatible interfaces compatible
 * Decorator: Adds behavior
 * Facade: Simplifies a complex system
 * Proxy: Controls access to an object
 * 
 */

interface BankAccount {
    public void withdraw(double amount);

    public void getBalance();

    public void deposit(double amount);
}

class Customer {
    private final String username;
    private final String password;

    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean validateCustomer(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}

/**
 * 
 * RealBankAccount
 * 
 * RealBankAccount IS-A BankAccount
 * 
 */
class RealBankAccount implements BankAccount {
    private double balance = 0.00;

    @Override
    public void getBalance() {
        System.out.println("Balance: " + balance);
    }

    @Override
    public void withdraw(double amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        balance -= amount;
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
    }
}

/**
 * 
 * BankAccountProxy
 * 
 * BankAccountProxy IS-A BankAccount
 * BankAccountProxy HAS-A BankAccount
 * 
 * Proxy maintains the same interface as the real object,
 * so the client can use the proxy exactly like the real object.
 * 
 * The proxy can perform additional operations such as:
 * - Access control
 * - Logging
 * - Caching
 * - Lazy initialization
 * 
 * before forwarding the request to the real object.
 * 
 */
class BankAccountProxy implements BankAccount {
    private final BankAccount realBankAccount;
    private final boolean isAuthenticated;

    public BankAccountProxy(Customer customer, String username, String password) {
        this.realBankAccount = new RealBankAccount();
        this.isAuthenticated = customer.validateCustomer(username, password);
    }

    private void checkAccess() {
        if (!isAuthenticated) {
            throw new SecurityException("Unauthorized access");
        }
    }

    @Override
    public void getBalance() {
        checkAccess();
        realBankAccount.getBalance();
    }

    @Override
    public void withdraw(double amount) {
        checkAccess();
        realBankAccount.withdraw(amount);
        System.out.println("Withdrawal of " + amount + " successful");
    }

    @Override
    public void deposit(double amount) {
        checkAccess();
        realBankAccount.deposit(amount);
        System.out.println("Deposit of " + amount + " successful");
    }
}

public class Proxy {
    public static void main(String args[]) {
        Customer customer = new Customer("harsh", "password");
        BankAccount bankAccount = new BankAccountProxy(customer, "harsh", "password");

        try {
            bankAccount.deposit(1000);
        } catch (SecurityException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            bankAccount.withdraw(500);
        } catch (SecurityException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            bankAccount.getBalance();
        } catch (SecurityException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
