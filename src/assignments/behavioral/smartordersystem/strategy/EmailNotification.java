package assignments.behavioral.smartordersystem.strategy;

import assignments.behavioral.smartordersystem.interfaces.NotificationStrategy;

public class EmailNotification implements NotificationStrategy {
    @Override
    public void sendNotification(String receiver, String message) {
        System.out.println(String.format("Email to %s: %s", receiver, message));
    }
}
