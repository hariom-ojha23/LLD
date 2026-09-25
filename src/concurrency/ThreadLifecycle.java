package concurrency;

/**
 * 
 * ThreadLifecycle
 * 
 * Thread.sleep(time)
 * - Pauses the CURRENT thread.
 * - Puts thread into TIMED_WAITING state.
 * - Other threads can continue executing.
 * - Does NOT release a lock.
 * - Can throw InterruptedException.
 * 
 * join()
 * - Makes the CURRENT thread wait for another thread.
 * - Does not stop the target thread.
 * - Does not create a new thread.
 * - join() waits until target thread terminates.
 * - join(timeout) waits for AT MOST the specified time.
 * 
 * Important:
 * 
 * thread1.start();
 * thread1.join();
 * thread2.start();
 * 
 * → Thread 1 and Thread 2 execute sequentially.
 * 
 * thread1.start();
 * thread2.start();
 * thread1.join();
 * thread2.join();
 * 
 * → Thread 1 and Thread 2 can execute concurrently.
 * → Main waits for both to finish.
 * 
 *
 * interrupt()
 * - Used to request a thread to interrupt its current operation.
 * - Does NOT forcibly kill or stop the thread.
 * - If thread is sleeping/waiting/joining, it may throw InterruptedException.
 * - If thread is doing normal work, it can check isInterrupted().
 * - InterruptedException clears the interrupt status.
 * - We can restore it using:
 * Thread.currentThread().interrupt();
 * 
 * interrupted() vs isInterrupted()
 * 
 * interrupted():
 * - Static method (belongs to Thread class).
 * - Checks if the CURRENT thread has been interrupted.
 * - Clears the interrupt status if interrupted.
 * 
 * isInterrupted():
 * - Instance method (belongs to Thread object).
 * - Checks if the SPECIFIC thread has been interrupted.
 * - Does NOT clear the interrupt status.
 * 
 * 
 * sleep()
 * → "Pause me."
 * 
 * join()
 * → "Wait for that thread."
 * 
 * interrupt()
 * → "Hey, wake up/stop waiting and handle this interruption."
 * 
 * 
 */

public class ThreadLifecycle {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main thread started");

        Runnable task1 = () -> {
            for (int i = 1; i < 6; i++) {
                try {
                    System.out.println("Thread 1: " + i);

                    /**
                     * Pauses this thread execution for 1000ms
                     */
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Runnable task2 = () -> {
            for (int i = 101; i < 106; i++) {
                System.out.println("Thread 2: " + i);
            }
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();

        /**
         * Current thread (main) waits for thread 1 to finish
         */
        thread1.join();

        /**
         * Current thread (main) waits for thread 2 for 500ms
         * If thread 2 doesn't finish within 500ms, main thread will continue
         * execution
         */
        thread2.join(500);

        System.out.println("Main thread finished");
    }
}
