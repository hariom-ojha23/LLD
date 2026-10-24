package problems.RateLimiter;

public interface IRateLimiter {
    boolean allowRequest(String clientId);
}
