package assignments.behavioral.smartordersystem.state;

import assignments.behavioral.smartordersystem.interfaces.OrderState;
import assignments.behavioral.smartordersystem.models.Order;

public class PreparingState implements OrderState {
    private final Order order;

    public PreparingState(Order order) {
        this.order = order;
    }

    @Override
    public void confirm() {
        System.out.println("Order is already confirmed and in preparation");
    }

    @Override
    public void preparing() {
        System.out.println("Order is already in preparation");
    }

    @Override
    public void outForDelivery() {
        order.changeState(new OutForDeliveryState(order), "Order is out for delivery");
    }

    @Override
    public void delivered() {
        System.out.println("Preparing order. Cannot deliver");
    }

    @Override
    public void cancelled() {
        order.changeState(new CanceledState(), "Order is canceled");
    }

}
