package assignments.structural.notification.interfaces;

import assignments.structural.notification.builder.NotificationRequest;

public interface PushNotification {
    public void sendNotification(NotificationRequest request);
}
