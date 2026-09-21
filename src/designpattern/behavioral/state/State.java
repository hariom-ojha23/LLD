package designpattern.behavioral.state;

/**
 * 
 * State
 * 
 * State pattern is used to change the behavior of the object when its internal
 * state changes.
 * 
 * From the outside, it looks as if the class has changed.
 * 
 * 
 * Main Idea:
 * Encapsulate state specific behaviour in separate classes.
 * 
 * When to use state pattern:
 * 
 * - An object has multiple distinct state
 * - Its behavior changes depending on current state
 * - There are many if-else/switch statements
 * - To separate state specific behavior from business logic
 * 
 * 
 * Strategy vs State
 * 
 * Both patterns:
 * - Encapsulate behavior in separate objects.
 * - Use composition.
 * - Context delegates behavior to another object.
 * 
 * Strategy:
 * - Client/context chooses the strategy.
 * - Strategies represent interchangeable algorithms/behaviors.
 * - The strategy usually does not control the lifecycle of the context.
 * - Switching strategies is generally a choice of the client/application.
 * 
 * State:
 * - Context's current state determines its behavior.
 * - States represent different stages/conditions of the context.
 * - States can trigger transitions to other states.
 * - State transitions are part of the object's lifecycle.
 * 
 * 
 */

interface OrderState {
    public void created();

    public void confirm();

    public void cancel();

    public void ship();

    public void deliver();
}

/**
 *
 * Order
 *
 * Order is the Context.
 *
 * Order HAS-A OrderState.
 *
 * The Order delegates operations to its current state.
 *
 */
class Order {
    private OrderState orderState;

    public Order() {
        this.orderState = new CreatedState(this);
    }

    protected void setState(OrderState newState) {
        this.orderState = newState;
    }

    public void confirm() {
        orderState.confirm();
    }

    public void cancel() {
        orderState.cancel();
    }

    public void ship() {
        orderState.ship();
    }

    public void deliver() {
        orderState.deliver();
    }
}

/**
 *
 * CreatedState
 *
 * CreatedState IS-A OrderState.
 *
 * CreatedState represents an Order that has been created
 * but has not yet been confirmed.
 *
 */

class CreatedState implements OrderState {
    /**
     * State HAS-A Order.
     *
     * The Order reference is required to change the
     * Context's current state.
     */
    private final Order order;

    public CreatedState(Order order) {
        System.out.println("Order created");
        this.order = order;
    }

    @Override
    public void created() {
        System.out.println("Order is already created");
    }

    @Override
    public void confirm() {
        System.out.println("Order confirmed");
        order.setState(new ConfirmedState(order));
    }

    @Override
    public void cancel() {
        System.out.println("Order canceled");
        order.setState(new CanceledState());
    }

    @Override
    public void ship() {
        System.out.println("Cannot ship a created order until it is confirmed");
    }

    @Override
    public void deliver() {
        System.out.println("Cannot deliver a created order until it is confirmed and shipped");
    }
}

/**
 *
 * ConfirmedState
 *
 * ConfirmedState IS-A OrderState.
 *
 * Represents an Order that has been confirmed
 * and is ready to be shipped.
 *
 */
class ConfirmedState implements OrderState {
    private final Order order;

    public ConfirmedState(Order order) {
        this.order = order;
    }

    @Override
    public void created() {
        System.out.println("Cannot create order once it is confirmed");
    }

    @Override
    public void confirm() {
        System.out.println("Order is already confirmed");
    }

    @Override
    public void cancel() {
        System.out.println("Order canceled");
        order.setState(new CanceledState());
    }

    @Override
    public void ship() {
        System.out.println("Order shipped");
        order.setState(new ShippedState(order));
    }

    @Override
    public void deliver() {
        System.out.println("Cannot deliver a order until it is shipped");
    }
}

/**
 *
 * CanceledState
 *
 * CanceledState IS-A OrderState.
 *
 * CanceledState is a terminal state.
 * No further valid state transitions are allowed.
 *
 */
class CanceledState implements OrderState {
    public CanceledState() {
    }

    @Override
    public void created() {
        System.out.println("Cannot create a canceled order");
    }

    @Override
    public void confirm() {
        System.out.println("Cannot confirm a canceled order");
    }

    @Override
    public void cancel() {
        System.out.println("Order is already canceled");
    }

    @Override
    public void ship() {
        System.out.println("Cannot ship a canceled order");

    }

    @Override
    public void deliver() {
        System.out.println("Cannot deliver a canceled order");
    }

}

/**
 *
 * ShippedState
 *
 * ShippedState IS-A OrderState.
 *
 * Represents an Order that has been shipped
 * and can now be delivered.
 *
 */
class ShippedState implements OrderState {
    private final Order order;

    public ShippedState(Order order) {
        this.order = order;
    }

    @Override
    public void created() {
        System.out.println("Cannot create order once it is shipped");
    }

    @Override
    public void confirm() {
        System.out.println("Cannot confirm order once it is shipped");
    }

    @Override
    public void cancel() {
        System.out.println("Order canceled");
        order.setState(new CanceledState());
    }

    @Override
    public void ship() {
        System.out.println("Order is already shipped");
    }

    @Override
    public void deliver() {
        System.out.println("Order delivered");
        order.setState(new DeliveredState());
    }
}

/**
 *
 * DeliveredState
 *
 * DeliveredState IS-A OrderState.
 *
 * DeliveredState is a terminal state.
 * No further state transitions are allowed.
 *
 */
class DeliveredState implements OrderState {

    public DeliveredState() {
    }

    @Override
    public void created() {
        System.out.println("Cannot create a delivered order");
    }

    @Override
    public void confirm() {
        System.out.println("Cannot confirm a delivered order");
    }

    @Override
    public void cancel() {
        System.out.println("Cannot cancel a delivered order");
    }

    @Override
    public void ship() {
        System.out.println("Cannot ship a delivered order");
    }

    @Override
    public void deliver() {
        System.out.println("Order is already delivered");
    }
}

/**
 *
 * Client
 *
 * The client only interacts with the Order.
 *
 * It does not need to know which OrderState is currently active.
 *
 */
public class State {
    public static void main(String[] args) {
        Order order = new Order();

        order.confirm();
        order.ship();
        order.cancel();
        order.deliver();
    }
}
