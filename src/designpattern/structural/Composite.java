package designpattern.structural;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Composite
 * 
 * Composite organises objects into tree structures.
 * 
 * Both simple (leaf) objects and complex (container) objects implement the same
 * interface.
 * 
 * This lets you write code that works on a single item
 * and a collection of items without knowing which it is.
 * 
 * 
 * Benefits:
 * - Makes it easy to represent tree/hierarchical structures.
 * - Reduces complexity - client code works uniformly with objects and
 * compositions.
 * - Enforces consistency - all components follow the same interface.
 * - Supports recursive structures naturally.
 * - Adding new Leaf/Composite types is relatively easy.
 * 
 * 
 * Drawbacks:
 * - Can make the design more complex because of the extra abstraction.
 * - Common interface may contain operations that are not relevant to all
 * classes.
 * 
 * When to use:
 * - Objects naturally form a tree structure.
 * - We want to treat individual objects and groups uniformly.
 * - Operations need to be applied recursively to a hierarchy.
 * 
 */

/**
 *  
 */
interface Employee {
    void showDetails();

    String getName();
}

/**
 * 
 * Leaf:
 * 
 * Developer IS-A Employee
 * Developer does not contain other Employee objects.
 */
class Developer implements Employee {
    private String name;

    public Developer(String name) {
        this.name = name;
    }

    @Override
    public void showDetails() {
        System.out.println("Developer: " + name);
    }

    @Override
    public String getName() {
        return name;
    }
}

/**
 * 
 * Manager:
 * 
 * Manager IS-A Employee
 * Manager HAS-A collection of Employee
 * 
 * Manager can contain both:
 * - Leaf objects (Developer)
 * - Other Composite objects (Manager)
 */
class Manager implements Employee {
    private String name;
    private List<Employee> employees = new ArrayList<>();

    public Manager(String name) {
        this.name = name;
    }

    @Override
    public void showDetails() {
        System.out.println("Manager: " + name);

        for (Employee employee : employees) {
            employee.showDetails();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        System.out.println("Added new employee: " + employee.getName());
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        System.out.println("Removed employee: " + employee.getName());
    }
}

public class Composite {
    public static void main(String[] args) {
        Manager engineeringManager = new Manager("John");
        Manager backendManager = new Manager("Emily");

        Employee developer1 = new Developer("Alice");
        Employee developer2 = new Developer("Bob");

        Employee developer3 = new Developer("Harry");
        Employee developer4 = new Developer("Peter");

        engineeringManager.addEmployee(developer1);
        engineeringManager.addEmployee(developer2);

        backendManager.addEmployee(developer3);
        backendManager.addEmployee(developer4);

        engineeringManager.addEmployee(backendManager);

        System.out.println("===============================");

        engineeringManager.removeEmployee(developer2);

        System.out.println("===============================");

        engineeringManager.showDetails();
    }
}
