package org.neco4j.collect;

import java.util.Iterator;

/**
 * A collection with unit key which never becomes empty, so get and remove operations will always succeed.
 * @param <V> the element type
 * @param <C> the collection self-type
 */
public interface InfiniteWithUnitKey<V, C extends InfiniteWithUnitKey<V,C>> extends WithUnitKey<V, C> {

    @Override
    default boolean isEmpty() {
        return false;
    }

    @Override
    default long size() {
        return Long.MAX_VALUE;
    }

    V get();

    @Override
    default Opt<V> getOpt() {
        return Opt.some(get());
    }

    C remove();

    @Override
    default Opt<C> removeOpt() {
        return Opt.some(remove());
    }

    @Override
    default Iterator<V> iterator() {
        return new Iterator<V>() {
            C coll = InfiniteWithUnitKey.this.self();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public V next() {
                V result = coll.get();
                coll = coll.remove();
                return result;
            }
        };
    }
}
