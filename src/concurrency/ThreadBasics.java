package concurrency;

/**
 * 
 * ThreadBasics
 * 
 * A thread is a smallest unit of execution inside a process
 * There can be multiple thread executing tasks concurrently
 *
 * 
 * thread.start() vs thread.run()
 * 
 * thread.start()
 * - Requests the JVM to start a new thread.
 * - The new thread becomes eligible to be scheduled.
 * - The JVM/OS scheduler determines when it gets CPU time.
 * - The new thread executes run().
 *
 * thread.run()
 * - It is a normal method call.
 * - It does NOT create a new thread.
 * - Its code executes on the current thread.
 * 
 * 
 * Multiple threads
 * - Can execute concurrently.
 * - Execution order is not deterministic.
 * 
 * 
 * Thread.sleep(time)
 * - Pauses the CURRENT thread.
 * - Puts thread into TIMED_WAITING state.
 * - Other threads can continue executing.
 * - Does NOT release a lock.
 * - Can throw InterruptedException.
 * 
 * sleep()
 * - "Pause myself"
 * 
 * join()
 * - "Wait for another thread"
 * 
 * 
 */

public class ThreadBasics {
    public static void main(String[] args) {
        System.out.println("Main thread started");

        Runnable task1 = () -> {
            for (int i = 1; i < 6; i++) {
                try {
                    System.out.println("Thread 1: " + i);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Runnable task2 = () -> {
            for (int i = 101; i < 106; i++) {
                try {
                    System.out.println("Thread 2: " + i);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();

        System.out.println("Main thread finished");
    }
}
