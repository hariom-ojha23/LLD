package problems.CacheEviction;

import java.util.HashMap;
import java.util.Map;

class LRUCacheEviction implements Cache {
    private final Node head;
    private final Node tail;

    private int capacity = 3;

    private final Map<Integer, Node> cache = new HashMap<>();

    public LRUCacheEviction(int capacity) {
        head = new Node(0, "", null, null);
        tail = new Node(0, "", null, head);
        tail.setPrev(head);
        head.setNext(tail);

        if (capacity <= 0) {
            System.out.println("Capacity cannot be less than 1");
            return;
        }

        this.capacity = capacity;
    }

    @Override
    public synchronized String get(int key) {
        // If key doesn't exist throw error
        if (!cache.containsKey(key)) {
            return "Key doesn't exist";
        }

        // Get valueNode and its value
        Node valueNode = cache.get(key);
        String value = valueNode.getValue();

        // Update valueNode order
        remove(valueNode);
        addInFront(valueNode);

        // Return key
        return value;
    }

    @Override
    public synchronized void put(int key, String value) {
        System.out.println("Adding Key " + key);

        // if key exist update value
        if (cache.containsKey(key)) {
            System.out.println("Key " + key + " already exist. Updating value");
            // remove old node
            remove(cache.get(key));

            // add new node
            addNewNode(key, value);

            return;
        }

        // If cache is not full add value
        if (cache.size() < capacity) {
            addNewNode(key, value);
            return;
        }

        // If cache is full:
        // Remove LRU and its cache and add new value
        System.out.println("Cache is full. Removing LRU key: " + tail.getPrev().getKey());
        cache.remove(tail.getPrev().getKey());
        remove(tail.getPrev());
        addNewNode(key, value);
    }

    private void addNewNode(int key, String value) {
        // create new node
        Node newValueNode = new Node(key, value, null, null);

        // add new node in order
        addInFront(newValueNode);

        // update new node in cache
        cache.put(key, newValueNode);
    }

    private void remove(Node node) {
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
    }

    private void addInFront(Node node) {
        node.setNext(head.getNext());
        head.getNext().setPrev(node);

        head.setNext(node);
        node.setPrev(head);
    }
}


public class LRUCache {
    public static void main(String... args) {
        Cache cache = new LRUCacheEviction(3);
        cache.put(1, "Apple");
        cache.put(2, "Banana");
        cache.put(3, "Mango");

        System.out.println("Get 1: " + cache.get(1));

        // Evicts key 2 (Banana)
        cache.put(4, "Orange");

        System.out.println("Get 1: " + cache.get(1));
        System.out.println("Get 2: " + cache.get(2));
        System.out.println("Get 3: " + cache.get(3));
        System.out.println("Get 4: " + cache.get(4));
    }
}
