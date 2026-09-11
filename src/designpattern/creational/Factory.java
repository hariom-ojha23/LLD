package designpattern.creational;

/**
 * Factory
 *
 *
 * Factory pattern provides a way to create object without exposing the object
 * creation logic to client
 *
 * The client asks factory for an object & factory decides which concrete
 * objects should be created.
 *
 * Using the Factory Pattern we can centralize the object creation - Factory =
 * encapsulate object creation
 *
 *
 * Terminology: - Product: Common interface - Concrete products: Concrete
 * classes - Factory: Class responsible for object creation
 *
 *
 * 1. Simple factory pattern
 * - Usually one class handle object creation
 * - Often uses if-else or switch
 * - Simple to implement
 * - Factory knows concrete products
 * - Adding products may violate Open/Closed principle
 * 
 * 
 * 2. Factory method pattern:
 * - Uses subclasses to create object
 * - Uses method overriding
 * - More flexible & extensible
 * - Concrete factories know their products
 * - New product can be be added without violating Open/Closed principle
 *
 *
 */
interface DatabaseConnection {

    void connect();

    void disconnect();
}

class MySQLDB implements DatabaseConnection {

    @Override
    public void connect() {
        System.out.println("Connecting to mySQL");
    }

    @Override
    public void disconnect() {
        System.out.println("Disconnecting from mySQL");
    }
}

class MongoDB implements DatabaseConnection {

    @Override
    public void connect() {
        System.out.println("Connecting to mongodb");
    }

    @Override
    public void disconnect() {
        System.out.println("Disconnecting from mongodb");
    }
}

class PgSQLDB implements DatabaseConnection {

    @Override
    public void connect() {
        System.out.println("Connecting to pgSQL");
    }

    @Override
    public void disconnect() {
        System.out.println("Disconnecting from pgSQL");
    }
}

/**
 *
 * SimpleFactory
 *
 * Benefits: - Centralized object creation - Enforce encapsulation - Cleaner
 * code
 *
 * Drawbacks:
 * - Adding a new DB requires modifying the Factory
 * - This can violate the Open/Closed Principle
 *
 * Open/Closed Principle:
 * - Open for extension
 * - Closed for modification
 *
 */
class SimpleFactory {

    public static DatabaseConnection createConnection(String dbType) {
        if ("mySQL".equals(dbType)) {
            return new MySQLDB();
        }

        if ("mongoDB".equals(dbType)) {
            return new MongoDB();
        }

        if ("pgSQL".equals(dbType)) {
            return new PgSQLDB();
        }

        throw new IllegalArgumentException("Unsupported database: " + dbType);
    }
}

// =============================================================================================

/**
 * 
 * DatabaseConnectionFactory
 * 
 * The superclass defines the method for creating an object,
 * but subclasses decide which concrete object to create.
 * 
 * The object creation responsibility is delegated to subclasses.
 * 
 */
abstract class DatabaseConnectionFactory {

    public abstract DatabaseConnection createConnection();
}

/**
 * 
 * MySQLFactory
 */
class MySQLFactory extends DatabaseConnectionFactory {

    @Override
    public DatabaseConnection createConnection() {
        return new MySQLDB();
    }
}

/**
 * 
 * MongoDBFactory
 */
class MongoDBFactory extends DatabaseConnectionFactory {

    @Override
    public DatabaseConnection createConnection() {
        return new MongoDB();
    }
}

/**
 * 
 * PgSQLFactory
 */
class PgSQLFactory extends DatabaseConnectionFactory {

    @Override
    public DatabaseConnection createConnection() {
        return new PgSQLDB();
    }
}

public class Factory {

    public static void main(String[] args) {
        System.out.println("Simple factory example:");
        DatabaseConnection simpleConnection = SimpleFactory.createConnection("mySQL");

        simpleConnection.connect();
        simpleConnection.disconnect();

        simpleConnection = SimpleFactory.createConnection("mongoDB");
        simpleConnection.connect();
        simpleConnection.disconnect();

        System.out.println("=========================================");

        System.out.println("Factory method example:");

        DatabaseConnectionFactory mysqlFactory = new MySQLFactory();
        DatabaseConnection mysqlDB = mysqlFactory.createConnection();
        mysqlDB.connect();
        mysqlDB.disconnect();

        DatabaseConnectionFactory pgsqlFactory = new PgSQLFactory();
        DatabaseConnection pgsqlDB = pgsqlFactory.createConnection();
        pgsqlDB.connect();
        pgsqlDB.disconnect();

        DatabaseConnectionFactory mongoDBFactory = new MongoDBFactory();
        DatabaseConnection mongoDB = mongoDBFactory.createConnection();
        mongoDB.connect();
        mongoDB.disconnect();
    }
}
