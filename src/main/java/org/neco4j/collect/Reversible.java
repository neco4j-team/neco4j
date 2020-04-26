package org.neco4j.collect;

public interface Reversible<C extends Reversible<C>> {
    C reverse();
}
