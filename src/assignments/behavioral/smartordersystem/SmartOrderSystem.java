package assignments.behavioral.smartordersystem;

import assignments.behavioral.smartordersystem.enums.SupportRequestType;
import assignments.behavioral.smartordersystem.facade.SmartOrderSystemFacade;
import assignments.behavioral.smartordersystem.interfaces.OrderObserver;
import assignments.behavioral.smartordersystem.models.User;
import assignments.behavioral.smartordersystem.observer.DeliveryApp;
import assignments.behavioral.smartordersystem.observer.RestaurantApp;
import assignments.behavioral.smartordersystem.observer.UserApp;
import assignments.behavioral.smartordersystem.strategy.EmailNotification;
import assignments.behavioral.smartordersystem.strategy.PushNotification;
import assignments.behavioral.smartordersystem.strategy.SmsNotification;

public class SmartOrderSystem {
    public static void main(String[] args) {

        /**
         * user with their choice of Notification
         */
        User user1 = new User("Aman", new PushNotification());

        OrderObserver userApp = new UserApp(user1);
        OrderObserver restaurantApp = new RestaurantApp(new EmailNotification());
        OrderObserver deliveryApp = new DeliveryApp(new SmsNotification());

        SmartOrderSystemFacade system = new SmartOrderSystemFacade();

        system.createOrder();

        system.addOrderObserver(userApp);
        system.addOrderObserver(restaurantApp);
        system.addOrderObserver(deliveryApp);

        system.confirmOrder();
        System.out.println("");

        system.prepareOrder();
        System.out.println("");

        system.orderOutForDelivery();
        System.out.println("");

        system.deliverOrder();
        System.out.println("");

        try {
            system.handleSupportRequest(SupportRequestType.ORDER, "Order query");
            System.out.println("");

            system.handleSupportRequest(SupportRequestType.PAYMENT, "Payment query");
            System.out.println("");

            system.handleSupportRequest(SupportRequestType.REFUND, "Refund query");
            System.out.println("");

            system.handleSupportRequest(null, "Refund query");
            System.out.println("");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
