package assignments.structural.notification.facade;

import assignments.structural.notification.decorator.EmailLogDecorator;
import assignments.structural.notification.decorator.EmailRetryDecorator;
import assignments.structural.notification.decorator.PushLogDecorator;
import assignments.structural.notification.decorator.PushRetryDecorator;
import assignments.structural.notification.decorator.SmsLogDecorator;
import assignments.structural.notification.decorator.SmsRetryDecorator;
import assignments.structural.notification.interfaces.EmailNotification;
import assignments.structural.notification.interfaces.PushNotification;
import assignments.structural.notification.interfaces.SmsNotification;

/**
 * 
 * NotificationFacade
 * 
 * NotificationFacade HAS-A EmailNotification
 * NotificationFacade HAS-A SmsNotification
 * NotificationFacade HAS-A PushNotification
 * 
 */
public class NotificationFacade {
    private final EmailNotification emailNotifier;
    private final SmsNotification smsNotifier;
    private final PushNotification pushNotifier;

    public NotificationFacade(
            EmailNotification emailNotifier,
            SmsNotification smsNotifier,
            PushNotification pushNotifier,
            int maxRetryCount) {
        this.emailNotifier = new EmailLogDecorator(new EmailRetryDecorator(emailNotifier, maxRetryCount));
        this.smsNotifier = new SmsLogDecorator(new SmsRetryDecorator(smsNotifier, maxRetryCount));
        this.pushNotifier = new PushLogDecorator(new PushRetryDecorator(pushNotifier, maxRetryCount));
    }

    /**
     * Method overloading
     */
    public void sendNotification(String to, String subject, String message) {
        emailNotifier.sendNotification(to, subject, message);
    }

    public void sendNotification(String mobileNumber, String message) {
        smsNotifier.sendNotification(mobileNumber, message);
    }

    public void sendNotification(String message) {
        pushNotifier.sendNotification(message);
    }
}
