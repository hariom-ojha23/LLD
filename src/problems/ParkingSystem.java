package problems;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Functional Requirement:
 * 
 * 1. It should support multi level parking
 * 2. Each level should have different types of spots: Motorcycle, Car, Truck
 * 3. Parking lot should support multiple entry and exit gate
 * 4. Parking lot should charge at exit gate
 * 5. Support multiple spot assignment strategies
 * 6. Support multiple fee charge strategies
 * 7. Should support ticket generation
 * 
 * -- Optional--
 * - It should support multiple types of vehicle
 * - It should have display at each level
 * 
 * 
 * Non-Functional Requirement:
 * 
 * 1. Extensible
 * 2. SOLID Principle
 * 3. Thread safety
 * 4. No double spot assignment
 * 
 * 
 * Core Classes/Interface/Enums etc.:
 * 
 * ParkingLot
 * Level
 * Spot
 * Vehicle
 * VehicleType (enum)
 * EntryGate
 * ExitGate
 * PaymentService
 * SpotAssignmentStrategy
 * FeeChargeStrategy
 * Ticket
 * 
 * 
 * Relationship:
 * 
 * ParkingLot has Levels, exit & entry gate
 * Level has Spots
 * Spot has vehicles
 * Level has Gates
 * ParkingLot has PaymentService
 * ParkingLot has SpotAssignmentStrategy
 * ParkingLot has FeeChargeStrategy
 * ParkingLot has Ticket
 * 
 */

enum VehicleType {
    BIKE,
    CAR,
    TRUCK
}

enum TicketStatus {
    VALID,
    INVALID
}

interface SpotAssignmentStrategy {
    public Spot getAvailableSpot(List<Level> levels);
}

interface FeeCalculationStrategy {
    public int calculateFee(LocalDateTime entryDateAndTime, LocalDateTime exitDateAndTime);
}

interface Payment {
    public void pay(double amount);
}

abstract class ParkedVehicle {
    public Ticket ticket;

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return this.ticket;
    }
}

/**
 * 
 * FirstFreeSpotAssignment
 * 
 * FirstFreeSpotAssignment IS-A SpotAssignmentStrategy
 * 
 */
class FirstFreeSpotAssignment implements SpotAssignmentStrategy {
    public Spot getAvailableSpot(List<Level> levels) {
        for (Level level : levels) {
            Spot spot = level.getAvailableSpot();

            if (spot == null) {
                continue;
            }

            return spot;
        }

        return null;
    }
}

/**
 * 
 * SpotAssignmentService
 * 
 * Implements Strategy Pattern
 * 
 */
class SpotAssignmentService {
    private final SpotAssignmentStrategy strategy;

    public SpotAssignmentService(SpotAssignmentStrategy strategy) {
        this.strategy = strategy;
    }

    public Spot getSpot(List<Level> levels) {
        return this.strategy.getAvailableSpot(levels);
    }
}

/**
 * 
 * CalculateParkingFee
 * 
 * CalculateParkingFee IS-A FeeCalculationStrategy
 * 
 */
class ParkingFeeCalculation implements FeeCalculationStrategy {
    @Override
    public int calculateFee(LocalDateTime entryDateAndTime, LocalDateTime exitDateAndTime) {
        Duration duration = Duration.between(entryDateAndTime, exitDateAndTime);

        long hour = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        int perHourFee = 10;
        int totalFee = 0;

        if (minutes > 0) {
            totalFee = (int) (hour + 1) * perHourFee;
        } else {
            totalFee = (int) (hour * perHourFee);
        }

        return totalFee;
    }
}

/**
 * 
 * ParkingFeeCalculationService
 * 
 * Implements Strategy Pattern
 * 
 */
class ParkingFeeCalculationService {
    private final FeeCalculationStrategy strategy;

    public ParkingFeeCalculationService(FeeCalculationStrategy strategy) {
        this.strategy = strategy;
    }

    public int calculateFee(LocalDateTime entryDateAndTime, LocalDateTime exitDateAndTime) {
        return this.strategy.calculateFee(entryDateAndTime, exitDateAndTime);
    }
}

