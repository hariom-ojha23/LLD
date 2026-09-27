package assignments.structural.notification.decorator;

import assignments.structural.notification.builder.NotificationRequest;
import assignments.structural.notification.interfaces.EmailNotification;

/**
 * 
 * EmailLogDecorator
 * 
 * EmailLogDecorator IS-A EmailNotification
 * EmailLogDecorator HAS-A EmailNotification
 * 
 */
public class EmailLogDecorator implements EmailNotification {
    private final EmailNotification notifier;

    public EmailLogDecorator(EmailNotification emailNotifier) {
        notifier = emailNotifier;
    }

    public void sendNotification(NotificationRequest request) {
        System.out.println("Sending email notification");

        this.notifier.sendNotification(request);

        String successLog = String.format("To: %s\nSubject: %s\nMessage: %s", request.getTo(), request.getSubject(),
                request.getMessage());

        System.out.println(successLog);
    }
}
