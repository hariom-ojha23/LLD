package controlflow;

public class SwitchCase {

    public static void main(String[] args) {

        /**
         * Switch-Case:
         *
         * Used to execute one block of code based on the value
         * of an expression.
         *
         * Syntax:
         *
         * switch (expression) {
         *     case value1:
         *         // code
         *         break;
         *     case value2:
         *         // code
         *         break;
         *     default:
         *         // code
         * }
         */


        /**
         * 1. Basic switch:
         *
         * The expression is compared with each case.
         */

        int day = 2;

        switch (day) {
            case 1:
                System.out.println("Monday");
                break;

            case 2:
                System.out.println("Tuesday");
                break;

            case 3:
                System.out.println("Wednesday");
                break;

            default:
                System.out.println("Invalid day");
        }


        /**
         * 2. Multiple cases:
         *
         * Multiple cases can execute the same block of code.
         */

        int month = 2;

        switch (month) {
            case 12:
            case 1:
            case 2:
                System.out.println("Winter");
                break;

            case 3:
            case 4:
            case 5:
                System.out.println("Spring");
                break;

            default:
                System.out.println("Other season");
        }


        /**
         * 3. break:
         *
         * break exits the switch after a matching case is executed.
         *
         * Without break, execution continues into the next cases.
         */

        int number = 1;

        switch (number) {
            case 1:
                System.out.println("One");
                // No break

            case 2:
                System.out.println("Two");
                break;
        }

        // Output:
        // One
        // Two


        /**
         * 4. Switch with String:
         *
         * String can be used as a switch expression.
         */

        String role = "ADMIN";

        switch (role) {
            case "ADMIN":
                System.out.println("Full access");
                break;

            case "USER":
                System.out.println("Limited access");
                break;

            default:
                System.out.println("Unknown role");
        }


        /**
         * 5. Switch Expression:
         *
         * Modern Java supports switch expressions using ->.
         *
         * No break is required with the arrow syntax.
         */

        int score = 2;

        String result = switch (score) {
            case 1 -> "Low";
            case 2 -> "Medium";
            case 3 -> "High";
            default -> "Invalid";
        };

        System.out.println(result); // Medium


        /**
         * Important:
         *
         * Traditional switch:
         *    - Uses case:
         *    - Usually requires break.
         *    - Can have fall-through.
         *
         * Modern switch:
         *    - Uses case value ->.
         *    - No break required.
         *    - Can return a value.
         *
         *
         * switch can commonly be used with:
         *    - byte
         *    - short
         *    - char
         *    - int
         *    - String
         *    - enum
         *    - corresponding wrapper types
         *
         * switch does NOT work directly with:
         *    - boolean
         *    - long
         *    - float
         *    - double
         */
    }
}