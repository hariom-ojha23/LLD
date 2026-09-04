package controlflow;

public class Loops {

    public static void main(String[] args) {

        /**
         * Loops:
         *
         * Used to execute a block of code repeatedly.
         *
         * 1. for loop
         * 2. while loop
         * 3. do-while loop
         * 4. enhanced for-each loop
         */


        /**
         * 1. for loop:
         *
         * Used when the number of iterations is known.
         *
         * for (initialization; condition; update) {
         *     // code
         * }
         */

        System.out.println("For loop example:");

        for (int i = 1; i <= 5; i++) {
            System.out.print(i + " ");
        }

        System.out.println(); // Output: 1 2 3 4 5


        /**
         * 2. while loop:
         *
         * Used when the number of iterations is not necessarily known.
         * Condition is checked before every iteration.
         *
         * If the condition is false initially, the loop executes 0 times.
         */

        System.out.println("While loop example:");

        int i = 1;

        while (i <= 5) {
            System.out.print(i + " ");
            i++;
        }

        System.out.println(); // Output: 1 2 3 4 5


        /**
         * 3. do-while loop:
         *
         * Similar to while, but the condition is checked after
         * executing the loop body.
         *
         * Therefore, the loop executes at least once.
         */

        System.out.println("Do-while loop example:");

        int j = 1;

        do {
            System.out.print(j + " ");
            j++;
        } while (j <= 5);

        System.out.println(); // Output: 1 2 3 4 5


        /**
         * 4. Enhanced for-each loop:
         *
         * Used to iterate over arrays and collections.
         *
         * Syntax:
         *
         * for (dataType variable : collection) {
         *     // code
         * }
         */

        System.out.println("Enhanced for-each loop example:");

        int[] numbers = {10, 20, 30, 40};

        for (int number : numbers) {
            System.out.print(number + " ");
        }

        System.out.println(); // Output: 10 20 30 40


        /**
         * break:
         *
         * Immediately terminates the loop.
         */

        System.out.println("Break example:");

        for (int k = 1; k <= 10; k++) {

            if (k == 5) {
                break;
            }

            System.out.print(k + " ");
        }

        System.out.println(); // Output: 1 2 3 4


        /**
         * continue:
         *
         * Skips the current iteration and moves to the next iteration.
         */

        System.out.println("Continue example:");

        for (int k = 1; k <= 5; k++) {

            if (k == 3) {
                continue;
            }

            System.out.print(k + " ");
        }

        System.out.println(); // Output: 1 2 4 5


        /**
         * Important:
         *
         * for       -> Best when iterations are known.
         *
         * while     -> Condition checked before execution.
         *
         * do-while  -> Executes at least once.
         *
         * for-each  -> Simple iteration over arrays/collections.
         *
         * break     -> Exit loop completely.
         *
         * continue  -> Skip current iteration.
         */
    }
}