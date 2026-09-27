package assignments.structural.notification.interfaces;

import assignments.structural.notification.builder.NotificationRequest;

public interface SmsNotification {
    public void sendNotification(NotificationRequest request);
}
