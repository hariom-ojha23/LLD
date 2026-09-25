package concurrency;

/**
 * synchronized
 *
 * 1. Instance synchronized method
 *
 * public synchronized void method() { }
 * Lock → this
 *
 *
 * 2. Synchronized block
 *
 * synchronized (this) { }
 * Lock → this
 *
 *
 * 3. Custom lock
 *
 * private final Object lock = new Object();
 *
 * synchronized (lock) { }
 * Lock → lock object
 *
 *
 * 4. Static synchronized method
 *
 * public static synchronized void method() { }
 * Lock → Class object
 *
 * Equivalent to:
 *
 * synchronized (MyClass.class) { }
 *
 *
 * Remember:
 *
 * Instance → Object lock
 * Static → Class lock
 *
 * Same lock → threads compete.
 * Different locks → threads can execute concurrently.
 *
 *
 * join()
 * → Makes the current thread wait.
 * → Does NOT make worker threads sequential.
 */

class CustomLockCounter {
    int count = 0;

    private final Object lock = new Object();

    public void increment() {
        synchronized (lock) {
            count++;
        }
    }

    public int getCount() {
        return count;
    }
}

class BlockLockCounter {
    int count = 0;

    public void increment() {
        synchronized (this) {
            count++;
        }
    }

    public int getCount() {
        return count;
    }
}

class StaticLockCounter {
    static int count = 0;

    public static void increment() {
        synchronized (StaticLockCounter.class) {
            count++;
        }
    }

    public static int getCount() {
        return count;
    }
}

public class Synchronized {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main thread starting");

        CustomLockCounter customLockCounter = new CustomLockCounter();
        BlockLockCounter blockLockCounter = new BlockLockCounter();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                customLockCounter.increment();
                blockLockCounter.increment();
                StaticLockCounter.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                customLockCounter.increment();
                blockLockCounter.increment();
                StaticLockCounter.increment();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("CustomLockCounter count: " + customLockCounter.getCount());
        System.out.println("BlockLockCounter count: " + blockLockCounter.getCount());
        System.out.println("StaticLockCounter count: " + StaticLockCounter.getCount());

        System.out.println("Main thread ending");
    }
}
