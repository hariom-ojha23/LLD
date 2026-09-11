package designpattern.creational;

/**
 *
 * AbstractFactory
 *
 *
 * Abstract Factory provides an interface for creating families of related
 * objects without specifying their concrete classes.
 *
 *
 * The main purpose of Abstract Factory is to create a family of related
 * objects that are designed to work together.
 *
 *
 * Example:
 *
 * MySQL Family:
 * - MysqlConnection
 * - MysqlCommand
 * - MysqlTransaction
 *
 *
 * PostgreSQL Family:
 * - PostgreSQLConnection
 * - PostgreSQLCommand
 * - PostgreSQLTransaction
 *
 *
 * The client works with the Abstract Factory and Product interfaces,
 * so it does not need to know about the concrete classes.
 *
 *
 * Key idea:
 * Factory Method -> Creates one product
 * Abstract Factory -> Creates a family of related products
 *
 *
 * Terminology:
 *
 * 1. Abstract Products
 * - Interfaces representing different types of products
 *
 * 2. Concrete Products
 * - Concrete implementations of the Abstract Products
 *
 * 3. Abstract Factory
 * - Interface that defines methods for creating different products
 *
 * 4. Concrete Factory
 * - Implements the Abstract Factory
 * - Creates a family of related concrete products
 *
 * 5. Client
 * - Uses the Abstract Factory and Abstract Products
 * - Does not directly create concrete products
 *
 *
 * Benefits:
 * - Creates families of related objects
 * - Ensures that related products are used together
 * - Hides concrete product creation from the client
 * - Reduces coupling between client and concrete classes
 * - Makes it easier to switch between product families
 *
 *
 * Drawbacks:
 * - Adding a new product type requires changes to every concrete factory
 * - Can introduce many interfaces and classes
 * - More complex than Simple Factory or Factory Method
 *
 *
 * Example:
 *
 * DatabaseFactory factory = new MysqlFactory();
 *
 * DatabaseConnection connection = factory.createConnection();
 * DatabaseCommand command = factory.createCommand();
 * DatabaseTransaction transaction = factory.createTransaction();
 *
 * All three objects belong to the MySQL family.
 *
 *
 */

/**
 *
 * DatabaseConnection: Abstract Product 1
 *
 * Defines the common behavior for database connections.
 *
 */
interface DatabaseConnection {

    public void connect();

    public void disconnect();
}

/**
 *
 * DatabaseCommand: Abstract Product 2
 *
 * Defines the common behavior for database commands.
 *
 */
interface DatabaseCommand {

    public void execute(String query);
}

/**
 *
 * DatabaseTransaction: Abstract Product 3
 *
 * Defines the common behavior for database transactions.
 *
 */
interface DatabaseTransaction {

    public void begin();

    public void commit();

    public void rollback();
}

/**
 *
 * MysqlConnection: Concrete Product 1
 *
 * Concrete implementation of DatabaseConnection.
 *
 * Belongs to the MySQL product family.
 *
 */
class MysqlConnection implements DatabaseConnection {

    @Override
    public void connect() {
        System.out.println("Connecting to mysql");
    }

    @Override
    public void disconnect() {
        System.out.println("Disconnecting from mysql");
    }
}

/**
 *
 * MysqlCommand: Concrete Product 2
 *
 * Concrete implementation of DatabaseCommand.
 *
 * Belongs to the MySQL product family.
 *
 */
class MysqlCommand implements DatabaseCommand {

    @Override
    public void execute(String query) {
        System.out.println("Executing: " + query);
    }
}

/**
 *
 * MysqlTransaction: Concrete Product 3
 *
 * Concrete implementation of DatabaseTransaction.
 *
 * Belongs to the MySQL product family.
 *
 */
class MysqlTransaction implements DatabaseTransaction {

    @Override
    public void begin() {
        System.out.println("Starting transaction");
    }

    @Override
    public void commit() {
        System.out.println("Commit the transaction");
    }

    @Override
    public void rollback() {
        System.out.println("Rollback the transaction");
    }
}

/**
 *
 * DatabaseFactory: Abstract Factory
 *
 * Defines methods for creating different types of related products.
 *
 * A concrete factory will implement these methods and create products
 * belonging to the same family.
 *
 */
interface DatabaseFactory {

    public DatabaseConnection createConnection();

    public DatabaseCommand createCommand();

    public DatabaseTransaction createTransaction();
}

/**
 *
 * MysqlFactory: Concrete Factory
 *
 * Creates a family of MySQL-related products.
 *
 * Products created:
 *
 * - MysqlConnection
 * - MysqlCommand
 * - MysqlTransaction
 *
 */
class MysqlFactory implements DatabaseFactory {

    @Override
    public DatabaseConnection createConnection() {
        return new MysqlConnection();
    }

    @Override
    public DatabaseCommand createCommand() {
        return new MysqlCommand();
    }

    @Override
    public DatabaseTransaction createTransaction() {
        return new MysqlTransaction();
    }
}

/**
 *
 * Client
 *
 * The client works with the Abstract Factory and Abstract Product interfaces.
 *
 * It does not directly create concrete objects such as:
 *
 * new MysqlConnection()
 * new MysqlCommand()
 * new MysqlTransaction()
 *
 * Instead, it asks the factory to create them.
 *
 */
public class AbstractFactory {

    public static void main(String[] args) {

        /*
         * Create the concrete factory.
         *
         * The factory determines which product family will be created.
         *
         * In this case:
         * MysqlFactory -> MySQL product family
         */
        DatabaseFactory factory = new MysqlFactory();

        /*
         * Create products through the Abstract Factory.
         *
         * All three products belong to the same MySQL family.
         */
        DatabaseConnection mysqlConnection = factory.createConnection();

        DatabaseCommand mysqlCommand = factory.createCommand();

        DatabaseTransaction mysqlTransaction = factory.createTransaction();

        /*
         * Successful transaction
         */
        mysqlConnection.connect();

        mysqlTransaction.begin();

        mysqlCommand.execute(
                "UPDATE USERS SET name = 'Hari Om Ojha' WHERE id = 2;");

        mysqlTransaction.commit();

        mysqlConnection.disconnect();

        System.out.println("===================================");

        /*
         * Failed transaction
         *
         * Instead of committing the transaction,
         * we rollback the transaction.
         */
        mysqlConnection.connect();

        mysqlTransaction.begin();

        mysqlCommand.execute(
                "UPDATE USERS SET name = 'Hariom Ojha' WHERE id = 2;");

        mysqlTransaction.rollback();

        mysqlConnection.disconnect();
    }
}
