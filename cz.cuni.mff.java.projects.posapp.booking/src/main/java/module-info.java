module cz.cuni.mff.java.projects.posapp.booking {
    requires cz.cuni.mff.java.projects.posapp.core;
    requires java.desktop;

    exports cz.cuni.mff.java.projects.posapp.plugins.booking;

    provides cz.cuni.mff.java.projects.posapp.plugins.POSPlugin
            with cz.cuni.mff.java.projects.posapp.plugins.booking.Plugin;

}