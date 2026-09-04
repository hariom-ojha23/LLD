package operators;

public class ArithmeticOperator {

    public static void main(String[] args) {

        /**
         * Arithmetic Operators:
         *
         * Used to perform mathematical operations.
         *
         * +  Addition
         * -  Subtraction
         * *  Multiplication
         * /  Division
         * %  Modulus (remainder)
         */

        int a = 10;
        int b = 3;

        System.out.println("Arithmetic Operators:");
        System.out.println("--------------------");

        System.out.println("Addition: " + (a + b));          // 13
        System.out.println("Subtraction: " + (a - b));       // 7
        System.out.println("Multiplication: " + (a * b));    // 30
        System.out.println("Division: " + (a / b));          // 3
        System.out.println("Modulus: " + (a % b));           // 1


        /**
         * Compound Assignment Operators:
         *
         * Shortcuts for performing an operation and assigning
         * the result back to the same variable.
         *
         * +=  a += b  -> a = a + b
         * -=  a -= b  -> a = a - b
         * *=  a *= b  -> a = a * b
         * /=  a /= b  -> a = a / b
         * %=  a %= b  -> a = a % b
         */

        int x = 10;
        System.out.println("\nCompound Assignment Operators:");
        System.out.println("--------------------------------");
        System.out.println("Initial x: " + x);

        x += 5;
        System.out.println("After += : " + x);    // 15

        x -= 3;
        System.out.println("After -= : " + x);    // 12

        x *= 2;
        System.out.println("After *= : " + x);    // 24

        x /= 4;
        System.out.println("After /= : " + x);    // 6

        x %= 4;
        System.out.println("After %= : " + x);    // 2


        /**
         * Important:
         *
         * 1. Integer division:
         *    10 / 3 -> 3
         *
         * 2. Floating-point division:
         *    10.0 / 3 -> 3.3333333333333335
         *
         * 3. Integer division by zero throws ArithmeticException.
         *
         * 4. % returns the remainder.
         */
    }
}