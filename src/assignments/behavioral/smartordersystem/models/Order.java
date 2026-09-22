package assignments.behavioral.smartordersystem.models;

import java.util.ArrayList;
import java.util.List;

import assignments.behavioral.smartordersystem.interfaces.OrderObserver;
import assignments.behavioral.smartordersystem.interfaces.OrderState;
import assignments.behavioral.smartordersystem.state.ConfirmedState;

public class Order {

    private OrderState orderState;
    private List<OrderObserver> observers = new ArrayList<>();

    public Order() {
        this.orderState = new ConfirmedState(this);
    }

    public void changeState(OrderState state, String message) {
        this.orderState = state;
        notifyObservers(message);
    }

    private void notifyObservers(String message) {
        for (OrderObserver observer : observers) {
            observer.update(message);
        }
    }

    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    public void confirm() {
        this.orderState.confirm();
    }

    public void preparing() {
        this.orderState.preparing();
    }

    public void outForDelivery() {
        this.orderState.outForDelivery();
    }

    public void delivered() {
        this.orderState.delivered();
    }

    public void cancelled() {
        this.orderState.cancelled();
    }

}
