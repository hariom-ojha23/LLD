package problems.FoodDelivery;

import javax.swing.plaf.multi.MultiSeparatorUI;
import java.util.ArrayList;
import java.util.List;

/// Functional Requirements:
/// 1. User should be able to search restaurants and dishes
/// 2. User can select restaurant & add menu items in a cart
/// 3. User can pay using different methods
/// 4. Restaurant will accept/reject the order
/// 5. User can track order status
/// 6. Notification to user for different status
///
/// Non-Functional Requirements
/// 1. No double payments/order
/// 2. Thread safety
/// 3. Extensible
/// 4. Follow SOLID principles
///
/// Core Classes/Interface
/// 1. Restaurant (Model Class)
/// 2. MenuItem (Model Class)
/// 3. RestaurantManager (Singleton Pattern)
/// 4. Customer
/// 5. Cart
/// 6. Order (Abstract class)
/// 7. Order Type (enum: Pickup/Delivery)
/// 8. PickupOrder
/// 9. DeliveryOrder
/// 10. OrderManager (Singleton Pattern)
/// 11. OrderFacade (Facade Pattern)
/// 12. NotificationService (Strategy Pattern)
///     - Notification Strategy (Interface)
///     - Email Notification
///     - Push Notification
///     - SMS Notification
/// 13. PaymentService (Strategy Pattern)
///     - Payment Strategy (Interface)
///     - UPI Payment
///     - Card Payment
/// 14. Offers (Decorator Pattern)
///     - RestaurantOffer
///     - AppOffer

enum OrderType {
    DELIVERY,
    PICKUP
}

interface PriceComponent {
    double getTotal();
}

/**
 * Strategy design pattern
 */
interface NotificationStrategy {
    void notify(String message);
}

/**
 * Strategy design pattern
 */
interface PaymentStrategy {
    void pay(double amount);
}

class NotificationEmail implements NotificationStrategy {
    @Override
    public void notify(String message) {
        System.out.println(message);
    }
}

class NotificationPush implements NotificationStrategy {
    @Override
    public void notify(String message) {
        System.out.println(message);
    }
}

class NotificationService {
    public void sendNotification(List<NotificationStrategy> strategies, String message) {
        for (NotificationStrategy notificationStrategy : strategies) {
            notificationStrategy.notify(message);
        }
    }
}

class PaymentCard implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid by card: " + amount);
    }
}

class PaymentUPI implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid by UPI: " + amount);
    }
}

class PaymentService {
    private final PaymentStrategy paymentStrategy;

    public PaymentService(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void processPayment(double amount) {
        this.paymentStrategy.pay(amount);
    }
}

class Cart implements PriceComponent {
    private Restaurant restaurant;
    private List<MenuItem> menuItems = new ArrayList<>();

    public void addItemInCart(MenuItem menuItem) {
        if (restaurant == null) {
            System.out.println("No restaurant found");
            return;
        }
        menuItems.add(menuItem);
    }

