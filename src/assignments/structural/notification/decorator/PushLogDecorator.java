package assignments.structural.notification.decorator;

import assignments.structural.notification.builder.NotificationRequest;
import assignments.structural.notification.interfaces.PushNotification;

/**
 * 
 * PushLogDecorator
 * 
 * PushLogDecorator IS-A PushNotification
 * PushLogDecorator HAS-A PushNotification
 * 
 */
public class PushLogDecorator implements PushNotification {
    private final PushNotification notifier;

    public PushLogDecorator(PushNotification pushNotifier) {
        notifier = pushNotifier;
    }

    public void sendNotification(NotificationRequest request) {
        System.out.println("Sending push notification");

        notifier.sendNotification(request);

        System.out.println("Message: " + request.getMessage() + '\n');
    }
}
