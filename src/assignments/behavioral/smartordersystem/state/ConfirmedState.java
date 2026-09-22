package assignments.behavioral.smartordersystem.state;

import assignments.behavioral.smartordersystem.interfaces.OrderState;
import assignments.behavioral.smartordersystem.models.Order;

public class ConfirmedState implements OrderState {
    private final Order order;

    public ConfirmedState(Order order) {
        this.order = order;
    }

    @Override
    public void confirm() {
        System.out.println("Order is already confirmed");
    }

    @Override
    public void preparing() {
        order.changeState(new PreparingState(order), "Order is being prepared");
    }

    @Override
    public void outForDelivery() {
        System.out.println("Order is not prepared. Cannot out for delivery");
    }

    @Override
    public void delivered() {
        System.out.println("Order is not prepared. Cannot deliver");
    }

    @Override
    public void cancelled() {
        order.changeState(new CanceledState(), "Order is canceled");
    }
}
