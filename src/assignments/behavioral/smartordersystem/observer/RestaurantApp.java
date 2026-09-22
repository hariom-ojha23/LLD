package assignments.behavioral.smartordersystem.observer;

import assignments.behavioral.smartordersystem.interfaces.NotificationStrategy;
import assignments.behavioral.smartordersystem.interfaces.OrderObserver;

public class RestaurantApp implements OrderObserver {
    private final NotificationStrategy strategy;

    public RestaurantApp(NotificationStrategy notificationStrategy) {
        strategy = notificationStrategy;
    }

    @Override
    public void update(String message) {
        strategy.sendNotification("Restaurant app", message);
    }
}
