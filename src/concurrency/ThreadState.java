package concurrency;

/**
 * 
 * ThreadState
 * 
 * NEW: thread object created, start() not called
 * 
 * RUNNABLE: thread is ready to run or currently running
 * 
 * BLOCKED: waiting to acquire monitor/intrinsic lock
 * 
 * WAITING: waiting indefinitely for another thread/event
 * 
 * TIMED_WAITING: waiting for a specified time period
 * 
 * TERMINATED: run() has finished or terminated due to exception.
 * 
 * 
 * NEW
 * ↓ start()
 * RUNNABLE
 * ↓ sleep()
 * TIMED_WAITING
 * ↓ timeout
 * RUNNABLE
 * ↓ run() finishes
 * TERMINATED
 * 
 * 
 * join()
 * → WAITING
 * 
 * join(timeout)
 * → TIMED_WAITING
 * 
 * sleep(timeout)
 * → TIMED_WAITING
 * 
 * synchronized lock unavailable
 * → BLOCKED
 * 
 * 
 */
public class ThreadState {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main thread");

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        System.out.println("Before start: " + thread.getState());
        thread.start();
        System.out.println("After start: " + thread.getState());
        Thread.sleep(500);
        System.out.println("While sleeping: " + thread.getState());
        thread.join();
        System.out.println("After completion: " + thread.getState());
    }
}
