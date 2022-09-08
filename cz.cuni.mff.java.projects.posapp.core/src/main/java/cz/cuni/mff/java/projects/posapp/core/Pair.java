package cz.cuni.mff.java.projects.posapp.core;

/**
 * Basic pair of objects.
 * @param <A> first pair member
 * @param <B> second pair member
 */
public class Pair<A, B>{
    private A a;
    private B b;

    /**
     * Get the first pair member.
     * @return the stored value
     */
    public A getA() {
        return a;
    }

    /**
     * Set the first pair member.
     * @param a new value
     */
    public void setA(A a) {
        this.a = a;
    }

    /**
     * Get the second pair member.
     * @return the stored value
     */
    public B getB() {
        return b;
    }

    /**
     * Set the second pair member.
     * @param b new value
     */
    public void setB(B b) {
        this.b = b;
    }

    /**
     * Initialise the Pair
     * @param a first pair member
     * @param b second pair member
     */
    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }
}
