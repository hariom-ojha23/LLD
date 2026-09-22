package assignments.behavioral.smartordersystem.services;

import assignments.behavioral.smartordersystem.interfaces.NotificationStrategy;

public class NotificationService implements NotificationStrategy {
    private NotificationStrategy notificationStrategy;

    public NotificationService(NotificationStrategy strategy) {
        notificationStrategy = strategy;
    }

    public void setStrategy(NotificationStrategy strategy) {
        notificationStrategy = strategy;
    }

    @Override
    public void sendNotification(String receiver, String message) {
        notificationStrategy.sendNotification(receiver, message);
    }
}
