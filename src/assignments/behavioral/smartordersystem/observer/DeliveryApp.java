package assignments.behavioral.smartordersystem.observer;

import assignments.behavioral.smartordersystem.interfaces.NotificationStrategy;
import assignments.behavioral.smartordersystem.interfaces.OrderObserver;

public class DeliveryApp implements OrderObserver {
    private final NotificationStrategy strategy;

    public DeliveryApp(NotificationStrategy notificationStrategy) {
        strategy = notificationStrategy;
    }

    @Override
    public void update(String message) {
        strategy.sendNotification("Delivery app", message);
    }
}
