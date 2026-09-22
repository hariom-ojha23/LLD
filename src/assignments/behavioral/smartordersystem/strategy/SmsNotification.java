package assignments.behavioral.smartordersystem.strategy;

import assignments.behavioral.smartordersystem.interfaces.NotificationStrategy;

public class SmsNotification implements NotificationStrategy {
    @Override
    public void sendNotification(String receiver, String message) {
        System.out.println(String.format("SMS to %s: %s", receiver, message));
    }
}
