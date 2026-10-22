package problems.CacheEviction;

public interface Cache {
    String get(int key);
    void put(int key, String value);
}
