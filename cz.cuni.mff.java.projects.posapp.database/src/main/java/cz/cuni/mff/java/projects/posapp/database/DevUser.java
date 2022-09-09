package cz.cuni.mff.java.projects.posapp.database;

/**
 * Database user with the development DB user login details
 */
public class DevUser implements DBUser {
    public String getUserName() {
        return "javaClient";
    }
    public String getPassword() {
        return "85*uYcjQ8yUH&Bv";
    }
    public String getURL() {
        return "jdbc:mysql://localhost/posappdb";
    }
}
