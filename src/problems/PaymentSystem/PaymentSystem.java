package problems.PaymentSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * 
 * PaymentSystem
 * 
 * Functional Requirement
 * 
 * - Create payemnt
 * - Support multiple payment method
 * - Supoprt multiple payment gateways
 * - Prevent double payment using idempotency
 * - Async submission to gateway
 * - Retry failed gateway calls
 * 
 * --- optional ---
 * 
 * - Track payment status
 * 
 * 
 * Not-Functional Requirement
 * 
 * - Thread safety
 * - Extensibility
 * - Idempotency
 * - SOLID Principle
 * - Payment status
 * 
 * 
 * Core Classes/Interface/Enums
 * 
 * PaymentService
 * PaymentMethod (Interface)
 * - UPIPayment
 * - CardPayment
 * PaymentGateway (Interface)
 * - Razorpay
 * - Stripe
 * IdempotencyStore
 * Payment
 * - id
 * - merchantId
 * - idempotencyKey
 * - payemntMethod
 * - paymentGateway
 * - amount
 * - currency
 * - status
 * - version
 * PaymentStatus (Enum)
 * - Created
 * - Processing
 * - Success
 * - Failed
 * PaymentOrchestrator
 * PaymentGatewayFactory
 * RetryDecorator
 * PaymentRepository
 * 
 * 
 */

enum PaymentStatus {
    CREATED,
    PROCESSING,
    SUCCESS,
    FAILED
}

enum PaymentGatewayType {
    RAZORPAY,
    STRIPE
}

enum PaymentMethodType {
    UPI,
    CARD
}

interface PaymentMethod {
    public void pay(double amount);

    public boolean validate();
}

interface PaymentGateway {
    abstract public void initiatePayment(Payment payment);
}

record UpdateStatusEvent(String paymentId, PaymentStatus status) {
}

record ProcessPaymentEvent(Payment payment) {
}

/**
 * 
 * UpiPayment
 */
class UpiPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println(String.format("Paid %s through UPI", amount));
    }

    @Override
    public boolean validate() {
        return true;
    }
}

/**
 * 
 * CardPayment
 */
class CardPayment implements PaymentMethod {

    @Override
    public void pay(double amount) {
        System.out.println(String.format("Paid %s through CARD", amount));
    }

    @Override
    public boolean validate() {
        return true;
    }
}

/**
 * 
 * RazorPayGateway
 */
class RazorPayGateway implements PaymentGateway {
    public PaymentEventPublisher eventPublisher = PaymentEventPublisher.getInstance();

    @Override
    public void initiatePayment(Payment payment) {
        /**
         * Get payment method
         * Call payment method
         * publish update status event
         */
        PaymentMethod method = PaymentMethodFactory.getMethod(payment.getPaymentMethod());
        method.pay(payment.getAmount());
        eventPublisher.addUpdateStatusEvent(new UpdateStatusEvent(payment.getId(), PaymentStatus.SUCCESS));
    }

}

/**
 * 
 * StripeGateway
 */
class StripeGateway implements PaymentGateway {
    public PaymentEventPublisher eventPublisher = PaymentEventPublisher.getInstance();

    @Override
    public void initiatePayment(Payment payment) {
        /**
         * Get payment method
         * Call payment method
         * publish update status event
         */
        PaymentMethod method = PaymentMethodFactory.getMethod(payment.getPaymentMethod());
        method.pay(payment.getAmount());
        eventPublisher.addUpdateStatusEvent(new UpdateStatusEvent(payment.getId(), PaymentStatus.SUCCESS));
    }

}

/**
 * 
 * PaymentDecorator
 */
abstract class PaymentDecorator implements PaymentGateway {
    public PaymentGateway paymenyGateway;

    public PaymentDecorator(PaymentGateway gateway) {
        this.paymenyGateway = gateway;
    }
}

/**
 * 
 * RetryDecorator
 * 
 * Design pattern: Decorator
 * 
 */
class RetryDecorator extends PaymentDecorator {
    private final int maxAttempt;
    private final Random random = new Random();

    public RetryDecorator(PaymentGateway gateway, int maxAttempt) {
        super(gateway);
        this.maxAttempt = maxAttempt;
    }

