package org.neco4j.collect;

import org.neco4j.collect.unitkey.Opt;

/**
 * A collection which never becomes empty, so get and remove operations will always succeed.
 *
 * @param <K> the key type
 * @param <V> the value type
 * @param <C> the collection self-type
 */
public interface Infinite<K, V, C extends Infinite<K, V, C>> extends Coll<K,V,C>  {

    V get(K k);

    @Override
    default Opt<V> getOpt(K k) {
        return Opt.some(get(k));
    }

    C remove(K k);

    @Override
    default Opt<C> removeOpt(K k) {
        return Opt.some(remove(k));
    }

    @Override
    default long size() {
        return Long.MAX_VALUE;
    }
}
