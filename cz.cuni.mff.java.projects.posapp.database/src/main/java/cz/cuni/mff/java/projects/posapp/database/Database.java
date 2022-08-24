package cz.cuni.mff.java.projects.posapp.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database implements AutoCloseable {

    private Connection connection;

    public Database(DBUser user, DBClient client) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(
                    user.getURL(), user.getUserName(), user.getPassword()
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        client.getTableDefs().forEach(this::verifyTable);
    }

    /**
     * Modules requiring their unique tables will verify those tables exist in the database
     * on startup using this method. A table definition is provided and the table is guaranteed
     * to exist after calling this method.
     * TODO: stop SQL injection
     * @param table Will request table by providing table definitions
     */
    public void verifyTable(DBTableDef table) {
        HashMap<String, String> tableColumns = table.getTableSchema();

        StringBuilder tableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS " + table.getTableName() + " ( ");
        tableColumns.forEach((column, colType) ->
                tableQuery.append(column).append(" ").append(colType).append(", ")
        );
        tableQuery.append(" PRIMARY KEY( ")
                .append(table.getPrimaryKey())
                .append(" ))");

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(tableQuery.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet query(String query) {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
