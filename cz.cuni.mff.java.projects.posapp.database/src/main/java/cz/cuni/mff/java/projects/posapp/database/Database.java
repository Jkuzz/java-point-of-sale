package cz.cuni.mff.java.projects.posapp.database;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class Database implements AutoCloseable {

    Connection connection;

    private static Database instance;  // Singleton

    public static Database getInstance(DBUser user, DBClient client) {
        if(instance == null) {
            instance = new Database(user);
        }
        client.getTableDefs().entrySet().forEach(instance::verifyTable);
        return instance;
    }


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

        StringBuilder tableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableDefEntry.getKey() + " ( ");
        tableColumns.forEach((column, colType) ->
                tableQuery.append(column).append(" ").append(colType).append(", ")
        );
        tableQuery.append(" PRIMARY KEY( ")
                .append(tableDefEntry.getValue().getPrimaryKey())
                .append(" ))");
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(tableQuery.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Execute the provided query string on the database connection.
     * TODO: Stop SQL injection?
     * @param query to execute
     * @return Result set or null if error occurred.
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
