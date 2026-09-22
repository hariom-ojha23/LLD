package assignments.behavioral.smartordersystem.observer;

import assignments.behavioral.smartordersystem.interfaces.OrderObserver;
import assignments.behavioral.smartordersystem.models.User;

public class UserApp implements OrderObserver {
    private final User user;

    public UserApp(User user) {
        this.user = user;
    }

    public void update(String message) {
        user.getNotificationStrategy().sendNotification(user.getName(), message);
    }
}