    @Override
    public void initiatePayment(Payment payment) {
        int attempt = 1;

        while (attempt <= maxAttempt) {
            try {
                int randomNumber = random.nextInt(3) + 1;

                if (randomNumber <= 2) {
                    throw new RuntimeException("Failed to send notification");
                }

                paymenyGateway.initiatePayment(payment);
                return;
            } catch (Exception e) {
                System.out.println("Attempt Failed: " + attempt);
                attempt++;
            }
        }
    }
}

/**
 * 
 * IdempotencyStore
 * 
 * Design pattern: Singleton
 */
class IdempotencyStore {
    private static Map<String, Payment> idempotencyMap = new ConcurrentHashMap<>();

    private IdempotencyStore() {
    }

    private static class IdempotencyStoreHolder {
        private static final IdempotencyStore instance = new IdempotencyStore();
    }

    public static IdempotencyStore getInstance() {
        return IdempotencyStoreHolder.instance;
    }

    public boolean registerIfAbsent(String key, Payment payment) {
        return idempotencyMap.putIfAbsent(key, payment) == null;
    }

    public boolean hasKey(String key) {
        return idempotencyMap.containsKey(key);
    }

    public void removeKey(String key) {
        idempotencyMap.remove(key);
    }
}

/**
 * 
 * Payment
 * 
 * Design pattern: Builder
 * 
 */
class Payment {
    private final String id;
    private final String merchantId;
    private final String idempotencyKey;
    private final PaymentGatewayType paymentGatewayType;
    private final PaymentMethodType paymentMethodType;
    private final double amount;
    private final String currency;
    private PaymentStatus status;
    private int version;

    public Payment(PaymentBuilder builder) {
        id = builder.id;
        merchantId = builder.merchantId;
        idempotencyKey = builder.idempotencyKey;
        paymentGatewayType = builder.paymentGatewayType;
        paymentMethodType = builder.paymentMethodType;
        amount = builder.amount;
        currency = builder.currency;
        status = builder.status;
        version = builder.version;
    }

    public static class PaymentBuilder {
        private String id;
        private String merchantId;
        private String idempotencyKey;
        private PaymentGatewayType paymentGatewayType;
        private PaymentMethodType paymentMethodType;
        private double amount;
        private String currency;
        private PaymentStatus status = PaymentStatus.CREATED;
        private int version;

        public PaymentBuilder(String id) {
            this.id = id;
        }

        public PaymentBuilder setMerchantId(String id) {
            merchantId = id;
            return this;
        }

        public PaymentBuilder setIdempotencyKey(String key) {
            idempotencyKey = key;
            return this;
        }

        public PaymentBuilder setGatewayType(PaymentGatewayType type) {
            paymentGatewayType = type;
            return this;
        }

        public PaymentBuilder setPaymentMethod(PaymentMethodType method) {
            paymentMethodType = method;
            return this;
        }

        public PaymentBuilder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public PaymentBuilder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public PaymentBuilder setVersion(int version) {
            this.version = version;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }

