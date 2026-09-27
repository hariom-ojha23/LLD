package assignments.structural.notification.builder;

public class NotificationRequest {
    private final String to;
    private final String subject;
    private final String message;

    private NotificationRequest(RequestBuilder builder) {
        this.to = builder.to;
        this.subject = builder.subject;
        this.message = builder.message;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public static class RequestBuilder {
        private String to;
        private String subject;
        private String message;

        public RequestBuilder setTo(String to) {
            this.to = to;
            return this;
        }

        public RequestBuilder setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public RequestBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public NotificationRequest build() {
            return new NotificationRequest(this);
        }
    }
}
