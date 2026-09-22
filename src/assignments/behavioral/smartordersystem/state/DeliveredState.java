package assignments.behavioral.smartordersystem.state;

import assignments.behavioral.smartordersystem.interfaces.OrderState;

public class DeliveredState implements OrderState {
    @Override
    public void confirm() {
        System.out.println("Order is already delivered");
    }

    @Override
    public void preparing() {
        System.out.println("Order is already delivered");
    }

    @Override
    public void outForDelivery() {
        System.out.println("Order is already delivered");
    }

    @Override
    public void delivered() {
        System.out.println("Order is already delivered");
    }

    @Override
    public void cancelled() {
        System.out.println("Cannot cancel already deliverd order");
    }
}
