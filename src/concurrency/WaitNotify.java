package concurrency;

/**
 * WaitNotify
 *
 * Demonstrates inter-thread communication using wait(), notify(),
 * notifyAll() and synchronized.
 *
 * Key concepts:
 *
 * - wait(), notify() and notifyAll() are methods of the Object class,
 *   not the Thread class.
 *
 * - wait() must be called while the thread owns the object's monitor,
 *   normally inside a synchronized block.
 *
 * - When wait() is called, the thread:
 *      1. Releases the object's monitor.
 *      2. Enters the WAITING state.
 *      3. Waits until it is notified, interrupted, or otherwise awakened.
 *
 * - After being notified, the waiting thread does NOT immediately
 *   continue execution. It must first reacquire the same object's monitor.
 *
 * - notify() wakes one thread waiting on the object's monitor.
 *
 * - notifyAll() wakes all threads waiting on the object's monitor.
 *   They still have to compete to reacquire the monitor.
 *
 * - notify() does NOT transfer the lock immediately to the waiting thread.
 *   The notifying thread keeps the monitor until it exits the
 *   synchronized block.
 *
 * - wait() should normally be used inside a while loop instead of an if
 *   statement so that the condition is checked again after waking up.
 *
 * Example:
 *
 *     synchronized (lock) {
 *         while (message == null) {
 *             lock.wait();
 *         }
 *     }
 *
 * wait() vs sleep():
 *
 * - wait()    -> releases the monitor and enters WAITING.
 * - sleep()   -> does NOT release the monitor and enters TIMED_WAITING.
 *
 * wait() vs join():
 *
 * - wait()    -> waits for a condition/event and is used for
 *                inter-thread communication.
 * - join()    -> waits for another thread to finish.
 *
 * Important:
 *
 *     wait()  -> release lock + wait
 *     sleep() -> keep lock + sleep
 *     join()  -> wait for thread termination
 *
 * Interview points:
 *
 * - wait(), notify() and notifyAll() belong to Object.
 * - wait() releases the monitor.
 * - sleep() does not release the monitor.
 * - notify() wakes one waiting thread.
 * - notifyAll() wakes all waiting threads.
 * - A notified thread must reacquire the monitor before continuing.
 * - Calling wait()/notify()/notifyAll() without owning the monitor
 *   causes IllegalMonitorStateException.
 */
public class WaitNotify {
    private static final Object lock = new Object();
    private static String message;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main thread starting");

        Thread consumer = new Thread(() -> {
            synchronized (lock) {
                while (message == null) {
                    try {
                        System.out.println("Consumer: No message. Waiting...");
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                System.out.println("Consumer: Message received: " + message);
            }
        });

        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            synchronized (lock) {
                message = "Hello Java!";

                System.out.println("Producer: Message is ready");

                lock.notify();
            }
        });

        consumer.start();
        producer.start();

        consumer.join();
        producer.join();

        System.out.println("Main thread ending");
    }
}
