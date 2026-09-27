package concurrency;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * Atomic
 * 
 * Atomics give indivisible read-modify-write without a lock, using a hardware
 * primitive.
 * 
 */

class Counter {
    AtomicInteger count = new AtomicInteger();

    public void increment() {
        count.getAndIncrement();
    }

    public int getCount() {
        return count.get();
    }
}

public class Atomic {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main thread starting");

        Counter counter = new Counter();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Counter value: " + counter.getCount());

        System.out.println("Main thred ending");
    }

}