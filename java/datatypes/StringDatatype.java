package datatypes;

public class StringDatatype {
    public static void main(String[] args) {
        /**
         * String Data Type:
         *
         * 1. String:
         *    - String is a class in Java (java.lang.String).
         *    - It represents a sequence of characters.
         *    - String objects are immutable, meaning their value cannot be
         *      changed after the object is created.
         *
         *
         * 2. String Pool:
         *    - String literals are stored in a special area of the Heap
         *      called the String Pool (String Constant Pool).
         *    - The String Pool allows Java to reuse String objects with
         *      the same value, which saves memory.
         *
         *    Example:
         *
         *        String s1 = "Hello";
         *        String s2 = "Hello";
         *
         *    Both s1 and s2 refer to the same "Hello" object in the
         *    String Pool.
         *
         *        System.out.println(s1 == s2);       // true
         *        System.out.println(s1.equals(s2));  // true
         *
         *
         * 3. String Literal:
         *    - A String created using double quotes is called a String literal.
         *
         *        String s1 = "Hello";
         *
         *    - The literal "Hello" is stored in the String Pool.
         *    - If "Hello" already exists in the pool, Java reuses the
         *      existing object.
         *
         *
         * 4. String using new:
         *    - A String can also be explicitly created using the new keyword.
         *
         *        String s2 = new String("Hello");
         *
         *    - "Hello" is a String literal and is stored/reused in the
         *      String Pool.
         *    - new String("Hello") creates a separate String object
         *      on the Heap.
         *
         *        String s1 = "Hello";
         *        String s2 = new String("Hello");
         *
         *        System.out.println(s1 == s2);       // false
         *        System.out.println(s1.equals(s2));  // true
         *
         *    - == compares object references.
         *    - equals() compares the contents of the Strings.
         *
         *
         * 5. String Immutability:
         *    - Strings are immutable.
         *    - Any operation that appears to modify a String actually
         *      creates a new String object.
         *
         *        String s = "Hello";
         *        s = s + " World";
         *
         *    - The original "Hello" String is not modified.
         *    - A new String "Hello World" is created.
         *    - The reference variable s now points to the new String.
         *
         *
         * 6. StringBuilder:
         *    - StringBuilder is a separate class used to create and modify
         *      a mutable sequence of characters.
         *    - StringBuilder objects are stored on the Heap.
         *    - StringBuilder does not use the String Pool for its mutable
         *      character data.
         *    - It is useful when performing frequent String modifications.
         *
         *        StringBuilder sb = new StringBuilder("Hello");
         *        sb.append(" World");
         *
         *        System.out.println(sb); // Hello World
         *
         *    - Unlike String, StringBuilder is mutable.
         *
         *
         * 7. StringBuffer:
         *    - StringBuffer is similar to StringBuilder but its methods
         *      are synchronized.
         *    - It is mutable and thread-safe.
         *    - StringBuffer objects are stored on the Heap.
         *
         *
         * 8. String vs StringBuilder vs StringBuffer:
         *
         *       String:
         *       - Immutable
         *       - String literals use String Pool
         *       - Good when String value does not change frequently
         *
         *       StringBuilder:
         *       - Mutable
         *       - Not thread-safe
         *       - Preferred for frequent String modifications
         *
         *       StringBuffer:
         *       - Mutable
         *       - Thread-safe
         *       - Methods are synchronized
         *
         *
         * 9. Important Memory Concept:
         *
         *       String s1 = "Hello";
         *
         *       Stack                    Heap
         *       -----                    ----------------
         *       s1 -------------------> String Pool
         *                                "Hello"
         *
         *
         *       String s2 = new String("Hello");
         *
         *       Stack                    Heap
         *       -----                    ----------------
         *       s2 -------------------> Separate String object
         *                                "Hello"
         *
         *       The literal "Hello" may also exist in the String Pool.
         *
         *
         * 10. Important:
         *     - String is a class, not a primitive data type.
         *     - String objects are stored on the Heap.
         *     - String literals are stored in the String Pool,
         *       which is part of the Heap.
         *     - String is immutable.
         *     - StringBuilder and StringBuffer are mutable.
         */


        // String literal
        String s1 = "Hello";

        // Same literal -> reuses the String Pool object
        String s2 = "Hello";

        System.out.println("String:");
        System.out.println("-------------");
        System.out.println("String 1: " + s1);
        System.out.println("String 2: " + s2);

        // == compares references
        System.out.println("s1 == s2: " + (s1 == s2));             // true

        // equals() compares contents
        System.out.println("s1.equals(s2): " + s1.equals(s2));    // true
        System.out.println();


        // String using new
        String s3 = new String("Hello");

        System.out.println("String using new:");
        System.out.println("-------------");

        // s3 refers to a separate String object on the Heap
        System.out.println("s1 == s3: " + (s1 == s3));             // false

        // Contents are the same
        System.out.println("s1.equals(s3): " + s1.equals(s3));    // true
        System.out.println();


        // String is immutable
        String s4 = "Hello";

        s4 = s4 + " World";

        System.out.println("String Immutability:");
        System.out.println("-------------");
        System.out.println("String after modification: " + s4);
        System.out.println();


        // StringBuilder is mutable
        StringBuilder sb = new StringBuilder("Hello");

        sb.append(" World");

        System.out.println("StringBuilder:");
        System.out.println("-------------");
        System.out.println("StringBuilder value: " + sb);
        System.out.println();


        // StringBuffer is mutable and thread-safe
        StringBuffer buffer = new StringBuffer("Hello");

        buffer.append(" World");

        System.out.println("StringBuffer:");
        System.out.println("-------------");
        System.out.println("StringBuffer value: " + buffer);
        System.out.println();


        /**
         * String Functions / Methods:
         *
         * 1. length()
         *    - Returns number of characters.
         *    - "Hello".length() -> 5
         *
         * 2. charAt(index)
         *    - Returns character at given index (0-based).
         *    - "Hello".charAt(1) -> 'e'
         *
         * 3. substring(beginIndex)
         *    - Returns substring from beginIndex to end.
         *    - "Hello".substring(2) -> "llo"
         *
         * 4. substring(beginIndex, endIndex)
         *    - beginIndex inclusive, endIndex exclusive.
         *    - "Hello".substring(1, 4) -> "ell"
         *
         * 5. contains(sequence)
         *    - Checks whether sequence exists.
         *    - "Hello".contains("ell") -> true
         *
         * 6. replace(old, new)
         *    - Replaces all matching characters/sequences.
         *    - "Hello".replace("l", "x") -> "Hexxo"
         *
         * 7. toUpperCase()
         *    - Converts to uppercase.
         *    - "Hello".toUpperCase() -> "HELLO"
         *
         * 8. toLowerCase()
         *    - Converts to lowercase.
         *    - "Hello".toLowerCase() -> "hello"
         *
         * 9. equals(other)
         *    - Compares String contents.
         *
         * 10. equalsIgnoreCase(other)
         *     - Compares contents ignoring case.
         *
         * 11. startsWith(prefix)
         *     - Checks whether String starts with prefix.
         *
         * 12. endsWith(suffix)
         *     - Checks whether String ends with suffix.
         *
         * 13. indexOf(value)
         *     - Returns first occurrence index; -1 if not found.
         *
         * 14. lastIndexOf(value)
         *     - Returns last occurrence index; -1 if not found.
         *
         * 15. isEmpty()
         *     - Returns true if length is 0.
         *
         * 16. isBlank()
         *     - Returns true if empty or contains only whitespace.
         *     - Available since Java 11.
         *
         * 17. trim()
         *     - Removes leading and trailing whitespace.
         *
         * 18. concat(str)
         *     - Concatenates another String.
         *
         * Important:
         *    - String is immutable, so these methods do not modify
         *      the original String. Methods that modify/transform
         *      String content generally return a new String.
         */

        System.out.println("String Functions / Methods:");
        System.out.println("-------------");
        String str = "Hello World";

        System.out.println("Original String: " + str);

        System.out.println("Length: " + str.length());
        System.out.println("Character at index 1: " + str.charAt(1));

        System.out.println("Substring: " + str.substring(6));
        System.out.println("Substring: " + str.substring(0, 5));

        System.out.println("Contains World: " + str.contains("World"));

        System.out.println("Replace: " + str.replace("World", "Java"));

        System.out.println("Uppercase: " + str.toUpperCase());
        System.out.println("Lowercase: " + str.toLowerCase());

        System.out.println("Starts with Hello: " + str.startsWith("Hello"));
        System.out.println("Ends with World: " + str.endsWith("World"));

        System.out.println("Index of o: " + str.indexOf('o'));
        System.out.println("Last index of o: " + str.lastIndexOf('o'));

        System.out.println("Is empty: " + str.isEmpty());
        System.out.println("Is blank: " + str.isBlank());

        System.out.println("Trimmed: " + "  Hello  ".trim());
    }
}
