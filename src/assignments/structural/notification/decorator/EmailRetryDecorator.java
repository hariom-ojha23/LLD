package assignments.structural.notification.decorator;

import assignments.structural.notification.builder.NotificationRequest;
import assignments.structural.notification.interfaces.EmailNotification;

public class EmailRetryDecorator implements EmailNotification {
    private final EmailNotification notifer;
    private final int maxRetries;

    public EmailRetryDecorator(EmailNotification emailNotifier, int maxRetries) {
        this.notifer = emailNotifier;
        this.maxRetries = maxRetries;
    }

    public void sendNotification(NotificationRequest request) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                this.notifer.sendNotification(request);
                return;
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + ": failed");
            }
        }
    }
}
