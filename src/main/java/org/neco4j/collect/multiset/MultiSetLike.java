package org.neco4j.collect.multiset;

import org.neco4j.collect.Puttable;
import org.neco4j.collect.unitkey.Opt;

public interface MultiSetLike<K, C extends MultiSetLike<K,C>> extends Puttable<K, Integer, C> {

    default C add(K k) {
        return addIfPossible(k, 1);
    }

    default C put(K k) {
        return put(k, 1);
    }

    int get(K k);

    default Opt<Integer> getOpt(K k) {
        return Opt.some(get(k));
    }
}
