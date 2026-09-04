package oops;

/**
 *
 * Inheritance
 *
 * 1. Inheritance allows one class to acquire properties (fields)
 * and behaviors (methods) of another class.
 *
 * 2. The class that inherits is called the Subclass
 * (or Derived class / Child class).
 *
 * The class from which it inherits is called the Superclass
 * (or Base class / Parent class).
 *
 * 3. Java uses the "extends" keyword to implement inheritance.
 *
 * 4. The access modifier of an inherited member does not change
 * because of inheritance.
 *
 * - public members remain public
 * - protected members remain protected
 * - private members are not accessible directly in the subclass
 * 
 * 5. Java supports:
 *
 *    Single inheritance:
 *        A → B
 *
 *    Multilevel inheritance:
 *        A → B → C
 *
 *    Hierarchical inheritance:
 *        A → B
 *        A → C
 *
 *    ❌ Java does NOT support multiple inheritance through classes :
 *
 *        A   B
 *         \ /
 *          C    
 *
 *    ✅ However, Java supports multiple inheritance through interfaces.
 * 
 *        X     Y
 *         \   /
 *          \ /
 *           C    
 *
 *
 * Inheritance Example:
 *
 *           Animal
 *             |
 *             ↓
 *            Dog
 *
 * Dog inherits from Animal.
 *
 */

class Animal {
    protected String name;

    public void eat() {
        System.out.println(name + " is eating.");
    }

    public void sleep() {
        System.out.println(name + " is sleeping.");
    }
}

class Dog extends Animal {
    public Dog(String name) {
        this.name = name;
    }

    public void bark() {
        System.out.println(name + " is barking.");
    }
}

class Cat extends Animal {
    public Cat(String name) {
        this.name = name;
    }

    public void meow() {
        System.out.println(name + " is meowing.");
    }
}

public class Inheritance {
    public static void main(String[] args) {
        Dog dog = new Dog("Tommy");

        // Inherited method
        dog.eat();

        // Inherited method
        dog.sleep();

        // Dog's own method
        dog.bark();

        System.out.println("====================");

        Cat cat = new Cat("Kitty");

        // Inherited method
        cat.eat();

        // Inherited method
        cat.sleep();

        // Cat's own method
        cat.meow();
    }
}
