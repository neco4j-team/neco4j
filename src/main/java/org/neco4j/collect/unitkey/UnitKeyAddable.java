package org.neco4j.collect.unitkey;

import org.neco4j.collect.Addable;
import org.neco4j.tuple.Unit;

/**
 * A collection with unit key, where the add operation never fails.
 * @param <V> the element type
 * @param <C> the collection self-type
 */
public interface UnitKeyAddable<V, C extends UnitKeyAddable<V, C>> extends Addable<Unit, V, C>, UnitKey<V,C> {

    C add(V v);

    default C add(Unit unit, V v) {
        return add(v);
    }

    default Opt<C> addOpt(V v) {
        return Opt.some(add(v));
    }

    default Opt<C> addOpt(Unit u, V v) {
        return addOpt(v);
    }

    default C addAll(V ... vs) {
        C result = this.self();
        for(V v : vs) {
            result = result.add(v);
        }
        return result;
    }

    default C addAll(Iterable<V> vs) {
        C result = this.self();
        for(V v : vs) {
            result = result.add(v);
        }
        return result;
    }

}
