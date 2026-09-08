package designpattern.creational;

/**
 * 
 * Builder
 * 
 * - The Builder Pattern is a creational design pattern 
 *   which separates the construction of a complex object from its representation
 * 
 * - Instead of passing many parameters to a constructor, 
 *   we build the object using a series of method calls.
 * 
 * 
 * Problem:
 * - Hard to understand which argument represents what.
 * - Constructor becomes huge.
 * - Difficult to handle optional parameters.
 * - Easy to accidentally pass arguments in the wrong order
 * 
 * 
 */

/**
 * 
 * Builder
 */
class HttpRequest {
    private final String url;
    private final String method;
    private final String body;
    private final int timeout;

    private HttpRequest(HttpRequestBuilder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.body = builder.body;
        this.timeout = builder.timeout;
    }

    public String getUrl() {
        return this.url;
    }

    public String getMethod() {
        return this.method;
    }

    public String getBody() {
        return this.body;
    }

    public int getTimeout() {
        return this.timeout;
    }

    static class HttpRequestBuilder {
        private String url;
        private String method = "GET";
        private String body;
        private int timeout = 3000;

        public HttpRequestBuilder(String url) {
            this.url = url;
        }

        public HttpRequestBuilder setMethod(String method) {
            this.method = method;
            return this;
        }

        public HttpRequestBuilder setBody(String body) {
            this.body = body;
            return this;
        }

        public HttpRequestBuilder setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public HttpRequest build() {
            if (this.url == null) {
                throw new IllegalStateException("URL is required");
            }

            return new HttpRequest(this);
        }
    }
}

public class Builder {
    public static void main(String[] args) {
        HttpRequest request = new HttpRequest.HttpRequestBuilder("/place-order")
                .setMethod("POST")
                .setBody("Laptop 512gb ssd 16gb ram")
                .setTimeout(5000).build();

        System.out.println("URL: " + request.getUrl());
        System.out.println("method: " + request.getMethod());
        System.out.println("Body: " + request.getBody());
        System.out.println("Timeout: " + request.getTimeout());
    }
}
