package assignments.structural.notification.decorator;

import assignments.structural.notification.builder.NotificationRequest;
import assignments.structural.notification.interfaces.SmsNotification;

/**
 * 
 * SmsLogDecorator
 * 
 * SmsLogDecorator IS-A SmsNotification
 * SmsLogDecorator HAS-A SmsNotification
 * 
 */
public class SmsLogDecorator implements SmsNotification {
    private final SmsNotification notifier;

    public SmsLogDecorator(SmsNotification smsNotifier) {
        notifier = smsNotifier;
    }

    public void sendNotification(NotificationRequest request) {
        System.out.println("Sending sms notification");

        notifier.sendNotification(request);

        String successLog = String.format("Mobile Number: (%s)\nMessage: %s", request.getTo(),
                request.getMessage());

        System.out.println(successLog + '\n');
    }
}
