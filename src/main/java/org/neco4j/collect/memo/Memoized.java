package org.neco4j.collect.memo;

public interface Memoized<T> {
    Evaluated<T> evaluate();
}
