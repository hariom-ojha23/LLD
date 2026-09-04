package datatypes;

public class ArrayDatatype {

    public static void main(String[] args) {

        /**
         * Array:
         *
         * - An array is a fixed-size data structure used to store
         *   multiple values of the same type.
         * - Arrays are objects in Java.
         * - Arrays are NOT part of the Collection Framework.
         * - Array size is fixed after creation.
         * - Indexing starts from 0.
         */


        /**
         * 1. Declaration:
         *
         *    dataType[] arrayName;
         */

        int[] numbers;


        /**
         * 2. Creation:
         *
         *    new dataType[size]
         */

        numbers = new int[5];


        /**
         * 3. Declaration + Initialization:
         */

        int[] values = {10, 20, 30, 40, 50};


        /**
         * 4. Accessing elements:
         *
         *    array[index]
         */

        System.out.println(values[0]);  // 10
        System.out.println(values[2]);  // 30


        /**
         * 5. Updating elements:
         */

        values[0] = 100;

        System.out.println(values[0]);  // 100


        /**
         * 6. Array length:
         *
         *    array.length
         *
         *    length is a property, not a method.
         */

        System.out.println("Array length: " + values.length); // 5


        /**
         * 7. Traversing an array:
         */

        for (int i = 0; i < numbers.length; i++) {
            System.out.print(numbers[i] + " ");
        }

        System.out.println();


        /**
         * 8. Enhanced for-each loop:
         */

        for (int value : values) {
            System.out.print(value + " ");
        }

        System.out.println();


        /**
         * 9. Default values:
         *
         * When an array is created using new, its elements are
         * initialized with default values.
         *
         * int     -> 0
         * long    -> 0L
         * float   -> 0.0f
         * double  -> 0.0d
         * char    -> '\u0000'
         * boolean -> false
         * object  -> null
         */

        int[] arr = new int[3];

        System.out.println(arr[0]); // 0
        System.out.println(arr[1]); // 0
        System.out.println(arr[2]); // 0


        /**
         * 10. Multidimensional Array:
         *
         * An array can contain other arrays.
         *
         * Example: 2D array
         */

        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6}
        };

        System.out.println(matrix[0][1]); // 2
        System.out.println(matrix[1][2]); // 6


        /**
         * Traversing 2D array:
         */

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }


        /**
         * Important:
         *
         * - Arrays have fixed size.
         * - Arrays store elements of the same type.
         * - Index starts from 0.
         * - Last index = array.length - 1.
         * - Accessing an invalid index throws
         *   ArrayIndexOutOfBoundsException.
         *
         * - array.length     -> Array size
         * - collection.size() -> Collection size
         *
         * - Arrays are objects and are created on the Heap.
         * - Arrays are NOT part of the Collection Framework.
         */
    }
}