    public double getTotal() {
        double totalAmount = 0;

        for (MenuItem menuItem : menuItems) {
            totalAmount += menuItem.getPrice();
        }

        return totalAmount;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public boolean isEmpty() {
        return menuItems.isEmpty();
    }

    public void clearCart() {
        menuItems.clear();
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

}

class Customer {
    private final String name;
    private final String address;
    private final Cart cart;
    private PaymentStrategy paymentStrategy;
    private List<NotificationStrategy> notificationStrategies = new ArrayList<>();

    public Customer(String name, String address) {
        this.name = name;
        this.address = address;
        this.cart = new Cart();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Cart getCart() {
        return cart;
    }

    public void setPaymentStrategy(PaymentStrategy strategy) {
        paymentStrategy = strategy;
    }

    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }

    public void setNotificationStrategies(List<NotificationStrategy> strategies) {
        this.notificationStrategies = strategies;
    }

    public List<NotificationStrategy> getNotificationStrategies() {
        return notificationStrategies;
    }
}

class MenuItem {
    public static int nextCode;
    private final String code;
    private final String name;
    private final double price;

    public MenuItem(String name, double price) {
        this.code = "ITEM-" + ++nextCode;
        this.name = name;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

abstract class Order {
    private final Restaurant restaurant;
    private final Customer customer;
    private final double total;
    private final List<MenuItem> orderItems;

    public Order(
            Customer customer,
            Restaurant restaurant,
            List<MenuItem> orderItems,
            double total
    ) {
        this.customer = customer;
        this.restaurant = restaurant;
        this.total = total;
        this.orderItems = List.copyOf(orderItems); // immutable
    }

    public Restaurant getRestaurant() {
        return  restaurant;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getTotal() {
        return total;
    }

    public List<MenuItem> getOrderItems() {
        return orderItems;
    }

    public abstract OrderType getType();
}

class OrderDelivery extends Order {
    private final String homeAddress;

    public OrderDelivery(
            Customer customer,
            Restaurant restaurant,
            List<MenuItem> orderItems,
            double total
    ) {
        super(customer, restaurant, orderItems,total);
        this.homeAddress = customer.getAddress();
    }

    @Override
    public OrderType getType() {
        return OrderType.DELIVERY;
    }

    public String getHomeAddress() {
        return homeAddress;
    }
}

class OrderPickup extends Order {
    private final String restaurantAddress;

    public OrderPickup(
            Customer customer,
            Restaurant restaurant,
            List<MenuItem> orderItems,
            double total
    ) {
        super(customer, restaurant, orderItems,total);
        this.restaurantAddress = restaurant.getLocation();
    }

    @Override
    public OrderType getType() {
        return OrderType.PICKUP;
    }

    public String getRestaurantAddress() {
        return this.restaurantAddress;
    }
}

/**
 * Singleton design pattern
 */
class OrderManager {
    private final List<Order> orders = new ArrayList<>();

    private OrderManager() {}

    private static class Provider {
        public static OrderManager instance = new OrderManager();
    }

    public static OrderManager getInstance() {
        return Provider.instance;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void showAllOrders() {
        System.out.println("All orders:");

        for (Order order : orders) {
            System.out.println("Type : " + order.getType());
            System.out.println("For  : " + order.getCustomer().getName());
            System.out.println("Total: " + order.getTotal());
            System.out.println();
        }
    }
}

/**
 * Factory design pattern
 */
abstract class OrderFactory {
    public abstract Order createOrder(
            Customer customer,
            Restaurant restaurant,
            List<MenuItem> orderItems,
            double totalAmount
    );
}

class OrderDeliveryFactory extends OrderFactory {

    @Override
    public Order createOrder(
            Customer customer,
            Restaurant restaurant,
            List<MenuItem> orderItems,
            double totalAmount
    ) {

        return new OrderDelivery(
                customer,
                restaurant,
                orderItems,
                totalAmount
        );
    }
}

class OrderPickupFactory extends OrderFactory {

    @Override
    public Order createOrder(
            Customer customer,
            Restaurant restaurant,
            List<MenuItem> orderItems,
            double totalAmount
    ) {
        return new OrderPickup(
                customer,
                restaurant,
                orderItems,
                totalAmount
        );
    }
}


class Restaurant {
    private final String name;
    private final String location;
    private final List<MenuItem> menu = new ArrayList<>();

    public Restaurant(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public String getLocation() {
        return this.location;
    }

    public void addMenuItem(MenuItem menuItem) {
        this.menu.add(menuItem);
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public void showMenu() {
        System.out.println("Menu: ");

        for (MenuItem menuItem : menu) {
            System.out.println(
                    "Code: " + menuItem.getCode() + " Name: " + menuItem.getName() + " Price: " + menuItem.getPrice()
            );
        }
        System.out.println();
    }
}


/**
 * Singleton design patterns
 */
class RestaurantManager {
    private final List<Restaurant> restaurants = new ArrayList<>();

    private RestaurantManager() {}

    private static class Provider {
        public final static RestaurantManager instance = new RestaurantManager();
    }

    public static RestaurantManager getInstance() {
        return Provider.instance;
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public List<Restaurant> searchByLocation(String location) {
        List<Restaurant> searchResults = new ArrayList<>();

        for (Restaurant restaurant : restaurants) {
            if (restaurant.getLocation().equals(location)) {
                searchResults.add(restaurant);
            }
        }

        return searchResults;
    }
}


/**
 * Decorator design pattern
 */
abstract class OfferDecorator implements PriceComponent {
    protected final PriceComponent priceComponent;

    public OfferDecorator(PriceComponent priceComponent) {
        this.priceComponent = priceComponent;
    }
}

class RestaurantOffer extends OfferDecorator {
    double discountPercent = 10;

    public RestaurantOffer(PriceComponent priceComponent) {
        super(priceComponent);
    }

    @Override
    public double getTotal() {
        System.out.println("Applying restaurant discount of " + discountPercent + "%");
        return priceComponent.getTotal() * (1- discountPercent / 100);
    }
}

class AppOffer extends OfferDecorator {
    double discountPercent = 5;

    public AppOffer(PriceComponent priceComponent) {
        super(priceComponent);
    }

    @Override
    public double getTotal() {
        System.out.println("Applying app discount of " + discountPercent + "%");
        return priceComponent.getTotal() * (1- discountPercent / 100);
    }
}

/**
 * Facade design pattern
 * Responsibilities:
 *
 *
 */
class OrderFacade {
    private final RestaurantManager restaurantManager = RestaurantManager.getInstance();
    private final OrderManager orderManager = OrderManager.getInstance();

    public OrderFacade() {
        initializeRestaurants();
    }

    private void initializeRestaurants() {
        MenuItem.nextCode = 0;

        Restaurant restaurant1 = new Restaurant("Brown Bakers & Restaurant", "Mayur Vihar, Delhi");
        restaurant1.addMenuItem(new MenuItem("Pav Bhaji", 120.00));
        restaurant1.addMenuItem(new MenuItem("Paneer Dum Biryani", 140.00));
        restaurant1.addMenuItem(new MenuItem("Hyderabadi Biryani", 180.00));

        Restaurant restaurant2 = new Restaurant("Haldiram", "Laxmi Nagar, Delhi");
        restaurant2.addMenuItem(new MenuItem("Raj Kachori", 80.00));
        restaurant2.addMenuItem(new MenuItem("Chole Bhature", 120.00));
        restaurant2.addMenuItem(new MenuItem("Masala Dosa", 100.00));

        Restaurant restaurant3 = new Restaurant("China Town", "Laxmi Nagar, Delhi");
        restaurant3.addMenuItem(new MenuItem("Chilli Potato", 110.00));
        restaurant3.addMenuItem(new MenuItem("Shezwan Chowmein", 130.00));
        restaurant3.addMenuItem(new MenuItem("Paneer Fried Rice", 130.00));

        restaurantManager.addRestaurant(restaurant1);
        restaurantManager.addRestaurant(restaurant2);
        restaurantManager.addRestaurant(restaurant3);
    }

    public List<Restaurant> searchRestaurant(String location) {
        return restaurantManager.searchByLocation(location);
    }

    public void selectRestaurant(Customer customer, Restaurant restaurant) {
        customer.getCart().setRestaurant(restaurant);
        System.out.println("Selected restaurant: " + restaurant.getName() + "\n");
    }

    public void addToCart(Customer customer, String itemCode) {
        Restaurant restaurant = customer.getCart().getRestaurant();

        if (restaurant == null) {
            System.out.println("First select a restaurant");
            return;
        }

        for (MenuItem menuItem : restaurant.getMenu()) {
            if (menuItem.getCode().equals(itemCode)) {
                System.out.println("Added " + menuItem.getName() + " in cart");
                customer.getCart().addItemInCart(menuItem);
            }
        }
    }

    public void showRestaurantMenu(Restaurant restaurant) {
        restaurant.showMenu();
    }

    public Order placeDeliveryOrder(Customer customer) {
        return placeOrder(customer, new OrderDeliveryFactory());
    }

    public Order placePickupOrder(Customer customer) {
        return placeOrder(customer, new OrderPickupFactory());
    }

    private Order placeOrder(
            Customer customer,
            OrderFactory factory
    ) {
        System.out.println("\nPlacing order....\n");
        // verify cart is not empty
        if (customer.getCart().isEmpty()) {
            System.out.println("Cannot place order. Cart is empty");
            return null;
        }

        Cart cart = customer.getCart();
        Restaurant restaurant = cart.getRestaurant();

        double totalAmount = Math.round(
                new AppOffer(new RestaurantOffer(cart)).getTotal() * 100.0
        ) / 100.0;

        System.out.println("\nInitial amount: " + cart.getTotal());
        System.out.println("Amount after discount: " + totalAmount + "\n");

        Order order = factory.createOrder(customer, restaurant, cart.getMenuItems(), totalAmount);

        // process payment
        processPayment(customer.getPaymentStrategy(), totalAmount);

        // send notification
        notifyCustomer(customer.getNotificationStrategies(), order);

        orderManager.addOrder(order);
        return order;
    }

    private void processPayment(PaymentStrategy paymentStrategy, double amount) {
        PaymentService paymentService = new PaymentService(paymentStrategy);
        paymentService.processPayment(amount);
    }

    private void notifyCustomer(List<NotificationStrategy> strategies, Order order) {
        NotificationService notificationService = new NotificationService();

        StringBuilder orderItems = new StringBuilder("\n");

        for (MenuItem item : order.getOrderItems()) {
            orderItems.append("Name: ").append(item.getName()).append(" Price: ").append(item.getPrice());
            orderItems.append("\n");
        }

        String message = """
        Your order has been placed successfully!

        Restaurant: %s
        Items: %s
        Total: %.2f
        Order Type: %s

        Thank you for ordering!
        """.formatted(
                order.getRestaurant().getName(),
                orderItems.toString(),
                order.getTotal(),
                order.getType()
        );

        notificationService.sendNotification(strategies, message);
    }
}

public class FoodDelivery {

    public static void main(String[] args) {

        OrderFacade facade = new OrderFacade();

        Customer customer = new Customer(
                "Hariom",
                "Mayur Vihar, Delhi"
        );

        customer.setPaymentStrategy(new PaymentUPI());

        customer.setNotificationStrategies(List.of(new NotificationEmail()));

        System.out.println();

        // Search restaurant
        List<Restaurant> restaurants = facade.searchRestaurant("Mayur Vihar, Delhi");

        Restaurant restaurant = restaurants.getFirst();

        // Select restaurant
        facade.selectRestaurant(customer, restaurant);
        restaurant.showMenu();

        // Add items to cart
        facade.addToCart(customer, "ITEM-1");
        facade.addToCart(customer, "ITEM-2");

        // Place order
        Order order = facade.placeDeliveryOrder(customer);
    }
}