/**
 * 
 * ParkingLot
 * 
 * Singleton class
 * Thread safe
 * 
 */
class ParkingLot {
    private List<Level> levels = new ArrayList<>();
    private List<EntryGate> entryGates = new ArrayList<>();
    private List<ExitGate> exitGates = new ArrayList<>();

    private SpotAssignmentService spotAssignmentService;
    private ParkingFeeCalculationService parkingFeeCalculationService;
    private PaymentService paymentService;

    Map<Ticket, Spot> parkingMap = new HashMap<>();

    // private constructor
    private ParkingLot() {
    }

    static class ParkingLotInstance {
        private static ParkingLot instance = new ParkingLot();
    }

    // get singleton istance
    public static ParkingLot getInstance() {
        return ParkingLotInstance.instance;
    }

    /**
     * 
     * Get available spot
     * If no spot available throw error or null
     * Park vehicle on spot
     * 
     */
    public synchronized boolean parkVehicle(Vehicle vehicle) {
        Spot spot = spotAssignmentService.getSpot(levels);

        if (spot == null) {
            return false;
        }

        LocalDateTime currentDateAndTime = LocalDateTime.now();

        Ticket ticket = new Ticket(vehicle.getVehicleNo(), currentDateAndTime, TicketStatus.VALID);
        parkingMap.put(ticket, spot);

        spot.occupySpot();
        vehicle.setTicket(ticket);

        return true;
    }

