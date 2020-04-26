package org.neco4j.collect;

public interface SelfTyped<C extends SelfTyped<C>> {

    @SuppressWarnings("unchecked")
    default C self() {
        return (C) this;
    }
}
