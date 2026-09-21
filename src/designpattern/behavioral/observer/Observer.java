package designpattern.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Observer
 * 
 * Observer pattern defines a one-to-many relationship between objects.
 * When one object changes its state, all the other objects that are
 * depending on it are notified and updated automatically.
 * 
 * Main Idea:
 * One object publishes a change, and multiple objects receive notifications.
 * 
 * 
 * Components:
 * 
 * 1. Subject:
 * The object that publishes changes.
 * It maintains a list of observers and notifies them when its state changes.
 * 
 * 2. Observer:
 * The object that receives notifications.
 * It has an update method that is called by the subject.
 * 
 * 3. ConcreteSubject:
 * The actual implementation of the subject.
 * 
 * 4. ConcreteObserver:
 * The actual implementation of the observer.
 * 
 * 
 * When to use:
 * 
 * - One object state change should notify other objects
 * - The number of dependants can change dynamically
 * - Event/Notification system
 * - There is one-to-many relationship
 * 
 * 
 */

/**
 * ProductObserver
 */
interface ProductObserver {
    public void update();
}

/**
 * 
 * EmailNotification
 * 
 * EmailNotification IS-A ProductObserver
 * 
 */
class EmailNotification implements ProductObserver {
    @Override
    public void update() {
        System.out.println("Sending email notification...");
    }
}

/**
 * 
 * SmsNotification
 * 
 * SmsNotification IS-A ProductObserver
 * 
 */
class SmsNotification implements ProductObserver {
    @Override
    public void update() {
        System.out.println("Sending SMS notification...");
    }
}

/**
 * 
 * PushNotification
 * 
 * PushNotification IS-A ProductObserver
 * 
 */
class PushNotification implements ProductObserver {
    @Override
    public void update() {
        System.out.println("Sending push notification...");
    }
}

/**
 * 
 * Product
 * 
 * Product HAS-A List of ProductObserver
 * 
 */
class Product {
    private final List<ProductObserver> observers = new ArrayList<>();

    public void addObserver(ProductObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ProductObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (ProductObserver observer : observers) {
            observer.update();
        }
    }

    public void makeProductAvailable() {
        System.out.println("Product is available now");
        notifyObservers();
    }
}

public class Observer {
    public static void main(String[] args) {
        ProductObserver smsObserver = new SmsNotification();
        ProductObserver emailObserver = new EmailNotification();
        ProductObserver pushObserver = new PushNotification();

        Product product = new Product();

        product.addObserver(smsObserver);
        product.addObserver(emailObserver);
        product.addObserver(pushObserver);

        product.makeProductAvailable();
    }
}
