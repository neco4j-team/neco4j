package org.neco4j.collect.set;

import org.neco4j.collect.Addable;
import org.neco4j.collect.unitkey.Opt;
import org.neco4j.tuple.Unit;


/**
 * A set-like collection, where the add operation never fails.
 * @param <K> the key type
 * @param <C> the collection self-type
 */
public interface SetLikeAddable<K,C extends SetLikeAddable<K,C>> extends Addable<K, Unit, C>, SetLike<K,C> {

    C add(K key);

    @Override
    default C add(K key, Unit unit) {
        return add(key);
    }

    @Override
    default Opt<C> addOpt(K key) {
        return Opt.some(add(key));
    }

    @Override
    default Opt<C> addOpt(K key, Unit unit) {
        return addOpt(key);
    }
}
