package assignments.structural.notification.decorator;

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

    public void sendNotification(String to, String subject, String message) {
        System.out.println("Sending email notification");

        this.notifier.sendNotification(to, subject, message);

        String successLog = String.format("To: %s\nSubject: %s\nMessage: %s", to, subject, message);
        System.out.println(successLog);
    }
}
