package assignments.structural.notification.adapter;

import assignments.structural.notification.builder.NotificationRequest;
import assignments.structural.notification.interfaces.EmailNotification;
import assignments.structural.notification.notificationapis.EmailNotificationApi;

/**
 * 
 * EmailAdapter
 * 
 * EmailAdapter IS-A EmailNotification
 * EmailAdapter HAS-A EmailNotificationApi
 * 
 */
public class EmailAdapter implements EmailNotification {
    private final EmailNotificationApi notificationService;

    public EmailAdapter(EmailNotificationApi notificationService) {
        this.notificationService = notificationService;
    }

    public void sendNotification(NotificationRequest request) {
        notificationService.sendEmailNotification(request.getTo(), request.getSubject(), request.getMessage());
    }
}
