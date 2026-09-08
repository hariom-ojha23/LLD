package solid;

/**
 *
 * SINGLE RESPONSIBILITY PRINCIPLE (SRP)
 *
 * WHAT IS IT?
 *
 * A class should have only one responsibility and therefore
 * only one reason to change.
 *
 * A responsibility means a specific area of functionality
 * that a class is responsible for.
 *
 *
 * WHY DO WE NEED IT?
 *
 * 1. Reduces coupling between unrelated responsibilities.
 *
 * 2. Makes classes easier to understand and maintain.
 *
 * 3. Makes code easier to test because each class has a
 *    focused purpose.
 *
 * 4. Changes in one responsibility are less likely to
 *    affect other parts of the system.
 *
 * 5. Makes the code easier to extend and reuse.
 *
 *
 * HOW TO APPLY IT?
 *
 * 1. Identify the different responsibilities handled by a class.
 *
 * 2. Group related functionality together.
 *
 * 3. Separate unrelated responsibilities into different classes.
 *
 * 4. Make each class responsible for one clear area of functionality.
 *
 * 5. Define classes based on reasons for change rather than
 *    simply dividing methods into separate classes.
 *
 *
 * HOW TO IDENTIFY AN SRP VIOLATION?
 *
 * Ask:
 *
 * "How many different reasons could cause this class to change?"
 *
 * If a class can change because of multiple unrelated concerns,
 * it is likely violating SRP.
 *
 * Common warning signs:
 *
 * 1. A class contains business logic, database logic,
 *    logging, and presentation logic together.
 *
 * 2. The class has methods belonging to unrelated concerns.
 *
 * 3. Different teams or developers frequently need to modify
 *    the same class for completely different reasons.
 *
 * 4. Changing one feature requires changes to an otherwise
 *    unrelated part of the class.
 *
 * 5. The class becomes very large because it keeps accumulating
 *    different responsibilities.
 *
 *
 * IMPORTANT:
 *
 * SRP does NOT mean that a class should have only one method.
 *
 * It means that all the functionality inside a class should
 * belong to one cohesive responsibility.
 *
 *
 * INTERVIEW ONE-LINER:
 *
 * "A class should have one responsibility and therefore
 * one reason to change."
 *
 */
public class SingleResponsibility {

}
