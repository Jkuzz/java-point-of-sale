package cz.cuni.mff.java.projects.posapp.core;

public class Pair<A, B>{
    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    private A a;

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    private B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }
}
