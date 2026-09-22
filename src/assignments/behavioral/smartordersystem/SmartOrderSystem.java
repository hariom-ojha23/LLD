package assignments.behavioral.smartordersystem;

import assignments.behavioral.smartordersystem.chainofresponsibility.BotHandler;
import assignments.behavioral.smartordersystem.chainofresponsibility.ExecutiveHandler;
import assignments.behavioral.smartordersystem.chainofresponsibility.ManagerHandler;
import assignments.behavioral.smartordersystem.chainofresponsibility.SupportHandler;
import assignments.behavioral.smartordersystem.enums.SupportRequestType;
import assignments.behavioral.smartordersystem.interfaces.OrderObserver;
import assignments.behavioral.smartordersystem.models.Order;
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

        /**
         * Created ne order
         */
        Order order = new Order();

        /**
         * Adding observers for the order
         */
        order.addObserver(userApp);
        order.addObserver(restaurantApp);
        order.addObserver(deliveryApp);

        System.out.println("");

        /**
         * Changing order state
         */
        order.confirm();
        System.out.println("");

        order.preparing();
        System.out.println("");

        order.outForDelivery();
        System.out.println("");

        order.delivered();
        System.out.println("");

        /**
         * Support request
         */
        SupportHandler botHandler = new BotHandler();
        SupportHandler managerHandler = new ManagerHandler();
        SupportHandler executivHandler = new ExecutiveHandler();

        /**
         * Setting handlers chain
         */
        botHandler.setNextSupportHandler(executivHandler);
        executivHandler.setNextSupportHandler(managerHandler);

        try {
            botHandler.handleRequest(SupportRequestType.ORDER, "Order query");
            System.out.println("");

            botHandler.handleRequest(SupportRequestType.PAYMENT, "Payment query");
            System.out.println("");

            botHandler.handleRequest(SupportRequestType.REFUND, "Refund query");
            System.out.println("");

            botHandler.handleRequest(null, "Refund query");
            System.out.println("");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
