package designpattern.creational;

/**
 * Singleton
 * 
 * Singleton pattern ensures that a class has only one instance throughout
 * the application and provides a global access point to that instance
 * 
 * 
 * Use singleton when ?
 * 
 * - Only one object should exist
 * - Creating multiple instances would be wasteful / incorrect
 * - Multiple parts of the application need access to same object
 * 
 * 
 * Example: Configuration manager, Logger, Cache manager, Connection manager
 * 
 */

/**
 * 
 * BasicSingleton
 * 
 * - It is not thread safe.
 * - Two threads can both see instance == null and create two objects.
 * 
 */
class BasicSingleton {
    private static BasicSingleton instance;

    // private constructor to prevent creating object
    private BasicSingleton() {
    }

    public static BasicSingleton getInstance() {
        if (instance == null) {
            instance = new BasicSingleton();
        }
        return instance;
    }
}

/**
 * 
 * MethodLockSingleton
 * 
 * - Only one thread can execute getInstance method at a time
 *
 * Advantages:
 * - Thread safe
 * - Simple
 * 
 * Disadvantages:
 * - Every call require acquiring the lock, even after object is created.
 * - So there is unnecessary synchronization overhead
 * 
 * 
 */
class MethodLockSingleton {
    private static MethodLockSingleton instance;

    private MethodLockSingleton() {
    }

    public static synchronized MethodLockSingleton getInstance() {
        if (instance == null) {
            instance = new MethodLockSingleton();
        }
        return instance;
    }
}

/**
 * 
 * DoubleCheckLockSingleton
 * 
 * 
 * - First check will skip the lock after the object exists.
 * - The second check prevents two threads that are already waiting for the lock
 * from creating two objects.
 * 
 * volatile:
 * - ensures that changes to instance are visible across threads and prevents
 * problematic instruction reordering during object construction.
 * 
 * 
 * Advantages:
 * - Thread safe
 * - Better performance than MethodLockSingleton
 * - Lock is used only during initialization
 * 
 */
class DoubleCheckLockSingleton {
    private static volatile DoubleCheckLockSingleton instance;

    private DoubleCheckLockSingleton() {
    }

    public static DoubleCheckLockSingleton getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckLockSingleton.class) {
                if (instance == null) {
                    instance = new DoubleCheckLockSingleton();
                }
            }
        }
        return instance;
    }

}

/**
 * 
 * EagerSingleton
 * 
 * It allows creation of object immediately
 * 
 * 
 * Advantages:
 * - Thread safe
 * - Very simple to implement
 * - No synchronization required
 * 
 * 
 * Disadvantages:
 * - Instance is created even if nobody uses it
 * - Wastage of resource
 * 
 */
class EagerSingleton {
    private static final EagerSingleton instance = new EagerSingleton();

    private EagerSingleton() {
    }

    public static EagerSingleton getInstance() {
        return instance;
    }
}

/**
 * 
 * BillPughSingleton
 * 
 * How does it work?
 * 
 * - Singleton class loaded -> SingletonHolder not loaded -> No instance created
 * 
 * - On calling getInstance():
 * - getInstance() -> SingletonHolder -> instance created -> instance returned
 * 
 * - Java's class initialization mechanism is thread-safe, so we don't need
 * synchronized or volatile
 * 
 * Advantages:
 * - Lazy initialization
 * - Thread safe
 * - No synchronization overhead
 * - Simple and clean
 *
 * 
 */
class BillPughSingleton {
    private BillPughSingleton() {
    }

    private static class SingletonHolder {
        private static final BillPughSingleton instance = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance() {
        return SingletonHolder.instance;
    }
}

/**
 * 
 * EnumSingleton
 * 
 * It is implemented using enum.
 * 
 * - Java guarantees that each enum constant is instantiated only once, making
 * it thread-safe.
 * - It also handles serialization safely prevents creating another enum
 * instance through normal reflection
 * 
 * 
 * - Usage:
 * 
 * - EnumSingleton singleton1 = EnumSingleton.instance;
 * - EnumSingleton singleton2 = EnumSingleton.instance;
 * 
 * - System.out.println(singleton1 == singleton2); // true
 * 
 * - singleton1.doSomething();
 * 
 */
enum EnumSingleton {
    instance;

    public void doSomething() {
        System.out.println("Doing something...");
    }
}

public class Singleton {
    public static void main(String[] args) {

    }
}
