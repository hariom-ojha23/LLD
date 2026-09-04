package controlflow;

public class IfElse {

    public static void main(String[] args) {

        /**
         * If-Else:
         *
         * Used to execute different blocks of code based on conditions.
         *
         * 1. if
         * 2. if-else
         * 3. if-else-if
         * 4. Nested if
         * 5. Ternary operator
         */


        /**
         * 1. if:
         *
         * Executes the block only when the condition is true.
         */

        int age = 20;

        if (age >= 18) {
            System.out.println("Adult");
        }


        /**
         * 2. if-else:
         *
         * if condition is true  -> execute if block
         * if condition is false -> execute else block
         */

        if (age >= 18) {
            System.out.println("Eligible to vote");
        } else {
            System.out.println("Not eligible to vote");
        }


        /**
         * 3. if-else-if:
         *
         * Used when there are multiple conditions.
         *
         * Conditions are checked from top to bottom.
         * The first true condition is executed.
         */

        int marks = 75;

        if (marks >= 90) {
            System.out.println("Grade A");
        } else if (marks >= 80) {
            System.out.println("Grade B");
        } else if (marks >= 70) {
            System.out.println("Grade C");
        } else {
            System.out.println("Grade D");
        }


        /**
         * 4. Nested if:
         *
         * An if statement inside another if statement.
         */

        boolean hasAccount = true;
        boolean isVerified = true;

        if (hasAccount) {
            if (isVerified) {
                System.out.println("Access granted");
            }
        }


        /**
         * 5. Ternary Operator:
         *
         * Short form of simple if-else.
         *
         * condition ? valueIfTrue : valueIfFalse
         */

        int number = 10;

        String result = (number % 2 == 0) ? "Even" : "Odd";

        System.out.println(result); // Even


        /**
         * Important:
         *
         * - Conditions must evaluate to boolean (true/false).
         * - Use &&, || and ! to combine conditions.
         * - Use == to compare primitive values.
         * - Use .equals() to compare String contents.
         *
         * Example:
         *
         *     if (age >= 18 && hasAccount) {
         *         ...
         *     }
         */
    }
}