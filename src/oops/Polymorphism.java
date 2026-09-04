package oops;

/**
 *
 * Polymorphism
 *
 * Polymorphism means "many forms".
 *
 * It allows the same method name or interface to represent
 * different behaviors depending on the situation.
 *
 * Java supports two types of polymorphism:
 *
 * 1. Compile-time Polymorphism
 * 2. Runtime Polymorphism
 *
 *
 * 1. COMPILE-TIME POLYMORPHISM
 *
 * Compile-time polymorphism is achieved using METHOD OVERLOADING.
 *
 * Method Overloading:
 * - Multiple methods have the same name
 * - But different parameter lists
 * - The compiler decides which method to call at compile time
 *
 * The parameter list can differ by:
 * - Number of parameters
 * - Type of parameters
 * - Order of parameters
 *
 * Return type alone cannot be used for method overloading.
 *
 *
 * Example:
 *
 * calculate(int, int)
 * calculate(double, double)
 *
 * Both methods have the same name but different parameter types.
 *
 *
 * 2. RUNTIME POLYMORPHISM
 *
 * Runtime polymorphism is achieved using METHOD OVERRIDING.
 *
 * Method Overriding:
 * - A subclass provides its own implementation of a method
 * already defined in the superclass.
 * 
 * - The method that gets executed is determined at runtime.
 *
 * Runtime polymorphism usually involves:
 *
 * Parent reference → Child object
 *
 * Example:
 *
 * Animal animal = new Dog();
 *
 * If Dog overrides the sound() method, then:
 *
 * animal.sound();
 *
 * calls Dog's sound() method at runtime.
 *
 */

class Calculator {

    // Add two integers
    public int add(int x, int y) {
        return x + y;
    }

    // Add two doubles
    public double add(double x, double y) {
        return x + y;
    }

    // Add three integers
    public int add(int x, int y, int z) {
        return x + y + z;
    }
}

class Car {
    // Method to start the car
    public void start() {
        System.out.println("Car starts");
    }
}

class SportsCar extends Car {
    // Override the start method for SportsCar
    @Override
    public void start() {
        System.out.println("Sports car starts with a roar");
    }
}

class ElectricCar extends Car {
    // Override the start method for ElectricCar
    @Override
    public void start() {
        System.out.println("Electric car starts silently");
    }
}

public class Polymorphism {
    public static void main(String[] args) {
        System.out.println("Compile-time Polymorphism (Method Overloading):");

        Calculator calc = new Calculator();
        System.out.println(calc.add(5, 10));
        System.out.println(calc.add(5.5, 10.7));
        System.out.println(calc.add(5, 10, 15));

        System.out.println("================================");

        System.out.println("Runtime Polymorphism (Method Overriding):");

        // Upcasting: Parent reference to Child object
        Car sportsCar = new SportsCar();
        sportsCar.start();

        // Upcasting: Parent reference to Child object
        Car electricCar = new ElectricCar();
        electricCar.start();
    }
}
