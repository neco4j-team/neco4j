package org.neco4j.collect;

public interface Reversable<C extends Reversable<C>> {
    C reverse();
}
