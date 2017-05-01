package org.neco4j.collect;

import org.neco4j.collect.unitkey.Opt;

/**
 * A collection where the put operation never fails
 *
 * @param <K> the key type
 * @param <V> the value type
 * @param <C> the collection self-type
 */
public interface Puttable<K, V, C extends Puttable<K, V, C>> extends Coll<K, V, C> {

    /**
     * Replaces the element under the given key by the given value.
     *
     * @param k the key
     * @param v the value
     * @return the modified collection
     */
    C put(K k, V v);

    @Override
    default Opt<C> putOpt(K k, V v) {
        return Opt.some(put(k, v));
    }
}
