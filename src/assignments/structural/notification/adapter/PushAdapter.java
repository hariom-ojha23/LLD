package assignments.structural.notification.adapter;

import assignments.structural.notification.interfaces.PushNotification;
import assignments.structural.notification.notificationapis.PushNotificationApi;

/**
 * 
 * PushAdapter
 * 
 * PushAdapter IS-A PushNotification
 * PushAdapter HAS-A PushNotificationApi
 */
public class PushAdapter implements PushNotification {
    private final PushNotificationApi pushService;

    public PushAdapter(PushNotificationApi pushService) {
        this.pushService = pushService;
    }

    public void sendNotification(String message) {
        pushService.sendPushNotification(message);
    }
}