    /**
     * 
     * Get spot from map
     * If no spot availble throw error
     * Calculate fee
     * Make Payment
     * Release spot
     */
    public synchronized boolean releaseVehicle(Ticket ticket) {
        if (ticket.isValid() == false) {
            return false;
        }

        Spot spot = parkingMap.get(ticket);

        if (spot == null) {
            return false;
        }

        LocalDateTime exitDateAndTime = LocalDateTime.now();
        int fee = parkingFeeCalculationService.calculateFee(ticket.getEntryDateAndTime(), exitDateAndTime);

        if (fee == 0) {
            spot.releaseSpot();
            ticket.inValidate();
            return true;
        }

        // Payment
        paymentService.pay(fee);

        spot.releaseSpot();
        parkingMap.remove(ticket);
        ticket.inValidate();

        return true;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    public void setEntryGates(List<EntryGate> entryGates) {
        this.entryGates = entryGates;
    }

    public void setExitGates(List<ExitGate> exitGates) {
        this.exitGates = exitGates;
    }

    public void setSpotAssignmentStrategy(SpotAssignmentService service) {
        this.spotAssignmentService = service;
    }

    public void setParkingFeeCalculationStrategy(ParkingFeeCalculationService service) {
        this.parkingFeeCalculationService = service;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}

/**
 * 
 * Level
 */
class Level {
    private final List<Spot> spots = new ArrayList<>();

    public Level(int totalSpots) {
        for (int i = 0; i < totalSpots; i++) {
            this.spots.add(new Spot());
        }
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public Spot getAvailableSpot() {
        for (Spot spot : spots) {
            if (spot.chekIfOccupied()) {
                continue;
            }
            return spot;
        }
        return null;
    }
}

/**
 * 
 * Spot
 */
class Spot {
    private boolean occupied = false;

    public void occupySpot() {
        if (chekIfOccupied()) {
            System.out.println("Cannot occupy spot. It is already occupied");
            return;
        }

        this.occupied = true;
    }

    public void releaseSpot() {
        if (!chekIfOccupied()) {
            System.out.println("Cannot release spot. It is not occupied.");
            return;
        }

        this.occupied = false;
    }

    public boolean chekIfOccupied() {
        return occupied;
    }
}

/**
 * 
 * Vehicle
 * 
 * Vehicle IS-A ParkedVehile
 */
class Vehicle extends ParkedVehicle {
    private final String vehicleNo;
    private final VehicleType vehicleType;

    public Vehicle(String vehicleNo, VehicleType type) {
        this.vehicleNo = vehicleNo;
        this.vehicleType = type;
    }

    public String getVehicleNo() {
        return this.vehicleNo;
    }

    public VehicleType getVehicleType() {
        return this.vehicleType;
    }
}

/**
 * 
 * Ticket
 */
class Ticket {
    private final String vehicleNo;
    private final LocalDateTime entryDateAndTime;
    private TicketStatus tickerStatus;

    public Ticket(String vehicleNo, LocalDateTime entryDateAndTime, TicketStatus ticketStatus) {
        this.vehicleNo = vehicleNo;
        this.entryDateAndTime = entryDateAndTime;
        this.tickerStatus = ticketStatus;
    }

    public LocalDateTime getEntryDateAndTime() {
        return entryDateAndTime;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public boolean isValid() {
        return tickerStatus == TicketStatus.VALID;
    }

    public void inValidate() {
        this.tickerStatus = TicketStatus.INVALID;
    }
}

/**
 * 
 * EntryGate
 */
class EntryGate {
    private final int entryGateNo;
    private final ParkingLot parkingLot;

    public EntryGate(int entryGateNo, ParkingLot parkingLot) {
        this.entryGateNo = entryGateNo;
        this.parkingLot = parkingLot;
    }

    public void parkVehicle(Vehicle vehicle) {
        boolean vehicleParked = this.parkingLot.parkVehicle(vehicle);
        if (vehicleParked) {
            System.out.println(
                    String.format("Entry Gate %s: Vehicle (%s) parked", entryGateNo, vehicle.getVehicleNo()));
        } else {
            System.out.println(
                    String.format("Entry Gate %s: Spot not available for vehicle (%s)", entryGateNo,
                            vehicle.getVehicleNo()));
        }
    }
}

/**
 * 
 * ExitGate
 */
class ExitGate {
    private final int exitGateNo;
    private final ParkingLot parkingLot;

    public ExitGate(int exitGateNo, ParkingLot parkingLot) {
        this.exitGateNo = exitGateNo;
        this.parkingLot = parkingLot;
    }

    public void releaseVehicle(Vehicle vehicle) {
        Ticket ticket = vehicle.getTicket();
        if (ticket == null || ticket.isValid() == false) {
            System.out.println(
                    "Exit Gate " + exitGateNo + ": Invalid ticket for vehicle (" + vehicle.getVehicleNo() + ")");
            return;
        }

        boolean vehicleReleased = this.parkingLot.releaseVehicle(vehicle.getTicket());
        if (vehicleReleased) {
            System.out.println(
                    String.format("Exit Gate " + exitGateNo + ": Vehicle (%s) released", vehicle.getVehicleNo()));
        } else {
            System.out.println("Exit Gate " + exitGateNo + ": Cannot release vehicle. It is not parked.");
        }
    }
}

/**
 * 
 * PaymentService
 */
class PaymentService implements Payment {
    @Override
    public void pay(double amount) {
        System.out.println("PAID: Parking fee: $%s" + amount);
    }
}

/**
 * 
 * ParkingSystem
 */
public class ParkingSystem {
    public static void main(String[] args) throws InterruptedException {
        ParkingLot parkingLot = ParkingLot.getInstance();

        // Levels
        Level levelOne = new Level(3);
        Level levelTwo = new Level(3);

        // Entry gates
        EntryGate entryGateOne = new EntryGate(1, parkingLot);
        EntryGate entryGateTwo = new EntryGate(2, parkingLot);

        // Exit gate
        ExitGate exitGateOne = new ExitGate(1, parkingLot);
        ExitGate exitGateTwo = new ExitGate(2, parkingLot);

        // Parking fee calculation service
        ParkingFeeCalculationService feeCalculationService = new ParkingFeeCalculationService(
                new ParkingFeeCalculation());

        // Spot Assignment service
        SpotAssignmentService spotAssignmentService = new SpotAssignmentService(new FirstFreeSpotAssignment());

        // Payment Service
        PaymentService paymentService = new PaymentService();

        // Parking lot configuration
        parkingLot.setLevels(List.of(levelOne, levelTwo));
        parkingLot.setEntryGates(List.of(entryGateOne, entryGateTwo));
        parkingLot.setExitGates(List.of(exitGateOne, exitGateTwo));
        parkingLot.setParkingFeeCalculationStrategy(feeCalculationService);
        parkingLot.setSpotAssignmentStrategy(spotAssignmentService);
        parkingLot.setPaymentService(paymentService);

        // Create 7 vehicles
        Vehicle car1 = new Vehicle("KA 05 MN 1234", VehicleType.CAR);
        Vehicle bike1 = new Vehicle("DL 23 MN 1235", VehicleType.BIKE);
        Vehicle truck1 = new Vehicle("MH 76 MN 1236", VehicleType.TRUCK);

        Vehicle car2 = new Vehicle("UP 32 AB 4567", VehicleType.CAR);
        Vehicle bike2 = new Vehicle("RJ 14 CD 7890", VehicleType.BIKE);
        Vehicle truck2 = new Vehicle("GJ 01 EF 2345", VehicleType.TRUCK);
        Vehicle car3 = new Vehicle("MP 09 GH 6789", VehicleType.CAR);

        // Entry Simulation
        Thread entryThread1 = new Thread(
                () -> entryGateOne.parkVehicle(car1),
                "Entry-Thread-1");

        Thread entryThread2 = new Thread(
                () -> entryGateTwo.parkVehicle(bike1),
                "Entry-Thread-2");

        Thread entryThread3 = new Thread(
                () -> entryGateOne.parkVehicle(truck1),
                "Entry-Thread-3");

        Thread entryThread4 = new Thread(
                () -> entryGateTwo.parkVehicle(car2),
                "Entry-Thread-4");

        Thread entryThread5 = new Thread(
                () -> entryGateOne.parkVehicle(bike2),
                "Entry-Thread-5");

        Thread entryThread6 = new Thread(
                () -> entryGateTwo.parkVehicle(truck2),
                "Entry-Thread-6");

        Thread entryThread7 = new Thread(
                () -> entryGateOne.parkVehicle(car3),
                "Entry-Thread-7");

        // Start all entry threads
        entryThread1.start();
        entryThread2.start();
        entryThread3.start();
        entryThread4.start();
        entryThread5.start();
        entryThread6.start();
        entryThread7.start();

        // Wait for all vehicles to enter
        entryThread1.join();
        entryThread2.join();
        entryThread3.join();
        entryThread4.join();
        entryThread5.join();
        entryThread6.join();
        entryThread7.join();

        // Exit Simulation
        Thread exitThread1 = new Thread(
                () -> exitGateOne.releaseVehicle(car1),
                "Exit-Thread-1");

        Thread exitThread2 = new Thread(
                () -> exitGateTwo.releaseVehicle(bike1),
                "Exit-Thread-2");

        Thread exitThread3 = new Thread(
                () -> exitGateOne.releaseVehicle(truck1),
                "Exit-Thread-3");

        Thread exitThread4 = new Thread(
                () -> exitGateTwo.releaseVehicle(car2),
                "Exit-Thread-4");

        Thread exitThread5 = new Thread(
                () -> exitGateOne.releaseVehicle(bike2),
                "Exit-Thread-5");

        Thread exitThread6 = new Thread(
                () -> exitGateTwo.releaseVehicle(truck2),
                "Exit-Thread-6");

        Thread exitThread7 = new Thread(
                () -> exitGateOne.releaseVehicle(car3),
                "Exit-Thread-7");

        // Start all exit threads
        exitThread1.start();
        exitThread2.start();
        exitThread3.start();
        exitThread4.start();
        exitThread5.start();
        exitThread6.start();
        exitThread7.start();

        // Wait for all vehicles to exit
        exitThread1.join();
        exitThread2.join();
        exitThread3.join();
        exitThread4.join();
        exitThread5.join();
        exitThread6.join();
        exitThread7.join();

    }
}
