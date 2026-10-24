package problems.RateLimiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Functional Requirement
 *
 * Allow or reject an incoming request
 * Configure bucket capacity
 * Configure token refill rate
 * Refill token automatically based on elapsed time
 *
 * Non-Functional Requirement
 *
 * Thread safe for multiple concurrent requests
 * Clean code: SOLID Principles
 * Extensible
 *
 * Core Classes/Interface
 * RateLimiter (Interface)
 * TokenBucketRateLimiter
 * Bucket
 * BucketConfig
 *
 */

class RateLimiterStrategy {
    private final IRateLimiter rateLimiter;

    public RateLimiterStrategy(IRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    public boolean allowRequest(String clientId) {
        return rateLimiter.allowRequest(clientId);
    }
}


public class RateLimiter {

    public static void main(String[] args) {
        IRateLimiter tokenBucketRateLimiter = new TokenBucketRateLimiter(2, 1.0);
        RateLimiterStrategy rateLimiter =  new RateLimiterStrategy(tokenBucketRateLimiter);

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int thread = 1; thread <= 3; thread++) {
            final int threadId = thread;

            executor.submit(() -> {
                for (int i = 1; i <= 3; i++) {
                    boolean allowed = rateLimiter.allowRequest("user-" + threadId);

                    System.out.printf(
                            "Thread-%d | %s | Request-%d | %s%n",
                            threadId,
                            "user-" + threadId,
                            i,
                            allowed ? "ALLOWED" : "REJECTED"
                    );
                }
            });
        }

        executor.shutdown();
    }
}