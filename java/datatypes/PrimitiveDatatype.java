package datatypes;

public class PrimitiveDatatype {
    public static void main(String[] args) {
        /**
         * Integral Data Types:
         * 1. byte: 8-bit signed integer
         * 2. short: 16-bit signed integer
         * 3. int: 32-bit signed integer
         * 4. long: 64-bit signed integer
         * 
         * By default integer literals are of type int. 
         * To specify a long literal, we can use the suffix 'L' or 'l'.
         */


        byte a = 127;
        System.out.println("Byte:");
        System.out.println("-------------");
        System.out.println("Original byte value: " + a);
        System.out.println("Minimum byte value: " + Byte.MIN_VALUE);
        System.out.println("Maximum byte value: " + Byte.MAX_VALUE);
        System.out.println();

        short b = 20;
        System.out.println("Short:");
        System.out.println("-------------");
        System.out.println("Original short value: " + b);
        System.out.println("Minimum short value: " + Short.MIN_VALUE);
        System.out.println("Maximum short value: " + Short.MAX_VALUE);
        System.out.println();

        int c = 100;
        System.out.println("Int:");
        System.out.println("-------------");
        System.out.println("Original int value: " + c);
        System.out.println("Minimum int value: " + Integer.MIN_VALUE);
        System.out.println("Maximum int value: " + Integer.MAX_VALUE);
        System.out.println();

        long d = 1000L;
        System.out.println("Long:");
        System.out.println("-------------");
        System.out.println("Original long value: " + d);
        System.out.println("Minimum long value: " + Long.MIN_VALUE);
        System.out.println("Maximum long value: " + Long.MAX_VALUE);
        System.out.println();


        /**
         * Decimal Data Types:
         * 1. float: 32-bit floating-point number
         *    - By default, floating-point literals are of type double.
         *    - Float stores 7 digits of precision
         *    - To specify a float literal, we can use the suffix 'F' or 'f'.
         * 
         * 2. double: 64-bit floating-point number
         *    - Double stores 15 digits of precision
         *    - To specify a double literal, we can use the suffix 'D' or 'd', but it's optional.
         */

        float e = 10.23476578f;
        System.out.println("Float:");
        System.out.println("-------------");
        System.out.println("Original float value: " + e);
        System.out.println("Smallest positive float value: " + Float.MIN_VALUE);
        System.out.println("Largest positive float value: " + Float.MAX_VALUE);
        System.out.println("Smallest negative float value: " + (-Float.MAX_VALUE));
        System.out.println("Largest negative float value: " + (-Float.MIN_VALUE));
        System.out.println();

        double f = 10.234567890123456d;
        System.out.println("Double:");
        System.out.println("-------------");
        System.out.println("Original double value: " + f);
        System.out.println("Smallest positive double value: " + Double.MIN_VALUE);
        System.out.println("Largest positive double value: " + Double.MAX_VALUE);
        System.out.println("Smallest negative double value: " + (-Double.MAX_VALUE));
        System.out.println("Largest negative double value: " + (-Double.MIN_VALUE));
        System.out.println();

        /**
         * Boolean Data Type:
         * 1. boolean: Represents one of two values: true or false.
         *    - boolean are 1 bit in size, but the size is not precisely defined in Java.
         */

        boolean g = true;
        System.out.println("Boolean:");
        System.out.println("-------------");
        System.out.println("Original boolean value: " + g);
        System.out.println("Inverted boolean value: " + !g);

        /**
         * Character Data Type:
         * 1. char: Represents a single 16-bit Unicode character.
         *    - Common ASCII characters are represented by values from 0 to 127.
         * 
         */
        char h = 'A';
        System.out.println("Character:");
        System.out.println("-------------");
        System.out.println("Original character value: " + h);
        System.out.println("Character code: " + (int) h);
        System.out.println("Maximum character value: " + (int) Character.MAX_VALUE);
        System.out.println("Minimum character value: " + (int) Character.MIN_VALUE);
        System.out.println();

        /**
         * Widening Conversion:
         * Widening conversion occurs when a smaller data type is converted to a larger data type
         */

        int i = 100;
        long j = i;
        System.out.println("Widening Conversion:");
        System.out.println("-------------");
        System.out.println("int value: " + i);
        System.out.println("long value: " + j);
        System.out.println();

        /**
         * Narrowing Conversion:
         * Narrowing conversion occurs when a larger data type is converted to a smaller data type
         * This can lead to loss of information and requires explicit casting.
         */

        double k = 1000.123344322;
        float m = (float) k;
        long l = (long) m;
        int n = (int) l;
        System.out.println("Narrowing Conversion:");
        System.out.println("-------------");
        System.out.println("double value: " + k);
        System.out.println("float value: " + m);
        System.out.println("long value: " + l);
        System.out.println("int value: " + n);
        System.out.println();
    }
}
