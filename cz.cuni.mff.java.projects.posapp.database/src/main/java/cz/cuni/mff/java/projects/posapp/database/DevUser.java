package cz.cuni.mff.java.projects.posapp.database;

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
