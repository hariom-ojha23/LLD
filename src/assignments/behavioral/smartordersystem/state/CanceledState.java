package assignments.behavioral.smartordersystem.state;

import assignments.behavioral.smartordersystem.interfaces.OrderState;

public class CanceledState implements OrderState {

    @Override
    public void confirm() {
        System.out.println("Cannot confirm. Order is already canceled");
    }

    @Override
    public void preparing() {
        System.out.println("Cannot prepare. Order is already canceled");
    }

    @Override
    public void outForDelivery() {
        System.out.println("Cannot initiate delivery. Order is already canceled");
    }

    @Override
    public void delivered() {
        System.out.println("Cannot deliver. Order is already canceled");
    }

    @Override
    public void cancelled() {
        System.out.println("Cannot canceled. Order is already canceled");
    }
}
