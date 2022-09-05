package cz.cuni.mff.java.projects.posapp.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Database implements AutoCloseable {

    Connection connection;

    private static Database instance;  // Singleton


    /**
     * Request the Database connection instance. Validates that the table definitions requested
     * by the plugin client are present in the database.
     *
     * TODO: Singleton -> Factory and enable multiple database connections?
     * @param user defining database connection details
     * @param client providing table definitions to validate
     * @return the Singleton instance
     */
    public static Database getInstance(DBUser user, DBClient client) {
        if(instance == null) {
            instance = new Database(user);
        }
        client.getTableDefs().entrySet().forEach(instance::verifyTable);
        client.getTableDefs().entrySet().forEach(instance::addForeignKeys);
        return instance;
    }


    /**
     * Ensure the driver is present and create a database connection.
     * @param user defines DB access details
     */
    Database(DBUser user) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            connection = DriverManager.getConnection(user.getURL(), user.getUserName(), user.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Modules requiring their unique tables will verify those tables exist in the database
     * on startup using this method. A table definition is provided and the table is guaranteed
     * to exist after calling this method.
     * TODO: stop SQL injection
     * @param tableDefEntry Entry [tableName: tableDef
     */
    public void verifyTable(Map.Entry<String, DBTableDef> tableDefEntry) {
        HashMap<String, String> tableColumns = tableDefEntry.getValue().getTableSchema();

        // Define table columns
        StringBuilder tableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableDefEntry.getKey() + " ( ");
        tableColumns.forEach((column, colType) ->
                tableQuery.append(column).append(" ").append(colType).append(", ")
        );

        // Primary key
        if(tableDefEntry.getValue().getPrimaryKey().contains("(")) {
            tableQuery.append(" PRIMARY KEY ")
                .append(tableDefEntry.getValue().getPrimaryKey())
                .append(" )");
        } else {
            tableQuery.append(" PRIMARY KEY (")
                    .append(tableDefEntry.getValue().getPrimaryKey())
                    .append(") )");
        }

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(tableQuery.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Add foreign keys to the database. Foreign keys added after table definitions
     * to avoid problems with ordering or cyclical references.
     * @param tableDefEntry table whose foreign keys to add
     */
    private void addForeignKeys(Map.Entry<String, DBTableDef> tableDefEntry) {

        if(tableDefEntry.getValue().getForeignKeys().size() == 0) {
            return;
        }
        tableDefEntry.getValue().getForeignKeys().forEach((key, val) -> {
            String tableQuery = "ALTER TABLE " +
                    tableDefEntry.getKey() +
                    " ADD FOREIGN KEY (" +
                    key +
                    ") REFERENCES " +
                    val.getA() +
                    "(" +
                    val.getB() +
                    ") ON DELETE CASCADE";
            query(tableQuery);
        });
    }


    /**
     * Execute the provided query string on the database connection.
     * TODO: Stop SQL injection?
     * @param query to execute
     * @return Result set or null if error or on UPDATE/INSERT.
     */
    public ResultSet query(String query) {
        try {
            Statement stmt = connection.createStatement();
            if(stmt.execute(query)) { // True if was SELECT
                return stmt.getResultSet();
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Execute the provided query string on the database connection.
     * Treats query as a transaction and commits at the end.
     * @param transactions to execute
     * @return Result set or null if error or on UPDATE/INSERT.
     */
    public ArrayList<ResultSet> doTransaction(Iterable<String> transactions) {
        try {
            connection.setAutoCommit(false);
            ArrayList<ResultSet> results = new ArrayList<>();
            transactions.forEach(t -> {
                results.add(query(t));
            });
            connection.commit();
            connection.setAutoCommit(true);
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public PreparedStatement prepareStatement(String statement) throws SQLException {
        return connection.prepareStatement(statement);
    }


    public void commit() throws SQLException {
        connection.commit();
    }


    /**
     * Close the database connection.
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
