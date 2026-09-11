package designpattern.structural;

/**
 *
 * Decorator
 *
 * Decorator pattern allows us to add new behaviour & responsibilities
 * to an object dynamically without modifying its original class.
 *
 * Decorator uses composition to wrap an object and add new behaviour
 * while maintaining the same interface.
 *
 * The decorator implements the same interface as the wrapped object
 * and delegates the original operation to it.
 *
 * Decorator = Wrap an object to add behaviour dynamically
 * without modifying the original object.
 *
 *
 * Real-world examples:
 *
 * API Client     → Retry, caching, logging, metrics, authentication
 * Payment        → Retry, fraud check, logging, metrics
 * File Storage   → Encryption, compression, logging
 * Notification   → Retry, logging, metrics
 * Pricing        → Discount, coupon, tax, membership benefits
 *
 */

class ApiRequest {
    private final String url;

    public ApiRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }
}

class ApiResponse {
    private final int statusCode;
    private final String body;

    public ApiResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getBody() {
        return this.body;
    }
}

/**
 * 
 * ApiClient: Component
 */
interface ApiClient {
    public ApiResponse execute(ApiRequest request);
}

/**
 * 
 * HttpApiClient: Concrete component
 */
class HttpApiClient implements ApiClient {
    @Override
    public ApiResponse execute(ApiRequest request) {
        System.out.println("Calling API: " + request.getUrl());

        return new ApiResponse(200, "Success");
    }
}

/**
 *
 * ApiClientDecorator: Abstract Decorator
 *
 * Implements the same ApiClient interface as the concrete component.
 *
 * Holds a reference to another ApiClient and delegates the actual
 * operation to it.
 *
 */
abstract class ApiClientDecorator implements ApiClient {
    protected ApiClient client;

    public ApiClientDecorator(ApiClient client) {
        this.client = client;
    }
}

class RetryApiClient extends ApiClientDecorator {

    public RetryApiClient(ApiClient client) {
        super(client);
    }

    @Override
    public ApiResponse execute(ApiRequest request) {
        for (int i = 0; i < 3; i++) {
            try {
                if (i != 2) {
                    throw new Exception("Request failed");
                }

                return client.execute(request);
            } catch (Exception e) {
                System.out.println("Retrying...");
            }
        }

        throw new RuntimeException("Request failed");
    }
}

class LoggingApiClient extends ApiClientDecorator {

    public LoggingApiClient(ApiClient client) {
        super(client);
    }

    @Override
    public ApiResponse execute(ApiRequest request) {
        System.out.println("Request started....");

        ApiResponse response = client.execute(request);

        System.out.println("Request status: " + response.getStatusCode());

        return response;
    }
}

public class Decorator {

    public static void main(String[] args) {
        /**
         * Start with the concrete component
         * 
         * Structure: RetryClient -> ApiClient
         * 
         */
        ApiClient client = new HttpApiClient();

        /**
         * Wrap with retry behaviour
         */
        client = new RetryApiClient(client);

        /**
         * Wrap with Logging behaviour
         * 
         * Structure: LoggingClient -> RetryClient -> ApiClient
         * 
         */
        client = new LoggingApiClient(client);

        ApiResponse response = client.execute(new ApiRequest("/users/update/1"));
        System.out.println("Response: " + response.getBody());
    }
}
