package designpattern.structural.facade;

/**
 * 
 * Facade
 * 
 * Facade provides a simple interface to a complex system
 * of classes or subsystems
 * 
 * It hides the complexity and gives the client
 * one simple way to perform an operation
 * 
 * 
 * Facade does not replace the underlying classes.
 * It provides a simpler entry point to them.
 * 
 * Facade coordinates the subsystem classes;
 * it does not perform all the subsystem responsibilities itself.
 * 
 * 
 * When to use:
 * - A system has many classes and is difficult to use
 * - To provide simple interface to comlex systems
 * - To hide internal implemntation from client
 * - Multiple client needs to perform same sequence of operation
 * 
 * 
 * Benefits:
 * - Reduces complexity of client code
 * - Decouples client from underlying system
 * - Hides internal implementation
 * - Provides simple interface
 * - Reuse existing code
 * - Improves readability
 * 
 * 
 * Drawbacks:
 * - Facade can become a large class if too much complexity is added
 * - It can hide useful functionality of underlying classes
 * - Another layer is added to system
 * 
 * 
 * Adapter
 * - Makes incompatible interfaces compatible.
 * 
 * Facade
 * - Makes a complex system easier to use.
 * 
 * 
 */

class Car {
    private final String brand;
    private final String color;

    public Car(String brand, String color) {
        this.brand = brand;
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }
}

/**
 * 
 * Customer
 * 
 * Subsystem class responsible for customer validation.
 * 
 */
class Customer {
    private final String name;
    private final String password;

    public Customer(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void validateCustomer(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        if (!username.equals(this.name) || !password.equals(this.password)) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        System.out.println("Customer is valid");
    }
}

/**
 * 
 * VehicleService:
 * 
 * Subsystem class responsible for checking vehicle availability.
 * 
 */
class VehicleService {
    public boolean checkAvailability() {
        return true;
    }
}

/**
 * 
 * PaymentService:
 * 
 * Subsystem class responsible for processing payments.
 * 
 */
class PaymentService {
    public void processPayment(int amount) {
        System.out.println("Processing payment: $" + amount);
        System.out.println("Payment successful");
    }
}

/**
 * 
 * CarBookingService:
 * 
 * Subsystem class responsible for creating car bookings.
 * 
 */
class CarBookingService {
    public void bookCar(Car car) {
        System.out.println(car.getBrand() + " " + car.getColor() + " Car booked successfully");
    }
}

/**
 * 
 * NotificationService:
 * 
 * Subsystem class responsible for sending notifications.
 * 
 */
class NotificationService {
    public void sendNotification() {
        System.out.println("Notification sent successfully");
    }
}

/**
 * 
 * CarRentFacade:
 * 
 * Facade provides a simple entry point for renting a car.
 * 
 * It coordinates multiple subsystem classes:
 * - Customer validation
 * - Vehicle availability
 * - Payment processing
 * - Car booking
 * - Notification
 * 
 * The client does not need to interact with these subsystem
 * classes individually.
 * 
 * 
 * CarRentFacade HAS-A VehicleService
 * CarRentFacade HAS-A PaymentService
 * CarRentFacade HAS-A CarBookingService
 * CarRentFacade HAS-A NotificationService
 * 
 */
class CarRentFacade {
    private final VehicleService vehicleService;
    private final PaymentService paymentService;
    private final CarBookingService bookingService;
    private final NotificationService notificationService;

    public CarRentFacade(
            VehicleService vehicleService,
            PaymentService paymentService,
            CarBookingService bookingService,
            NotificationService notificationService) {
        this.vehicleService = vehicleService;
        this.paymentService = paymentService;
        this.bookingService = bookingService;
        this.notificationService = notificationService;
    }

    public void rentCar(Customer customer, Car car) {
        customer.validateCustomer(customer.getName(), customer.getPassword());
        boolean carAvailable = vehicleService.checkAvailability();

        if (carAvailable) {
            paymentService.processPayment(1000);
            bookingService.bookCar(car);
            notificationService.sendNotification();
        } else {
            System.out.println("Car is not available");
        }
    }
}

public class Facade {
    public static void main(String[] args) {
        Car car = new Car("Audi", "Red");
        Customer customer = new Customer("John", "1234");

        VehicleService vehicleService = new VehicleService();
        PaymentService paymentService = new PaymentService();
        CarBookingService bookingService = new CarBookingService();
        NotificationService notificationService = new NotificationService();

        CarRentFacade facade = new CarRentFacade(
                vehicleService,
                paymentService,
                bookingService,
                notificationService);

        facade.rentCar(customer, car);
    }
}
