package org.neco4j.collect.set;

import org.neco4j.collect.Puttable;
import org.neco4j.collect.unitkey.Opt;
import org.neco4j.tuple.Unit;

public interface SetLikePuttable<K, C extends SetLikePuttable<K,C>>
        extends SetLike<K,C>, Puttable<K, Unit, C> {

    C put(K key);

    @Override
    default C put(K key, Unit unit) {
        return put(key);
    }

    @Override
    default Opt<C> putOpt(K key) {
        return Opt.some(put(key));
    }

    @Override
    default Opt<C> putOpt(K key, Unit unit) {
        return putOpt(key);
    }
}
