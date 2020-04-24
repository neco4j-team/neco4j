package org.neco4j.collect.deque;

import org.neco4j.collect.Coll;
import org.neco4j.collect.unitkey.Opt;
import org.neco4j.tuple.Pair;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

public interface DequeLike<V, C extends DequeLike<V, C>> extends Coll<DequeKey, V, C>, Iterable<V> {

    Opt<C> addFirstOpt(V v);

    Opt<C> addLastOpt(V v);

    @Override
    default Opt<C> addOpt(DequeKey dequeKey, V v) {
        return dequeKey == DequeKey.FIRST
                   ? addFirstOpt(v)
                   : addLastOpt(v);
    }

    Opt<C> putFirstOpt(V v);

    Opt<C> putLastOpt(V v);

    @Override
    default Opt<C> putOpt(DequeKey dequeKey, V v) {
        return dequeKey == DequeKey.FIRST
                   ? putFirstOpt(v)
                   : putLastOpt(v);
    }

    Opt<V> getFirstOpt();

    Opt<V> getLastOpt();

    @Override
    default Opt<V> getOpt(DequeKey dequeKey) {
        return dequeKey == DequeKey.FIRST
                   ? getFirstOpt()
                   : getLastOpt();
    }

    default V getFirstOrFail() throws NoSuchElementException {
        return getFirstOpt().getOrFail();
    }

    default V getLastOrFail() throws NoSuchElementException {
        return getLastOpt().getOrFail();
    }

    default V getFirstOrElse(V defaultValue) {
        return getFirstOpt().getOrElse(defaultValue);
    }

    default V getLastOrElse(V defaultValue) {
        return getLastOpt().getOrElse(defaultValue);
    }

    Opt<C> removeFirstOpt();

    Opt<C> removeLastOpt();

    @Override
    default Opt<C> removeOpt(DequeKey dequeKey) {
        return dequeKey == DequeKey.FIRST
                   ? removeFirstOpt()
                   : removeLastOpt();
    }

    default Iterator<V> iterator() {
        return new Iterator<V>() {
            private C _coll = DequeLike.this.self();

            @Override
            public boolean hasNext() {
                return !_coll.isEmpty();
            }

            @Override
            public V next() {
                if (hasNext()) {
                    V result = _coll.getFirstOrFail();
                    _coll = _coll.removeFirstOpt().getOrFail();
                    return result;
                }
                throw new NoSuchElementException();
            }
        };
    }

    @Override
    default Iterable<Pair<DequeKey, V>> asKeyValuePairs() {
        return () -> new Iterator<Pair<DequeKey, V>>() {
            private final Iterator<V> underlying = DequeLike.this.iterator();

            @Override
            public boolean hasNext() {
                return underlying.hasNext();
            }

            @Override
            public Pair<DequeKey, V> next() {
                return Pair.of(DequeKey.FIRST, underlying.next());
            }
        };
    }

    default String show(String collectionName) {
        StringJoiner joiner = new StringJoiner(", ");
        this.forEach(v -> joiner.add(v.toString()));
        return String.format("%s[%s]", collectionName, joiner.toString());
    }

    default String show() {
        return show(getClass().getSimpleName());
    }

}
