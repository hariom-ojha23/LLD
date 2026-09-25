package concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * Locks
 * 
 * Key concepts:
 * 
 * - Lock is an interface which provides more control over locking than
 * synchronized
 * 
 * 
 * - unlock() should be called inside finally block, so that lock is
 * released even if an exception occurs
 * 
 * - A thread must only call unlock() if it successfully acquired lock.
 * Calling unlock without owning lock causes IllegalMonitorStateException
 * 
 * 
 * lock() vs tryLock() vs lockInterruptibly():
 * 
 * - lock(): wait until the lock becomes available
 * 
 * - tryLock(): 
 *   Attempt to acquire lock without waiting indefinitely.
 *   Returns true if lock acquired immediately, else false
 * 
 * - tryLock(timeout, unit):
 *   Attempt to acquire lock within specified timeout.
 *   Returns true if lock acquired within timeout, else false
 * 
 * - lockInterruptibly():
 *   Wait for the lock, but allows the waiting thread to be interrupted.
 * 
 * 
 * ReentrantLock:
 * - It means that a thread which already owns the lock can acquire the
 *   same lock again
 * 
 * - The lock maintain a lock count for owning thread
 * 
 * 
 * Lock vs Synchronized:
 * 
 * Synchronized:
 * - Automatic lock and release
 * - Simpler and less error-prone
 * 
 * Lock:
 * - Explicit lock() and unlock()
 * - Provides additional features such as tryLock()
 * - timed lock acquisition and lockInterruptibly()
 * 
 * 
 * Interview points:
 *  - Lock provides more flexibility than synchronized.
 *  - ReentrantLock allows the same thread to acquire the same lock
 *    multiple times.
 *  - Always release an acquired lock in finally.
 *  - Never call unlock() if the current thread did not acquire the lock.
 *  - tryLock() can prevent indefinite waiting.
 *  - tryLock(timeout) waits only for the specified duration.
 *  - lockInterruptibly() allows a thread waiting for a lock to
 *    respond to interruption.
 * 
 */

class SimpleLockCounter {
    private int count = 0;

    private final Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock();

        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        return count;
    }
}

class TryLockCounter {
    private int count = 0;

    private final Lock lock = new ReentrantLock();

    public void increment() {
        boolean acquired = false;

        try {
            acquired = lock.tryLock(1, TimeUnit.MILLISECONDS);

            if (acquired) {
                System.out.println("Acquired lock");
                count++;
            } else {
                System.out.println("Failed to acquire lock");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (acquired) {
                lock.unlock();
            }
        }
    }

    public int getCount() {
        return count;
    }
}

public class Locks {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main thread starting");

        SimpleLockCounter simpleLockCounter = new SimpleLockCounter();
        TryLockCounter tryLockCounter = new TryLockCounter();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                simpleLockCounter.increment();
                tryLockCounter.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                simpleLockCounter.increment();
                tryLockCounter.increment();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Simple counter value: " + simpleLockCounter.getCount());
        System.out.println("Try counter value: " + tryLockCounter.getCount());

        System.out.println("Main thread ending");
    }
}
