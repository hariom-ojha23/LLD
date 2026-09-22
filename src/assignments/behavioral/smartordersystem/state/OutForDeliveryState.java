package assignments.behavioral.smartordersystem.state;

import assignments.behavioral.smartordersystem.interfaces.OrderState;
import assignments.behavioral.smartordersystem.models.Order;

public class OutForDeliveryState implements OrderState {
    private final Order order;

    public OutForDeliveryState(Order order) {
        this.order = order;
    }

    @Override
    public void confirm() {
        System.out.println("Order is already confirmed and out for delivery");
    }

    @Override
    public void preparing() {
        System.out.println("Order is already prepared and out for delivery");
    }

    @Override
    public void outForDelivery() {
        System.out.println("Order is already out for delivery");
    }

    @Override
    public void delivered() {
        order.changeState(new DeliveredState(), "Order is delivered");
    }

    @Override
    public void cancelled() {
        order.changeState(new CanceledState(), "Order is canceled");
    }
}
