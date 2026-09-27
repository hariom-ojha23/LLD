package assignments.structural.notification.priority;

public class NotificationChannelSelector {

    public NotificationChannel[] selectChannels(NotificationPriority priority) {

        return switch (priority) {
            case LOW -> new NotificationChannel[] { 
                NotificationChannel.EMAIL 
            };

            case MEDIUM -> new NotificationChannel[] { 
                NotificationChannel.EMAIL, NotificationChannel.PUSH 
            };

            case HIGH -> new NotificationChannel[] { 
                NotificationChannel.PUSH, NotificationChannel.SMS 
            };

            case CRITICAL -> new NotificationChannel[] { 
                NotificationChannel.SMS, NotificationChannel.PUSH, NotificationChannel.EMAIL 
            };
        };
    }
}