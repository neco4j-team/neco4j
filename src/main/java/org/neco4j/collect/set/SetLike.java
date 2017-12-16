package org.neco4j.collect.set;


import org.neco4j.collect.Coll;
import org.neco4j.collect.unitkey.Opt;
import org.neco4j.tuple.Pair;
import org.neco4j.tuple.Unit;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

/**
 * A collection focused on the mere existence of keys, without associated values.
 *
 * @param <K> the key type
 * @param <C> the collection self-type
 */
public interface SetLike<K, C extends SetLike<K, C>> extends Coll<K, Unit, C>, Iterable<K> {

    Opt<Unit> UNIT_OPT = Opt.from(Unit.unit);

    @Override
    default Opt<C> addOpt(K key, Unit unit) {
        return addOpt(key);
    }

    Opt<C> addOpt(K key);

    @Override
    default Opt<Unit> getOpt(K key) {
        return contains(key) ? UNIT_OPT : Opt.none();
    }

    @Override
    default Opt<C> putOpt(K key, Unit unit) {
        return putOpt(key);
    }

    Opt<C> putOpt(K key);

    @Override
    default Iterable<Pair<K, Unit>> asKeyValuePairs() {
        return () -> new Iterator<Pair<K, Unit>>() {
            private final Iterator<K> underlying = SetLike.this.iterator();

            @Override
            public boolean hasNext() {
                return underlying.hasNext();
            }

            @Override
            public Pair<K, Unit> next() {
                return Pair.of(underlying.next(), Unit.unit);
            }
        };
    }

    @Override
    default Unit getOrFail(K key) throws NoSuchElementException {
        if (contains(key)) {
            return Unit.unit;
        }
        throw new NoSuchElementException();
    }

    @Override
    default Unit getOrElse(K k, Unit defaultValue) {
        return Unit.unit;
    }

    boolean contains(K key);

    default String show(String collectionName) {
        StringJoiner joiner = new StringJoiner(", ");
        this.forEach(v -> joiner.add(v.toString()));
        return String.format("%s[%s]", collectionName, joiner.toString());
    }

    default String show() {
        return show(getClass().getSimpleName());
    }
}
