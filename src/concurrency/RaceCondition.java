package concurrency;

/**
 *
 * RaceCondition
 *
 * A race condition occurs when multiple threads access shared data
 * concurrently and the program's result depends on the timing/order
 * of the operations.
 *
 * Race condition = Multiple threads + shared mutable data + lack of proper
 * synchronization
 *
 *
 * Shared Resource:
 * - Data that can be accessed by multiple threads.
 *
 * Example:
 * count++;
 *
 * count++ is NOT atomic.
 *
 * Internally:
 * Read -> Modify -> Write
 *
 *
 * Race condition can cause:
 * - Lost updates
 * - Incorrect results
 * - Non-deterministic behavior
 *
 *
 * join():
 * - Makes the current thread wait for another thread.
 * - It does NOT make shared data thread-safe.
 *
 *
 * Thread Safety:
 * - A program behaves correctly when multiple threads
 * access shared data concurrently.
 *
 *
 * synchronized:
 * - Protects a critical section from concurrent access by multiple threads
 * using the same lock.
 * 
 * - Only one thread at a time can hold a particular
 * intrinsic lock (monitor).
 * 
 * - Uses an intrinsic lock (monitor).
 *
 * 
 * Instance synchronized method:
 * - Locks the current object (this).
 *
 * Static synchronized method:
 * - Locks the Class object.
 *
 * Example:
 *
 * public synchronized void increment() { count++; }
 *
 * is equivalent to:
 *
 * synchronized (this) { count++; }
 *
 */

class ThreadUnsafeCounter {

    int count = 0;

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

class ThreadSafeCounter {
    int count = 0;

    public synchronized void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

public class RaceCondition {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main thread starting");

        ThreadUnsafeCounter threadUnsafeCounter = new ThreadUnsafeCounter();
        ThreadSafeCounter threadSafeCounter = new ThreadSafeCounter();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                threadUnsafeCounter.increment();
                threadSafeCounter.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                threadUnsafeCounter.increment();
                threadSafeCounter.increment();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Count: " + threadUnsafeCounter.getCount());
        System.out.println("Thread safe count: " + threadSafeCounter.getCount());

        System.out.println("Main thread ending");
    }

}
