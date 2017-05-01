package org.neco4j.collect.unitkey;

import org.neco4j.collect.Puttable;
import org.neco4j.tuple.Unit;

/**
 * A collection with unit key, where the put operation never fails.
 *
 * @param <V> the element type
 * @param <C> the collection self-type
 */
public interface UnitKeyPuttable<V, C extends UnitKeyPuttable<V, C>> extends UnitKey<V, C>, Puttable<Unit, V, C> {

    C put(V v);

    @Override
    default C put(Unit unit, V v) {
        return put(v);
    }

    @Override
    default Opt<C> putOpt(V v) {
        return Opt.some(put(v));
    }

    @Override
    default Opt<C> putOpt(Unit u, V v) {
        return putOpt(v);
    }

}
