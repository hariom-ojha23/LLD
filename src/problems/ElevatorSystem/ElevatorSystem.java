package problems.ElevatorSystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.lang.Thread;

/**
 * 
 * ElevatorSystem
 * 
 * 
 * Functional requirement:
 * 1. It should support multiple elevators - Strategy
 * 2. It should be able to use multiple scheduling algorithm - Strategy
 * 3. It should be able to handle internal and external requests
 * 4. It should have a display to show current floor - Observer
 * 
 * Not functional requirement:
 * 1. The system should be able to handle concurrent requests
 * 2. Each elevator should run independently
 * 3. System should be extensible
 * 4. Code must follow SOLID Principle
 * 5. Code must be maintainable
 * 
 * Class / Interfaces
 * 
 * ElevatorSystem
 * Elevator
 * SchedulingAlgorithm (interface)
 * Request (record)
 * ElevatorState (enum)
 * ElevatorDisplay
 * Direction (enum)
 * .....
 * 
 * 
 */

/**
 * Interfaces
 */
enum Direction {
    UP,
    DOWN,
    IDLE
}

interface SchedulingAlgorithm {
    public Elevator getElevator(List<Elevator> elevators, RequestType request);
}

interface ElevatorDisplayObserver {
    public void onFloorChange(int elevatorId, int floor, Direction direction);
}

/**
 * 
 * Records
 */
record RequestType(Direction direction, int floor) {
}

record ElevatorSnapshot(int currentFloor, Direction direction, int pendingStops) {
}

/**
 * 
 * NearFit
 */
class NearFit implements SchedulingAlgorithm {

    @Override
    public Elevator getElevator(List<Elevator> elevators, RequestType request) {
        Elevator selectedElevator = null;
        int minCost = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            int currentCost = calculateCost(elevator, request);

            if (currentCost < minCost) {
                selectedElevator = elevator;
                minCost = currentCost;
            }
        }

        return selectedElevator;
    }

    /**
     * cost = distance + directionPenality + pendingStops
     */
    private int calculateCost(Elevator elevator, RequestType request) {

        ElevatorSnapshot snapshot = elevator.getSnapshot();

        boolean isSameOrIdle = snapshot.direction() == request.direction() || snapshot.direction() == Direction.IDLE;

        int directionPenality = isSameOrIdle ? 0 : 2;
        int distance = Math.abs(snapshot.currentFloor() - request.floor());
        int pendingStops = snapshot.pendingStops();

        int cost = distance + directionPenality + pendingStops;
        return cost;
    }
}

// using strategy pattern
class SchedulingAlgorithmService {
    private final SchedulingAlgorithm algorithm;

    public SchedulingAlgorithmService(SchedulingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Elevator getElevator(List<Elevator> elevators, RequestType request) {
        return algorithm.getElevator(elevators, request);
    }
}

/**
 * 
 * Elevator
 */
class Elevator implements Runnable {
    private final int id;

    // using min heap
    PriorityQueue<RequestType> upStops = new PriorityQueue<>(Comparator.comparingInt(RequestType::floor));

    // using max heap
    PriorityQueue<RequestType> downStops = new PriorityQueue<>(Comparator.comparingInt(RequestType::floor).reversed());

    private int currentFloor = 1;
    private Direction currentDirection = Direction.IDLE;

    private List<ElevatorDisplayObserver> elevatorDisplays = new ArrayList<>();

