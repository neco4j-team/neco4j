package org.neco4j.collect;

import org.neco4j.collect.unitkey.Opt;

/**
 * A collection with unlimited capacity, where the add operation will never fail.
 * @param <K> the key or index type for accessing elements
 * @param <V> the element type
 * @param <C> the collection self-type
 */
public interface Addable<K, V, C extends Addable<K, V, C>> extends Coll<K,V,C>  {

    /**
     * Adds an element to the collection.
     * @param k the key or index
     * @param v the value
     * @return the collection
     */
    C add(K k, V v);

    default Opt<C> addOpt(K k, V v) {
        return Opt.some(add(k, v));
    }
}
