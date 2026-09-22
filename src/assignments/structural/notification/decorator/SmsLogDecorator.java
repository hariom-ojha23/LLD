package assignments.structural.notification.decorator;

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

    public void sendNotification(String mobileNumber, String message) {
        System.out.println("Sending sms notification");

        notifier.sendNotification(mobileNumber, message);

        String successLog = String.format("Mobile Number: (%s)\nMessage: %s", mobileNumber, message);
        System.out.println(successLog);

    }
}
