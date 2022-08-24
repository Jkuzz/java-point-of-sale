package cz.cuni.mff.java.projects.posapp.core;

import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import java.util.ArrayList;
import java.util.ServiceLoader;

public class PluginLoader {

    private final ServiceLoader<POSPlugin> serviceLoader;

    public PluginLoader() {
        this.serviceLoader = ServiceLoader.load(POSPlugin.class);
    }

    public ArrayList<POSPlugin> getPlugins() {
       ArrayList<POSPlugin> plugins = new ArrayList<>();
       serviceLoader.forEach(plugins::add);
       return plugins;
    }
}
