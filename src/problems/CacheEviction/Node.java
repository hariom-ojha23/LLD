package problems.CacheEviction;

public class Node{
    private Node prev;
    private Node next;
    private int key;
    private String value;

    public Node(int key, String value, Node next, Node prev) {
        this.key = key;
        this.value = value;
        this.prev = prev;
        this.next = next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
