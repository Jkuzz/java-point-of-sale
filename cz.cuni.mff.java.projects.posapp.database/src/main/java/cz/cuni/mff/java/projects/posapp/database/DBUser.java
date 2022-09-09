package cz.cuni.mff.java.projects.posapp.database;

/**
 * Provides access details to the used database.
 */
public interface DBUser {
    /**
     * Get the username of the user to access the database with.
     * @return the username
     */
    String getUserName();

    /**
     * Get the password to the user to connect to the database with.
     * @return the password
     */
    String getPassword();

    /**
     * Provide the URL to access the database instance that will be used by the database connector.
     * @return the database access url
     */
    String getURL();
}
