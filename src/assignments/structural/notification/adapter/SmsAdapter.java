package assignments.structural.notification.adapter;

import assignments.structural.notification.interfaces.SmsNotification;
import assignments.structural.notification.notificationapis.SmsNotificationApi;

/**
 * 
 * SmsAdapter
 * 
 * SmsAdapter IS-A SmsNotification
 * SmsAdapter HAS-A SmsNotificationApi
 * 
 */
public class SmsAdapter implements SmsNotification {
    private final SmsNotificationApi smsService;

    public SmsAdapter(SmsNotificationApi smsService) {
        this.smsService = smsService;
    }

    public void sendNotification(String mobileNumber, String message) {
        smsService.sendSmsNotification(mobileNumber, message);
    }
}
