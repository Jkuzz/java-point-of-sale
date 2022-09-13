package cz.cuni.mff.java.projects.posapp.database;

import java.io.File;
import com.electronwill.nightconfig.core.file.FileConfig;


/**
 * DBUser that reads the required login details from a config file
 */
public class ConfigDBUser implements DBUser {

    private final String userName;
    private final String password;
    private final String dbURL;


    /**
     * Load the login details from config.toml
     */
    public ConfigDBUser() {
        System.out.println("Looking for database config file...");

        File cfgFile = new File("config.toml");
        if (cfgFile.exists()) {
            FileConfig config = FileConfig.of(cfgFile);
            config.load();

            password = config.get("database.password");
            userName = config.get("database.user_name");
            dbURL = config.get("database.url");
            if(password == null || userName == null || dbURL == null) {
                System.out.println("[ERROR]: missing database user config");
                throw new RuntimeException("Missing database configuration in config file!");
            }
        } else {
            System.out.println("The config file does not exist.");
            throw new RuntimeException("Missing configuration file!");
        }

        System.out.println("Database config successfully loaded.");
    }


    /**
     * Get the username of the user to access the database with.
     *
     * @return the username
     */
    @Override
    public String getUserName() {
        return userName;
    }

    /**
     * Get the password to the user to connect to the database with.
     *
     * @return the password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Provide the URL to access the database instance that will be used by the database connector.
     *
     * @return the database access url
     */
    @Override
    public String getURL() {
        return dbURL;
    }
}
