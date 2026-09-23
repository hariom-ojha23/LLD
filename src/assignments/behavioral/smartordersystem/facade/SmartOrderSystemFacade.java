package assignments.behavioral.smartordersystem.facade;

import assignments.behavioral.smartordersystem.enums.SupportRequestType;
import assignments.behavioral.smartordersystem.interfaces.OrderObserver;
import assignments.behavioral.smartordersystem.services.OrderService;
import assignments.behavioral.smartordersystem.services.SupportService;

public class SmartOrderSystemFacade {
    private final OrderService orderService;
    private final SupportService supportService;

    public SmartOrderSystemFacade() {
        this.orderService = new OrderService();
        this.supportService = new SupportService();
    }

    public void createOrder() {
        orderService.createOrder();
    }

    public void prepareOrder() {
        orderService.prepareOrder();
    }

    public void confirmOrder() {
        orderService.confirmOrder();
    }

    public void orderOutForDelivery() {
        orderService.outForDelivery();
    }

    public void deliverOrder() {
        orderService.deliverOrder();
    }

    public void cancelOrder() {
        orderService.cancelOrder();
    }

    public void addOrderObserver(OrderObserver orderObserver) {
        orderService.addObserver(orderObserver);
    }

    public void handleSupportRequest(SupportRequestType type, String message) {
        supportService.handleRequest(type, message);
    }
}