    public String getId() {
        return id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public PaymentGatewayType getGatewayType() {
        return paymentGatewayType;
    }

    public PaymentMethodType getPaymentMethod() {
        return paymentMethodType;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public int getVersion() {
        return version;
    }

    public void updateStatus(PaymentStatus status) {
        this.status = status;
    }
}

class PaymentIntent {
    private final String id;
    private final String merchantId;
    private final double amount;
    private final String idempotencyKey;
    private final String currency;
    private final PaymentGatewayType paymentGatewayType;
    private final PaymentMethodType paymentMethodType;

    private PaymentIntent(Builder builder) {
        this.id = builder.id;
        this.merchantId = builder.merchantId;
        this.amount = builder.amount;
        this.idempotencyKey = builder.idempotencyKey;
        this.currency = builder.currency;
        this.paymentGatewayType = builder.paymentGatewayType;
        this.paymentMethodType = builder.paymentMethodType;
    }

    public static class Builder {
        private final String id;
        private String merchantId;
        private double amount;
        private String idempotencyKey;
        private String currency;
        private PaymentGatewayType paymentGatewayType;
        private PaymentMethodType paymentMethodType;

        public Builder(String id) {
            this.id = id;
        }

        public Builder merchantId(String merchantId) {
            this.merchantId = merchantId;
            return this;
        }

        public Builder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder idempotencyKey(String idempotencyKey) {
            this.idempotencyKey = idempotencyKey;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder paymentGatewayType(PaymentGatewayType type) {
            this.paymentGatewayType = type;
            return this;
        }

        public Builder paymentMethodType(PaymentMethodType type) {
            this.paymentMethodType = type;
            return this;
        }

        public PaymentIntent build() {
            return new PaymentIntent(this);
        }
    }

    public String getId() {
        return id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public double getAmount() {
        return amount;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getCurrency() {
        return currency;
    }

    public PaymentGatewayType getPaymentGatewayType() {
        return paymentGatewayType;
    }

    public PaymentMethodType getPaymentMethodType() {
        return paymentMethodType;
    }
}

/**
 * 
 * PaymentGatewayFactory
 * 
 * Design pattern: Simple Factory
 * 
 */
class PaymentGatewayFactory {
    private final static Map<PaymentGatewayType, Supplier<PaymentGateway>> gateways = Map.of(
            PaymentGatewayType.RAZORPAY,
            () -> new RetryDecorator(new RazorPayGateway(), 3),

            PaymentGatewayType.STRIPE,
            () -> new RetryDecorator(new StripeGateway(), 3));

    public static PaymentGateway getGateway(PaymentGatewayType type) {
        Supplier<PaymentGateway> gateway = gateways.get(type);

        if (gateway == null) {
            throw new RuntimeException("Unsuppoted gateway: " + type);
        }

        return gateway.get();
    }
}

/**
 * 
 * PaymentMethodFactory
 * 
 * Design pattern: Simple Factory
 * 
 */
class PaymentMethodFactory {
    private final static Map<PaymentMethodType, Supplier<PaymentMethod>> methods = Map.of(
            PaymentMethodType.UPI,
            () -> new UpiPayment(),

            PaymentMethodType.CARD,
            () -> new CardPayment());

    public static PaymentMethod getMethod(PaymentMethodType type) {
        Supplier<PaymentMethod> method = methods.get(type);

        if (method == null) {
            throw new RuntimeException("Unsuppoted method: " + type);
        }

        return method.get();
    }
}

/**
 * 
 * PaymentService
 */
class PaymentService {
    private final IdempotencyStore idempotencyStore;
    private final PaymentRepository paymentRepo;
    private final PaymentEventPublisher eventPublisher;

    public PaymentService() {
        this.idempotencyStore = IdempotencyStore.getInstance();
        this.paymentRepo = PaymentRepository.getInstance();
        this.eventPublisher = PaymentEventPublisher.getInstance();
    }

    /**
     * 1. Check idempotency
     * 2. Check payment method
     * 3. Create Payment (stauts = created)
     * 4. Save idempotency key in store
     * 4. Send payment to orchestrator (async)
     */
    public Payment initiatePayment(PaymentIntent intent) {
        Payment payment = null;

        try {
            synchronized (this) {
                String key = intent.getMerchantId() + ":" + intent.getIdempotencyKey();

                // check idempotency
                if (idempotencyStore.hasKey(key)) {
                    throw new IllegalArgumentException("Duplicate key. Payment already initiated");
                }

                // check payment method
                PaymentMethod method = PaymentMethodFactory.getMethod(intent.getPaymentMethodType());
                boolean valid = method.validate();
                if (!valid) {
                    throw new IllegalArgumentException("Invailid method type credentials");
                }

                // create and save payment to db & idempotency key
                payment = new Payment.PaymentBuilder(UUID.randomUUID().toString())
                        .setMerchantId(intent.getMerchantId())
                        .setIdempotencyKey(intent.getIdempotencyKey())
                        .setGatewayType(intent.getPaymentGatewayType())
                        .setPaymentMethod(intent.getPaymentMethodType())
                        .setAmount(intent.getAmount())
                        .setCurrency(intent.getCurrency())
                        .setVersion(1)
                        .build();

                // saving key atomically
                if (!idempotencyStore.registerIfAbsent(key, payment)) {
                    throw new IllegalArgumentException("Duplicate idempotency key");
                }

                try {
                    paymentRepo.save(payment);
                } catch (Exception e) {
                    // rollback idempotency
                    idempotencyStore.removeKey(key);
                    throw e;
                }

            }

            // send payment to event publisher
            eventPublisher.addPaymentProcessEvent(new ProcessPaymentEvent(payment));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return payment;
    }

    public void updateStatus(String paymentId, PaymentStatus status) {
        paymentRepo.updateStatus(paymentId, status);
    }
}

/**
 * 
 * PaymentOrchestrator
 */
class PaymentOrchestrator {
    ExecutorService threadPool = Executors.newFixedThreadPool(3);

    public void submit(Payment payment) {
        threadPool.submit(() -> {
            PaymentGateway gateway = PaymentGatewayFactory.getGateway(payment.getGatewayType());
            gateway.initiatePayment(payment);
        });
    }
}

/**
 * 
 * PaymentRepository
 * 
 * DB Simulation
 * 
 */
class PaymentRepository {
    Map<String, Payment> repo = new HashMap<>();

    private PaymentRepository() {
    }

    private static class PaymentRepositoryHolder {
        private final static PaymentRepository instance = new PaymentRepository();
    }

    public static PaymentRepository getInstance() {
        return PaymentRepositoryHolder.instance;
    }

    public void save(Payment payment) {
        repo.put(payment.getId(), payment);
    }

    private boolean checkEntry(String paymentId) {
        if (paymentId == null || !repo.containsKey(paymentId)) {
            throw new IllegalArgumentException("Invalid payment id");
        }

        return true;
    }

    public Payment getPayment(String paymentId) {
        checkEntry(paymentId);
        return repo.get(paymentId);
    }

    public void updateStatus(String paymentId, PaymentStatus status) {
        checkEntry(paymentId);

        Payment payment = repo.get(paymentId);
        payment.updateStatus(status);
    }
}

/**
 * 
 * PaymentEventPublisher
 * 
 * Design pattern: Singleton
 */
class PaymentEventPublisher {
    private PaymentOrchestrator orchestrator;
    private PaymentService paymentService;

    private static class PaymentEventPublisherHolder {
        private final static PaymentEventPublisher instance = new PaymentEventPublisher();
    }

    public static PaymentEventPublisher getInstance() {
        return PaymentEventPublisherHolder.instance;
    }

    public void setOrchestrator(PaymentOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    public void setPaymentService(PaymentService service) {
        this.paymentService = service;
    }

    public void addUpdateStatusEvent(UpdateStatusEvent event) {
        this.paymentService.updateStatus(event.paymentId(), event.status());
    }

    public void addPaymentProcessEvent(ProcessPaymentEvent event) {
        paymentService.updateStatus(event.payment().getId(), PaymentStatus.PROCESSING);
        this.orchestrator.submit(event.payment());
    }
}

public class PaymentSystem {

    public static void main(String[] args) throws InterruptedException {

        // Create dependencies
        PaymentOrchestrator orchestrator = new PaymentOrchestrator();
        PaymentService paymentService = new PaymentService();

        // Event publisher
        PaymentEventPublisher eventPublisher = PaymentEventPublisher.getInstance();
        eventPublisher.setOrchestrator(orchestrator);
        eventPublisher.setPaymentService(paymentService);

        // Create a payment intent
        PaymentIntent intent = new PaymentIntent.Builder(UUID.randomUUID().toString())
                .merchantId("MERCHANT_101")
                .idempotencyKey(UUID.randomUUID().toString())
                .paymentGatewayType(PaymentGatewayType.RAZORPAY)
                .paymentMethodType(PaymentMethodType.UPI)
                .amount(1500.00)
                .currency("INR")
                .build();

        // Initiate payment
        System.out.println("Initiating payment...");

        Payment payment = paymentService.initiatePayment(intent);

        // Print initial payment details
        System.out.println("Payment ID: " + payment.getId());
        System.out.println("Merchant ID: " + payment.getMerchantId());
        System.out.println("Amount: " + payment.getAmount() + " " + payment.getCurrency());
        System.out.println("Gateway: " + payment.getGatewayType());
        System.out.println("Payment Method: " + payment.getPaymentMethod());
        System.out.println("Initial Status: " + payment.getStatus());

        // Wait briefly to observe asynchronous processing
        Thread.sleep(2000);

        System.out.println("Final Status: " + payment.getStatus());
    }
}
