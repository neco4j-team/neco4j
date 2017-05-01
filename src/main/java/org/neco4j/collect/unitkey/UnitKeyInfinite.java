package org.neco4j.collect.unitkey;

import org.neco4j.collect.Infinite;
import org.neco4j.tuple.Unit;

import java.util.Iterator;

/**
 * A collection with unit key which never becomes empty, so get and remove operations will always succeed.
 * @param <V> the element type
 * @param <C> the collection self-type
 */
public interface UnitKeyInfinite<V, C extends UnitKeyInfinite<V,C>> extends UnitKey<V, C>, Infinite<Unit, V, C> {

    @Override
    default boolean isEmpty() {
        return false;
    }

    V get();

    @Override
    default V get(Unit unit) {
        return get();
    }

    @Override
    default Opt<V> getOpt() {
        return Opt.some(get());
    }

    @Override
    default Opt<V> getOpt(Unit unit) {
        return Opt.some(get());
    }

    C remove();

    @Override
    default C remove(Unit unit) {
        return remove();
    }

    @Override
    default Opt<C> removeOpt() {
        return Opt.some(remove());
    }

    @Override
    default Opt<C> removeOpt(Unit unit) {
        return Opt.some(remove());
    }

    @Override
    default Iterator<V> iterator() {
        return new Iterator<V>() {
            C coll = UnitKeyInfinite.this.self();

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
