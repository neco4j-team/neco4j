package org.neco4j.collect.memo;

public class Evaluated<T> implements Memoized<T> {
    private final T t;

    public Evaluated(T t) {
        this.t = t;
    }

    @Override
    public Evaluated<T> evaluate() {
        return this;
    }

    public T get() {
        return t;
    }
}
