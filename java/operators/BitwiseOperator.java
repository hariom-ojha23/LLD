package operators;

public class BitwiseOperator {

    public static void main(String[] args) {

        /**
         * Bitwise Operators:
         *
         * Used to perform operations directly on the bits of integers.
         *
         * &   Bitwise AND
         * |   Bitwise OR
         * ^   Bitwise XOR
         * ~   Bitwise NOT
         *
         * <<  Left Shift
         * >>  Signed Right Shift
         * >>> Unsigned Right Shift
         */


        int a = 5;  // 0101
        int b = 3;  // 0011

        System.out.println("Bitwise Operators:");
        System.out.println("------------------");

        // AND: 1 only if both bits are 1
        System.out.println("AND (&): " + (a & b));       // 1

        // OR: 1 if at least one bit is 1
        System.out.println("OR (|): " + (a | b));        // 7

        // XOR: 1 if bits are different
        System.out.println("XOR (^): " + (a ^ b));       // 6

        // NOT: Flips every bit
        System.out.println("NOT (~): " + (~a));           // -6


        /**
         * Shift Operators:
         *
         * 1. Left Shift (<<)
         *    - Shifts bits to the left.
         *    - Empty positions on the right are filled with 0.
         *
         *        5 << 1
         *        0101 << 1 -> 1010 -> 10
         *
         *    - Generally equivalent to multiplying by 2^n.
         */

        System.out.println("Left Shift (5 << 1): " + (5 << 1));   // 10


        /**
         * 2. Signed Right Shift (>>)
         *    - Shifts bits to the right.
         *    - Preserves the sign bit.
         *
         *        10 >> 1
         *        1010 >> 1 -> 0101 -> 5
         *
         *    - Generally equivalent to dividing by 2^n for positive numbers.
         */

        System.out.println("Right Shift (10 >> 1): " + (10 >> 1)); // 5


        /**
         * 3. Unsigned Right Shift (>>>)
         *    - Shifts bits to the right.
         *    - Fills the leftmost positions with 0.
         *    - Unlike >>, it does not preserve the sign.
         */

        System.out.println(
            "Unsigned Right Shift (10 >>> 1): " + (10 >>> 1)
        ); // 5


        /**
         * Important:
         *
         * AND (&)
         *    1 & 1 -> 1
         *    Otherwise -> 0
         *
         * OR (|)
         *    0 | 0 -> 0
         *    Otherwise -> 1
         *
         * XOR (^)
         *    Same bits -> 0
         *    Different bits -> 1
         *
         * NOT (~)
         *    0 -> 1
         *    1 -> 0
         *
         *
         * Shift:
         *
         *    x << n  -> approximately x * 2^n
         *    x >> n  -> approximately x / 2^n
         *
         * Note:
         *    These multiplication/division shortcuts have limitations
         *    due to integer overflow and signed integer behavior.
         */
    }
}