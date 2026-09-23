package assignments.behavioral.smartordersystem.services;

import assignments.behavioral.smartordersystem.interfaces.OrderObserver;
import assignments.behavioral.smartordersystem.models.Order;

public class OrderService {
    private Order order;

    public OrderService() {
    }

    public void createOrder() {
        this.order = new Order();
        System.out.println("Order created");
    }

    public Order getOrder() {
        return order;
    }

    public void confirmOrder() {
        order.confirm();
    }

    public void prepareOrder() {
        order.preparing();
    }

    public void outForDelivery() {
        order.outForDelivery();
    }

    public void deliverOrder() {
        order.delivered();
    }

    public void cancelOrder() {
        order.cancelled();
    }

    public void addObserver(OrderObserver observer) {
        order.addObserver(observer);
    }

    public void removeObserver(OrderObserver observer) {
        order.removeObserver(observer);
    }
}
