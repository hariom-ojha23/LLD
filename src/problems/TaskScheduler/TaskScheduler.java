package problems.TaskScheduler;

import java.util.Comparator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/// Functional Requirements
/// 1. Schedule a task for a specific time
/// 2. Schedule & support different types of task (fixed/recurring)
/// 3. Task can have different Priorities
/// 4. Retry failed tasks with configurable retry limit
/// 5. Track task status: Scheduled, Running, Completed, failed.
///
/// Non-Functional Requirements
///
/// 1. Thread safe
/// 2. Extensible
/// 3. Follow SOLID principles
/// 4. No busy waiting
///
/// Core Classes/Interface/Enums
/// Task (Interface)
/// ScheduledTask (Model class)
/// TaskManager (ManagerClass)
/// TaskExecutor
/// TaskStatus (Enum)
/// TaskPriority (Enum)
///

enum TaskStatus {
    SCHEDULED,
    RUNNING,
    COMPLETED,
    FAILED
}

enum TaskPriority {
    LOW,
    MEDIUM,
    HIGH
}

interface Task {
    void execute() throws InterruptedException;
}

class ScheduledTask {
    private final String id;
    private final String title;
    private final Task task;
    private long executeAt;
    private long interval;
    private final int maxRetry;
    private int retryCount = 0;
    private final TaskPriority priority;
    private TaskStatus status;
    private boolean executing;

    public ScheduledTask(
            int id,
            String title,
            Task task,
            long executeAt,
            long interval,
            int maxRetry,
            TaskPriority priority
    ) {
        this.id = "Task-" + id;
        this.title = title;
        this.task = task;
        this.executeAt = executeAt;
        this.interval = interval;
        this.maxRetry = maxRetry;
        this.priority = priority;
        this.status = TaskStatus.SCHEDULED;
    }

