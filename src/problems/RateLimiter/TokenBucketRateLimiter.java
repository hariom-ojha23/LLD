package problems.RateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class TokenBucket {
    private final BucketConfig config;
    private long lastRefillTimeMs;
    private long availableTokens;

    public TokenBucket(BucketConfig config) {
        this.config = config;
        this.availableTokens = config.capacity();
        this.lastRefillTimeMs = System.currentTimeMillis();
    }

    public void refillToken() {
        double elapsedTime = (System.currentTimeMillis() - this.lastRefillTimeMs) / 1000.0;
        long tokensToAdd = (long) (elapsedTime * config.refillRate());

        // Available token cannot be more than capacity
        availableTokens = Math.min(config.capacity(), availableTokens + tokensToAdd);

        // Update last refill time.
        // It preserves fractional time left toward next token
        long consumedTimeMs = (long) (tokensToAdd * 1000.0 / config.refillRate());
        lastRefillTimeMs += consumedTimeMs;
    }

    /**
     *
     * Refill token based on elapsed time
     * If tokens available consume and return true
     * if tokens not available return false
     */
    public synchronized boolean tryConsume() {
        refillToken();

        if (availableTokens <= 0) {
            return false;
        }

        availableTokens--;
        return true;
    }
}

public class TokenBucketRateLimiter implements IRateLimiter {
    private final long capacity;
    private final double refillRate;

    private final Map<String, TokenBucket> tokenBucketMap = new ConcurrentHashMap<>();

    public TokenBucketRateLimiter(long capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
    }

    /**
     * Get the client's token bucket
     * If bucket not available, create one
     * Consume a token if available and allow request
     * Return true/false
     */
    @Override
    public boolean allowRequest(String clientId) {

        // If client do not have bucket, assign one
        TokenBucket clientBucket = tokenBucketMap.computeIfAbsent(
                clientId,
                id -> new TokenBucket(new BucketConfig(capacity, refillRate))
        );

        return clientBucket.tryConsume();
    }
}

