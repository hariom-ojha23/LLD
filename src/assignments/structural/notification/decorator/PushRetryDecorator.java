package assignments.structural.notification.decorator;

import assignments.structural.notification.interfaces.PushNotification;

public class PushRetryDecorator implements PushNotification {
    private final PushNotification notifer;
    private final int maxRetries;

    public PushRetryDecorator(PushNotification pushNotifier, int maxRetries) {
        this.notifer = pushNotifier;
        this.maxRetries = maxRetries;
    }

    public void sendNotification(String message) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                if (attempt == 1) {
                    throw new RuntimeException("Attempt failed");
                }

                this.notifer.sendNotification(message);
                return;
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + ": failed");
            }
        }
    }
}