    public Elevator(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                while (upStops.isEmpty() && downStops.isEmpty()) {
                    try {
                        wait();
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            while (!upStops.isEmpty()) {
                currentDirection = Direction.UP;
                goUp();
            }

            currentDirection = Direction.IDLE;
            notifyFloorChange(currentFloor, currentDirection);

            while (!downStops.isEmpty()) {
                currentDirection = Direction.DOWN;
                goDown();
            }

            if (currentDirection != Direction.IDLE) {
                currentDirection = Direction.IDLE;
                notifyFloorChange(currentFloor, currentDirection);
            }
        }
    }

    private void step() {
        try {
            Thread.sleep(1200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goUp() {
        while (true) {
            RequestType request;

            synchronized (this) {
                request = upStops.peek();

                if (request == null) {
                    break;
                }

                if (request.floor() == currentFloor) {
                    upStops.poll();
                    openDoor();
                    continue;
                }

                currentFloor++;
            }

            notifyFloorChange(currentFloor, currentDirection);
            step();
        }

    }

    private void goDown() {
        while (true) {
            RequestType request;

            synchronized (this) {
                request = downStops.peek();

                if (request == null) {
                    break;
                }

                if (request.floor() == currentFloor) {
                    downStops.poll();
                    openDoor();
                    continue;
                }

                currentFloor--;
            }

            notifyFloorChange(currentFloor, currentDirection);
            step();
        }
    }

    private void openDoor() {
        System.out.println(
                String.format("\nDown: Opening door of elevator %s on %s floor\n", this.id, this.currentFloor));
    }

    public synchronized void selectFloor(int floor) {
        if (floor == currentFloor) {
            openDoor();
            return;
        }

        Direction direction = floor > currentFloor ? Direction.UP : Direction.DOWN;
        RequestType request = new RequestType(direction, floor);

        if (direction == Direction.UP) {
            upStops.add(request);
        } else {
            downStops.add(request);
        }

        notify();
    }

    public void requestElevator(RequestType request) {
        synchronized (this) {
            // open door if the request is for the current floor
            if (currentFloor == request.floor()) {
                openDoor();
                return;
            }

            /**
             * if going up and request is above and on the way add in upStops
             * 
             * if going down and request is below and on the way add in downStops
             * 
             * if direction is idle, add in upStops or downStops based on the
             * difference between the request floor and current floor
             * 
             * if elevator is going up and request is above and on the way add in upStops
             */

            if (currentDirection == Direction.IDLE) {
                if (request.floor() > currentFloor) {
                    currentDirection = Direction.UP;
                    upStops.add(request);
                } else {
                    currentDirection = Direction.DOWN;
                    downStops.add(request);
                }
            } else if (currentDirection == Direction.UP && request.floor() > currentFloor) {
                upStops.add(request);
            } else if (currentDirection == Direction.DOWN && request.floor() < currentFloor) {
                downStops.add(request);
            } else {
                System.out.println(
                        String.format("Invalid request for elevator %s on floor %s", this.id, request.floor()));
            }

            notify();
        }
    }

    public void addElevatorDisplay(ElevatorDisplayObserver observer) {
        elevatorDisplays.add(observer);
    }

    public void removeElevatorDisplay(ElevatorDisplayObserver observer) {
        elevatorDisplays.remove(observer);
    }

    private void notifyFloorChange(int floor, Direction direction) {
        for (ElevatorDisplayObserver observer : elevatorDisplays) {
            observer.onFloorChange(this.id, floor, direction);
        }
    }

    public synchronized ElevatorSnapshot getSnapshot() {
        return new ElevatorSnapshot(currentFloor, currentDirection, upStops.size() + downStops.size());
    }
}

/**
 * 
 * ElevatorDisplay
 * 
 */
class ElevatorDisplay implements ElevatorDisplayObserver {
    @Override
    public void onFloorChange(int elevatorId, int floor, Direction direction) {
        System.out.println(
                "DISPLAY: Elevator " + elevatorId +
                        " | Floor: " + floor +
                        " | Direction: " + direction);
    }
}

/**
 * 
 * ElevatorController
 */
class ElevatorController {
    private List<Elevator> elevators = new ArrayList<>();
    private List<Thread> threads = new ArrayList<>();
    private SchedulingAlgorithmService schedulingAlgoService;

    public ElevatorController(List<Elevator> elevators, SchedulingAlgorithmService service) {
        this.elevators = elevators;
        this.schedulingAlgoService = service;
    }

    public void startElevators() {
        for (Elevator elevator : elevators) {
            Thread thread = new Thread(elevator);
            threads.add(thread);
            thread.start();
        }
    }

    public void requestElevator(RequestType request) {
        Elevator elevator = schedulingAlgoService.getElevator(elevators, request);
        elevator.requestElevator(request);
    }
}

/**
 * 
 * ElevatorSystem
 */
public class ElevatorSystem {
    public static void main(String[] args) throws InterruptedException {
        Elevator elevator1 = new Elevator(1);
        Elevator elevator2 = new Elevator(2);

        ElevatorDisplay display = new ElevatorDisplay();

        elevator1.addElevatorDisplay(display);
        elevator2.addElevatorDisplay(display);

        SchedulingAlgorithm algorithm = new NearFit();
        SchedulingAlgorithmService service = new SchedulingAlgorithmService(algorithm);

        ElevatorController controller = new ElevatorController(List.of(elevator1, elevator2), service);

        controller.startElevators();
        controller.requestElevator(new RequestType(Direction.UP, 10));
        controller.requestElevator(new RequestType(Direction.DOWN, 5));

        Thread.sleep(10000);

        controller.requestElevator(new RequestType(Direction.DOWN, 3));
        controller.requestElevator(new RequestType(Direction.UP, 2));

        elevator1.selectFloor(4);
        elevator2.selectFloor(9);
    }
}
