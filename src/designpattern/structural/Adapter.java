package designpattern.structural;

/**
 * 
 * Adapter
 * 
 * Adapter Pattern allows incompatible interfaces to work together without
 * changing their existing code.
 * 
 * It acts as a bridge between two incompatible interfaces.
 * 
 * It is commonly used to integrate third-party services/libraries
 * without changing our existing code.
 * 
 * 
 * Adapter converts the interface of an existing class into an interface
 * expected by the client.
 * 
 * It can also adapt:
 * - Legacy code
 * - Third-party libraries
 * - Different APIs
 * - Different data formats
 * - Incompatible interfaces
 * 
 * 
 * Example: Suppose we have a legacy system that uses Fahrenheit temperature
 * scale, but we need to integrate it with a new system that uses Celsius scale.
 * 
 * solution: We can create an adapter that converts Fahrenheit to Celsius.
 * 
 * 
 * The Adapter allows:
 * Your existing code → remain unchanged
 * Third-party code → remain unchanged
 * Adapter → converts one interface into another
 * 
 * 
 * This is different from Decorator:
 * Adapter → changes the interface
 * Decorator → adds new behavior while keeping the same interface.
 * 
 */

interface NotificationService {
    public void send(String message);
}

/**
 * 
 * CurrentService
 * 
 * CurrentService IS-A NotificationService
 * 
 */
class CurrentService implements NotificationService {
    @Override
    public void send(String message) {
        System.out.println("Current service: " + message);
    }
}

class ThirdPartySmsService {
    public void sendSms(String message) {
        System.out.println("Third party sms service: " + message);
    }
}

/**
 * 
 * ThirdPartySmsAdapter
 * 
 * ThirdPartySmsAdapter IS-A NotificationService
 * ThirdPartySmsAdapter HAS-A ThirdPartySmsService
 * 
 */
class ThirdPartySmsAdapter implements NotificationService {
    private final ThirdPartySmsService smsService;

    public ThirdPartySmsAdapter(ThirdPartySmsService smsService) {
        this.smsService = smsService;
    }

    @Override
    public void send(String message) {
        smsService.sendSms(message);
    }
}

public class Adapter {
    public static void main(String[] args) {
        NotificationService currentService = new CurrentService();
        ThirdPartySmsService smsService = new ThirdPartySmsService();
        NotificationService smsAdapter = new ThirdPartySmsAdapter(smsService);

        currentService.send("Message from current service");
        smsAdapter.send("Message from sms service");
    }
}
