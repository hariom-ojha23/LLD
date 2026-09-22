package assignments.structural.notification;

import assignments.structural.notification.notificationapis.EmailNotificationApi;
import assignments.structural.notification.notificationapis.PushNotificationApi;
import assignments.structural.notification.notificationapis.SmsNotificationApi;
import assignments.structural.notification.adapter.EmailAdapter;
import assignments.structural.notification.adapter.PushAdapter;
import assignments.structural.notification.adapter.SmsAdapter;
import assignments.structural.notification.facade.NotificationFacade;
import assignments.structural.notification.interfaces.EmailNotification;
import assignments.structural.notification.interfaces.PushNotification;
import assignments.structural.notification.interfaces.SmsNotification;

public class Notification {
    public static void main(String[] args) {
        String emailSubject = "Test subject";
        String message = "This is a test notification";
        String emailReceipent = "hariomojha@email.com";
        String smsReceipent = "9988776655";
        int maxRetryCount = 3;

        EmailNotification emailAdapter = new EmailAdapter(new EmailNotificationApi());
        SmsNotification smsAdapter = new SmsAdapter(new SmsNotificationApi());
        PushNotification pushAdapter = new PushAdapter(new PushNotificationApi());

        NotificationFacade notificationFacade = new NotificationFacade(emailAdapter, smsAdapter, pushAdapter,
                maxRetryCount);

        System.out.println("");

        notificationFacade.sendNotification(emailReceipent, emailSubject, message);

        System.out.println("");

        notificationFacade.sendNotification(smsReceipent, message);

        System.out.println("");

        notificationFacade.sendNotification(message);
    }
}
