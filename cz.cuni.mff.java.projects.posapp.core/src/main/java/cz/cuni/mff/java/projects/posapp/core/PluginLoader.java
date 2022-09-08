package cz.cuni.mff.java.projects.posapp.core;

import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import java.util.ArrayList;
import java.util.ServiceLoader;

/**
 * Loader for the application plugins.
 */
public class PluginLoader {

    private final ServiceLoader<POSPlugin> serviceLoader;

    /**
     * Initialise the loader by loading using ServiceLoader.
     */
    public PluginLoader() {
        this.serviceLoader = ServiceLoader.load(POSPlugin.class);
    }

    /**
     * Finds the plugins and returns an array of instantiated plugins
     * @return the plugin instances
     */
    public ArrayList<POSPlugin> getPlugins() {
        ArrayList<POSPlugin> plugins = new ArrayList<>();
        serviceLoader.forEach(plugins::add);  // Instantiating here
        return plugins;
    }
}
