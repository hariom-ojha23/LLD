package assignments.creational.transactions.builder;

public class Transaction {
    // mandatory fields
    private final String transactionId;
    private final String customerId;
    private final double amount;

    // optional
    private final String currency;
    private final String timestamp;
    private final String notes;

    public String getTransactionId() {
        return transactionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getNotes() {
        return notes;
    }

    public String getTransactionDetails() {
        return String.format(
                "Transaction:\n transactionId=%s\n customerId=%s\n amount=%f\n currency=%s\n timestamp=%s\n notes=%s\n",
                transactionId, customerId, amount, currency, timestamp, notes);
    }

    private Transaction(TransactionBuilder builder) {
        transactionId = builder.transactionId;
        customerId = builder.customerId;
        amount = builder.amount;

        currency = builder.currency;
        timestamp = builder.timestamp;
        notes = builder.notes;
    }

    public static class TransactionBuilder {
        private final String transactionId;
        private final String customerId;
        private final double amount;

        // optional
        private String currency;
        private String timestamp;
        private String notes;

        public TransactionBuilder(String transactionId, String customerId, double amount) {
            this.transactionId = transactionId;
            this.customerId = customerId;
            this.amount = amount;
        }

        public TransactionBuilder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public TransactionBuilder setTimestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public TransactionBuilder setNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }

}
