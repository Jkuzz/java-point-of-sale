package cz.cuni.mff.java.projects.posapp.plugins.tables;

/**
 * Interface of the Prototype pattern
 */
public interface Prototype {
    /**
     * Clone the object to create a copy instance
     * @return the cloned object
     */
    Prototype clone();
}
