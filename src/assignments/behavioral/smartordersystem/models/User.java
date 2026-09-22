package assignments.behavioral.smartordersystem.models;

import assignments.behavioral.smartordersystem.interfaces.NotificationStrategy;

public class User {
    private final String name;
    private NotificationStrategy notificationStrategy;

    public User(String name, NotificationStrategy notificationStrategy) {
        this.name = name;
        this.notificationStrategy = notificationStrategy;
    }

    public String getName() {
        return name;
    }

    public NotificationStrategy getNotificationStrategy() {
        return notificationStrategy;
    }

    public void setNotificationStrategy(NotificationStrategy strategy) {
        this.notificationStrategy = strategy;
    }
}
