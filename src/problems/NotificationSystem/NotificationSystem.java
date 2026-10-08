package problems.NotificationSystem;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * 
 * NotificationSystem
 * 
 * Functional Requrement:
 * 
 * - Support multiple type of notification (SMS, Push, Email)
 * - Async submission of notification to handlers
 * - Retry failed notifications - Decorator
 * - Support priority of notifications
 * - Support different channels for notification
 * - Easy to add new channels - factory
 * - Support user preferences
 * 
 * 
 * Non-Functional Requirement:
 * 
 * - Thread safety
 * - Async submission
 * - Follow SOLID principles
 * - Extensible
 * 
 * 
 * Core Classes/Interface/Enums
 * 
 * NotificationType
 * Notification
 * NotificationService
 * NotificationChannel
 * NotificationChannelFactory
 * UserPreferenceService
 * NotificationDispatcher
 * 
 */

enum NotificationType {
    SMS,
    PUSH,
    EMAIL
}

enum NotificationPriority {
    LOW,
    MEDIUM,
    HIGH
}

interface NotificationChannel {
    public void sendNotification(Notification notification);
}

/**
 * 
 * Notification
 * 
 * Design pattern: Builder
 * 
 */
class Notification {
    private final int id;
    private final String userId;
    private final String recepient;
    private final String message;
    private final String subject;
    private final NotificationPriority priority;

    private Notification(NotificationBuilder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.recepient = builder.recepient;
        this.message = builder.message;
        this.subject = builder.subject;
        this.priority = builder.priority;
    }

    public static class NotificationBuilder {
        private int id;
        private String userId;
        private String recepient;
        private String message;
        private String subject;
        private NotificationPriority priority;

        public NotificationBuilder(int id) {
            this.id = id;
        }

        public NotificationBuilder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public NotificationBuilder setReceipent(String recepient) {
            this.recepient = recepient;
            return this;
        }

        public NotificationBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public NotificationBuilder setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public NotificationBuilder setPriority(NotificationPriority priority) {
            this.priority = priority;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public String getReceipent() {
        return recepient;
    }

    public String getSubject() {
        return subject;
    }

    public NotificationPriority getPriority() {
        return priority;
    }
}

class SmsNotification implements NotificationChannel {

    @Override
    public void sendNotification(Notification notification) {
        System.out.println(
                String.format(
                        "SMS Notification:\nReceipent: %s\nMessage: %s\n",
                        notification.getReceipent(), notification.getMessage()));
    }

}

class PushNotification implements NotificationChannel {

    @Override
    public void sendNotification(Notification notification) {
        System.out.println(
                String.format(
                        "Push Notification:\nReceipent: %s\nMessage: %s\n",
                        notification.getReceipent(), notification.getMessage()));
    }

}

class EmailNotification implements NotificationChannel {

    @Override
    public void sendNotification(Notification notification) {
        System.out.println(
                String.format(
                        "Email Notification:\nReceipent: %s\nSubject: %s\nMessage: %s\n",
                        notification.getReceipent(), notification.getSubject(), notification.getMessage()));
    }

}

/**
 * 
 * NotificationDecorator
 */
abstract class NotificationDecorator implements NotificationChannel {
    NotificationChannel channel;

    public NotificationDecorator(NotificationChannel channel) {
        this.channel = channel;
    }
}

/**
 * 
 * RetryDecorator
 * 
 * Design pattern: Decorator
 * 
 */
class RetryDecorator extends NotificationDecorator {
    private final int maxAttempt;
    private final Random random = new Random();

    public RetryDecorator(NotificationChannel channel, int maxAttempt) {
        super(channel);
        this.maxAttempt = maxAttempt;
    }

    @Override
    public void sendNotification(Notification notification) {
        int attempt = 1;

        while (attempt <= maxAttempt) {
            try {
                int randomNumber = random.nextInt(3) + 1;

                if (randomNumber <= 2) {
                    throw new RuntimeException("Failed to send notification");
                }

                channel.sendNotification(notification);
                return;
            } catch (Exception e) {
                System.out.println("Attempt Failed: " + attempt);
                attempt++;
            }
        }
    }
}

/**
 * 
 * NotificationService
 */
class NotificationService {
    private final UserPreferenceService preferenceService;
    private final NotificationDispatcher dispatcher;

    public NotificationService(UserPreferenceService preferenceService, NotificationDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.preferenceService = preferenceService;
    }

    /**
     * 
     * Get user preferneces
     * if no preferences, do not send notification
     * 
     * Send notification to dispatcher
     * 
     */
    public void sendNotification(Notification notification) {
        List<NotificationType> preferences = preferenceService.getUserPreferences(notification.getUserId());

        // validate preferences
        if (preferences == null) {
            System.out.println(String.format("User %s do not have any preference", notification.getUserId()));
            return;
        }

        // dispatch notification
        this.dispatcher.submit(notification, preferences);
    }
}

/**
 * 
 * UserPreferenceService
 */
class UserPreferenceService {
    private final Map<String, List<NotificationType>> preferenceMap = new ConcurrentHashMap<>();

    public List<NotificationType> getUserPreferences(String userId) {
        return preferenceMap.get(userId);
    }

    public void setUserPreferences(String userId, List<NotificationType> preferences) {
        preferenceMap.put(userId, List.copyOf(preferences));
    }
}

/**
 * 
 * NotificationChannelFactory
 * 
 * Design pattern: Simple Factory
 * 
 */
class NotificationChannelFactory {
    private final static Map<NotificationType, Supplier<NotificationChannel>> channels = Map.of(
            NotificationType.SMS,
            () -> new RetryDecorator(new SmsNotification(), 3),

            NotificationType.PUSH,
            () -> new RetryDecorator(new PushNotification(), 3),

            NotificationType.EMAIL,
            () -> new RetryDecorator(new EmailNotification(), 3));

    public static NotificationChannel getNotificationChannel(NotificationType type) {
        Supplier<NotificationChannel> supplier = channels.get(type);

        if (supplier == null) {
            throw new IllegalArgumentException("Unsupported notification type: " + type);
        }

        return supplier.get();
    }
}

/**
 * 
 * NotificationDispatcher
 * 
 * Async submission
 * 
 */
class NotificationDispatcher {
    ExecutorService threadPool = Executors.newFixedThreadPool(3);

    public void submit(Notification notification, List<NotificationType> preferences) {
        threadPool.submit(() -> {
            // Send notification job
            for (NotificationType preference : preferences) {
                NotificationChannel channel = NotificationChannelFactory.getNotificationChannel(preference);
                channel.sendNotification(notification);
            }
        });
    }

    public void shutdown() {
        threadPool.shutdown();
    }
}

/**
 * 
 * NotificationSystem
 */
public class NotificationSystem {

    public static void main(String[] args) {

        // Create services
        UserPreferenceService preferenceService = new UserPreferenceService();

        NotificationDispatcher dispatcher = new NotificationDispatcher();

        NotificationService notificationService = new NotificationService(
                preferenceService,
                dispatcher);

        // Set user preferences
        preferenceService.setUserPreferences(
                "user-1",
                List.of(
                        NotificationType.EMAIL,
                        NotificationType.SMS,
                        NotificationType.PUSH));

        // Create notification
        Notification notification = new Notification.NotificationBuilder(1)
                .setUserId("user-1")
                .setReceipent("9876543210")
                .setSubject("Order Confirmation")
                .setMessage("Your order has been placed successfully.")
                .setPriority(NotificationPriority.HIGH)
                .build();

        // Send notification
        notificationService.sendNotification(notification);

        // Shutdown dispatcher
        dispatcher.shutdown();
    }
}