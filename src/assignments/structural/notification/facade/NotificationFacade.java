package assignments.structural.notification.facade;

import assignments.structural.notification.decorator.EmailLogDecorator;
import assignments.structural.notification.decorator.EmailRetryDecorator;
import assignments.structural.notification.decorator.PushLogDecorator;
import assignments.structural.notification.decorator.PushRetryDecorator;
import assignments.structural.notification.decorator.SmsLogDecorator;
import assignments.structural.notification.decorator.SmsRetryDecorator;
import assignments.structural.notification.interfaces.EmailNotification;
import assignments.structural.notification.builder.NotificationRequest;
import assignments.structural.notification.interfaces.PushNotification;
import assignments.structural.notification.interfaces.SmsNotification;
import assignments.structural.notification.priority.NotificationChannel;
import assignments.structural.notification.priority.NotificationChannelSelector;
import assignments.structural.notification.priority.NotificationPriority;

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
    private final NotificationChannelSelector channelSelector;

    public NotificationFacade(
            EmailNotification emailNotifier,
            SmsNotification smsNotifier,
            PushNotification pushNotifier,
            NotificationChannelSelector channelSelector,
            int maxRetryCount) {
        this.emailNotifier = new EmailLogDecorator(new EmailRetryDecorator(emailNotifier, maxRetryCount));
        this.smsNotifier = new SmsLogDecorator(new SmsRetryDecorator(smsNotifier, maxRetryCount));
        this.pushNotifier = new PushLogDecorator(new PushRetryDecorator(pushNotifier, maxRetryCount));
        this.channelSelector = channelSelector;
    }

    public void sendNotification(NotificationRequest request, NotificationPriority priority) {

        NotificationChannel[] channels = channelSelector.selectChannels(priority);

        for (NotificationChannel channel : channels) {

            switch (channel) {

                case EMAIL:
                    sendEmail(request);
                    break;

                case SMS:
                    sendSms(request);
                    break;

                case PUSH:
                    sendPush(request);
                    break;
            }
        }
    }

    /**
     * Method overloading
     */
    private void sendEmail(NotificationRequest request) {
        emailNotifier.sendNotification(request);
    }

    private void sendSms(NotificationRequest request) {
        smsNotifier.sendNotification(request);
    }

    private void sendPush(NotificationRequest request) {
        pushNotifier.sendNotification(request);
    }
}
