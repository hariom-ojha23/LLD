package operators;

public class LogicalOperator {

    public static void main(String[] args) {

        /**
         * Logical Operators:
         *
         * Used to combine or negate boolean expressions.
         *
         * &&  Logical AND
         * ||  Logical OR
         * !   Logical NOT
         */


        boolean a = true;
        boolean b = false;

        System.out.println("Logical Operators:");
        System.out.println("-----------------");

        // AND: true only when both conditions are true
        System.out.println("AND (&&): " + (a && b));     // false

        // OR: true when at least one condition is true
        System.out.println("OR (||): " + (a || b));      // true

        // NOT: reverses the boolean value
        System.out.println("NOT (!): " + (!a));          // false


        /**
         * Truth Table:
         *
         *     A       B       A && B      A || B
         *    -----------------------------------
         *   false   false      false       false
         *   false   true       false       true
         *   true    false      false       true
         *   true    true       true        true
         *
         *    !true  -> false
         *    !false -> true
         */


        /**
         * Short-Circuit Evaluation:
         *
         * &&:
         * - If the first condition is false, Java does not evaluate
         *   the second condition because the result will always be false.
         *
         * ||:
         * - If the first condition is true, Java does not evaluate
         *   the second condition because the result will always be true.
         */

        int x = 10;

        // Second condition is NOT evaluated because x > 20 is false.
        if (x > 20 && ++x > 10) {
            System.out.println("Both conditions are true");
        }

        System.out.println("x: " + x); // 10


        // Second condition is NOT evaluated because x > 5 is true.
        if (x > 5 || ++x > 10) {
            System.out.println("At least one condition is true");
        }

        System.out.println("x: " + x); // 10


        /**
         * Important:
         *
         * && -> Logical AND + short-circuit
         * || -> Logical OR  + short-circuit
         * !  -> Logical NOT
         *
         * Logical operators work with boolean values.
         */
    }
}