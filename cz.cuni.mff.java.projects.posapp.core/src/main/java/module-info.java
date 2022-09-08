/**
 * Core module of the application. Handles loading of other plugins and their management.
 * All plugins must depend on this module.
 * Plugins providing an interface must export the POSPlugin implementation.
 */
module cz.cuni.mff.java.projects.posapp.core {
    requires java.logging;
    requires java.desktop;

    exports cz.cuni.mff.java.projects.posapp.plugins;
    exports cz.cuni.mff.java.projects.posapp.core;

    uses cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;
}