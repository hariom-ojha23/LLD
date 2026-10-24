package problems.RateLimiter;

public record BucketConfig(long capacity, double refillRate) {
    public BucketConfig {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }

        if (!Double.isFinite(refillRate) || refillRate <= 0) {
            throw new IllegalArgumentException("Refill rate must be positive");
        }
    }
}