    public void execute() throws Exception {
        task.execute();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getExecuteAt() {
        return executeAt;
    }

    public long getInterval() {
        return interval;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setRetryCount(int count) {
        this.retryCount = count;
    }

    public void setExecuteAt(long executeAt) {
        this.executeAt = executeAt;
    }

    public synchronized boolean startExecution() {
        if (executing) {
            return false;
        }

        executing = true;
        status = TaskStatus.RUNNING;
        return true;
    }

    public synchronized void finishExecution() {
        executing = false;
    }
}

/**
 * Singleton Class
 */
class TaskManager {
    private static final Comparator<ScheduledTask> COMPARATOR = Comparator
            .comparingLong(ScheduledTask::getExecuteAt)
            .thenComparing(
                    ScheduledTask::getPriority,
                    Comparator.comparingInt(TaskPriority::ordinal).reversed()
            );

    private final PriorityBlockingQueue<ScheduledTask> tasks = new PriorityBlockingQueue<>(
            11,
            COMPARATOR
    );
    private final PriorityBlockingQueue<ScheduledTask> retryTasks = new PriorityBlockingQueue<>(
            11,
            COMPARATOR
    );

    private final ReentrantLock scheduledLock = new ReentrantLock();
    private final ReentrantLock retryLock = new ReentrantLock();

    private final Condition scheduledChanged = scheduledLock.newCondition();
    private final Condition retryChanged = retryLock.newCondition();

    // private constructor
    private TaskManager() {}

    private static class Holder {
        public static final TaskManager instance = new TaskManager();
    }

    public static TaskManager getInstance() {
        return Holder.instance;
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public void addTask(ScheduledTask task) {
        scheduledLock.lock();

        try {
            ScheduledTask previous = tasks.peek();
            tasks.offer(task);

            // Wake scheduler if new task is earlier
            if (previous == null || COMPARATOR.compare(task, previous) < 0) {
                scheduledChanged.signal();
            }

        } finally {
            scheduledLock.unlock();
        }
    }

    public void addRetryTask(ScheduledTask task) {
        retryLock.lock();

        try {
            ScheduledTask previous = retryTasks.peek();
            retryTasks.offer(task);

            if (previous == null || COMPARATOR.compare(task, previous) < 0) {
                retryChanged.signal();
            }
        } finally {
            retryLock.unlock();
        }
    }

    public ScheduledTask getNextScheduledTask() throws InterruptedException {
        scheduledLock.lockInterruptibly();

        try {
            while (true) {
                ScheduledTask task = tasks.peek();

                if (task == null) {
                    scheduledChanged.await();
                    continue;
                }

                long delay = task.getExecuteAt() - System.currentTimeMillis();

                if (delay > 0) {
                    scheduledChanged.awaitNanos(delay * 1_000_000L);
                    continue;
                }

                return tasks.poll();
            }
        } finally {
            scheduledLock.unlock();
        }
    }

    public ScheduledTask getNextRetryTask() throws InterruptedException {
        retryLock.lockInterruptibly();

        try {
            while (true) {
                ScheduledTask task = retryTasks.peek();

                if (task == null) {
                    retryChanged.await();
                    continue;
                }

                long delay = task.getExecuteAt() - System.currentTimeMillis();

                if (delay > 0) {
                    retryChanged.awaitNanos(delay * 1_000_000L);
                    continue;

                }

                return retryTasks.poll();
            }
        } finally {
            retryLock.unlock();
        }
    }
}

/**
 * Singleton class
 */
class TaskExecutor implements Runnable {
    TaskManager manager = TaskManager.getInstance();
    ExecutorService taskExecutors = Executors.newFixedThreadPool(2);
    ExecutorService retryTaskExecutor = Executors.newSingleThreadExecutor();

    private volatile boolean running = true;

    // private constructor
    private TaskExecutor() {}

    private static class Holder {
        public static final TaskExecutor instance = new TaskExecutor();
    }

    public static TaskExecutor getInstance() {
        return Holder.instance;
    }

    @Override
    public void run() {
        Thread scheduledThread = new Thread(this::processScheduledTasks);
        Thread retryThread = new Thread(this::processRetryTasks);

        scheduledThread.start();
        retryThread.start();
    }

    private void processScheduledTasks() {
        while(running && !Thread.currentThread().isInterrupted()) {
            try {
                ScheduledTask task = manager.getNextScheduledTask();
                taskExecutors.submit(() -> executeTask(task));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void processRetryTasks() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                ScheduledTask task = manager.getNextRetryTask();
                retryTaskExecutor.submit(() -> executeRetry(task));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void executeTask(ScheduledTask task) {

        if (!task.startExecution()) {
            return;
        }

        System.out.println(String.format(
                "[STARTED] id=%s | title=%s | priority=%s",
                task.getId(),
                task.getTitle(),
                task.getPriority()
        ));

        boolean success = false;

        try {
            task.execute();
            success = true;

            task.setStatus(TaskStatus.COMPLETED);

            System.out.println(String.format(
                    "[COMPLETED] id=%s | title=%s",
                    task.getId(),
                    task.getTitle()
            ));

        } catch (Exception e) {

            System.out.println("WARN: " + String.format(
                    "[FAILED] id=%s | title=%s | error=%s",
                    task.getId(),
                    task.getTitle(),
                    e.getMessage()
            ));

            scheduleRetry(task);

        } finally {
            task.finishExecution();
        }

        if (success) {
            scheduleNextOccurrence(task);
        }
    }

    private void executeRetry(ScheduledTask task) {

        if (!task.startExecution()) {
            return;
        }

        System.out.println(String.format(
                "[RETRY STARTED] id=%s | title=%s | attempt=%d/%d",
                task.getId(),
                task.getTitle(),
                task.getRetryCount(),
                task.getMaxRetry()
        ));

        boolean success = false;

        try {
            task.execute();
            success = true;

            task.setStatus(TaskStatus.COMPLETED);

            System.out.println(String.format(
                    "[RETRY SUCCESS] id=%s | title=%s",
                    task.getId(),
                    task.getTitle()
            ));

        } catch (Exception e) {

            System.out.println("WARN: " + String.format(
                    "[RETRY FAILED] id=%s | attempt=%d | error=%s",
                    task.getId(),
                    task.getRetryCount(),
                    e.getMessage()
            ));

            scheduleRetry(task);

        } finally {
            task.finishExecution();
        }

        if (success) {
            scheduleNextOccurrence(task);
        }
    }

    private void scheduleRetry(ScheduledTask task) {

        if (task.getRetryCount() < task.getMaxRetry()) {
            task.setRetryCount(task.getRetryCount() + 1);
            task.setExecuteAt(System.currentTimeMillis() + 5000);
            task.setStatus(TaskStatus.SCHEDULED);

            manager.addRetryTask(task);

            System.out.println(String.format(
                    "[RETRY SCHEDULED] id=%s | title=%s | attempt=%d/%d | retryAfter=5 seconds",
                    task.getId(),
                    task.getTitle(),
                    task.getRetryCount(),
                    task.getMaxRetry()
            ));

        } else {
            task.setStatus(TaskStatus.FAILED);
            System.out.println("ERROR: " + String.format(
                    "[PERMANENTLY FAILED] id=%s | title=%s | totalRetries=%d",
                    task.getId(),
                    task.getTitle(),
                    task.getRetryCount()
            ));
        }
    }

    private void scheduleNextOccurrence(ScheduledTask task) {
        if (task.getInterval() <= 0) {
            return;
        }

        task.setExecuteAt(
                System.currentTimeMillis() + task.getInterval()
        );

        task.setRetryCount(0);
        task.setStatus(TaskStatus.SCHEDULED);

        manager.addTask(task);

        System.out.println(String.format(
                "[RECURRING SCHEDULED] id=%s | title=%s | nextExecutionIn=%d ms",
                task.getId(),
                task.getTitle(),
                task.getInterval()
        ));
    }

    public void shutdown() {
        running = false;
        taskExecutors.shutdown();
        retryTaskExecutor.shutdown();
    }
}


public class TaskScheduler {

    public static void main(String[] args) throws InterruptedException {
        TaskExecutor executor = TaskExecutor.getInstance();
        TaskManager manager = TaskManager.getInstance();

        executor.run();

        long now = System.currentTimeMillis();

        // High priority: successful email
        ScheduledTask emailTask = new ScheduledTask(
                1,
                "Send Welcome Email",
                () -> {
                    Thread.sleep(1000);
                    System.out.println("Email successfully sent to user.");
                },
                now + 2000,
                0,
                3,
                TaskPriority.HIGH
        );

        // Low priority: successful cleanup
        ScheduledTask cleanupTask = new ScheduledTask(
                2,
                "Cleanup Temporary Files",
                () -> {
                    System.out.println("Temporary files cleaned.");
                },
                now + 2000,
                0,
                2,
                TaskPriority.LOW
        );

        // Fails once, succeeds on second attempt
        AtomicInteger flakyAttempts = new AtomicInteger(0);

        ScheduledTask flakyTask = new ScheduledTask(
                3,
                "Send Payment Notification",
                () -> {
                    int attempt = flakyAttempts.incrementAndGet();

                    if (attempt <= 1) {
                        throw new RuntimeException(
                                "Temporary payment service unavailable"
                        );
                    }

                    System.out.println("Payment notification sent successfully.");
                },
                now + 4000,
                0,
                3,
                TaskPriority.HIGH
        );

        // Recurring task: runs every 5 seconds
        ScheduledTask reportTask = new ScheduledTask(
                4,
                "Generate Daily Report",
                () -> {
                    System.out.println("Daily report generated.");
                },
                now + 6000,
                5000,
                3,
                TaskPriority.MEDIUM
        );

        // Schedule all tasks
        manager.addTask(emailTask);
        manager.addTask(cleanupTask);
        manager.addTask(flakyTask);
        manager.addTask(reportTask);

        System.out.println("====================================");
        System.out.println("All 5 tasks have been scheduled.");
        System.out.println("====================================");
    }
}
