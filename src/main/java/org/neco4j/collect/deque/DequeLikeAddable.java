package org.neco4j.collect.deque;

import org.neco4j.collect.unitkey.Opt;

public interface DequeLikeAddable<V, C extends DequeLikeAddable<V, C>> extends DequeLike<V, C> {

    C addFirst(V v);

    C addLast(V v);

    @Override
    default Opt<C> addFirstOpt(V v) {
        return Opt.some(addFirst(v));
    }

    @Override
    default Opt<C> addLastOpt(V v) {
        return Opt.some(addLast(v));
    }
}
