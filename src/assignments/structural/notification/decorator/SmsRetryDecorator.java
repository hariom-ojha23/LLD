package assignments.structural.notification.decorator;

import assignments.structural.notification.builder.NotificationRequest;
import assignments.structural.notification.interfaces.SmsNotification;

public class SmsRetryDecorator implements SmsNotification {
    private final SmsNotification notifer;
    private final int maxRetries;

    public SmsRetryDecorator(SmsNotification smsNotifier, int maxRetries) {
        this.notifer = smsNotifier;
        this.maxRetries = maxRetries;
    }

    public void sendNotification(NotificationRequest request) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                if (attempt == 3) {
                    this.notifer.sendNotification(request);
                    return;
                }

                throw new RuntimeException("Attempt failed");
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + ": failed");
            }
        }
    }
}
