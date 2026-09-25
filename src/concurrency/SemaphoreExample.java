package concurrency;

import java.util.concurrent.Semaphore;

/**
 * 
 * Semaphore
 * 
 * Semaphore
 *
 * A semaphore controls how many threads can access a resource
 * concurrently by maintaining a set of permits.
 *
 * Each thread must acquire a permit before accessing the resource
 * and release the permit after finishing.
 * 
 */

class SimpleSemaphoreExample {
    private final Semaphore semaphore = new Semaphore(2);

    public void execute(int threadId) {
        boolean acquire = false;

        try {
            System.out.println("Thread " + threadId + " is waiting for permit");

            semaphore.acquire();
            acquire = true;

            System.out.println("Thread " + threadId + " acquired permit");

            Thread.sleep(2000);

            System.out.println("Thread " + threadId + " has finished work");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (acquire) {
                System.out.println("Thread " + threadId + " released permit");
                semaphore.release();
            }
        }
    }
}

public class SemaphoreExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main thread starting");

        final SimpleSemaphoreExample semaphoreExample = new SimpleSemaphoreExample();

        Thread[] threads = new Thread[4];

        for (int i = 0; i < 4; i++) {
            int threadId = i + 1;

            threads[i] = new Thread(() -> {
                semaphoreExample.execute(threadId);
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Main thread ending");
    }
}
