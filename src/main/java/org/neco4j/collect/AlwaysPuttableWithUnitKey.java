package org.neco4j.collect;

/**
 * A collection with unit key, where the put operation never fails.
 * @param <V> the element type
 * @param <C> the collection self-type
 */
public interface AlwaysPuttableWithUnitKey<V, C extends AlwaysPuttableWithUnitKey<V, C>> extends WithUnitKey<V,C> {

    C put(V v);

    default Opt<C> putOpt(V v) {
        return Opt.some(put(v));
    }

}
