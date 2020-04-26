package org.neco4j.collect.unitkey;

import org.neco4j.collect.Coll;
import org.neco4j.tuple.Pair;
import org.neco4j.tuple.Unit;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;
import java.util.stream.StreamSupport;

/**
 * A collection where values are stored independent from a key or index.
 * <p>
 * Hence this interface provides versions of the collection methods which don't need key arguments.
 * Further, as the order of elements is fixed, the collection can implement the {@link Iterator} interface
 *
 * @param <V> the element type
 * @param <C> the collection self-type
 */
public interface UnitKey<V, C extends UnitKey<V, C>> extends Coll<Unit, V, C>, Iterable<V> {

    Opt<C> addOpt(V v);

    default Opt<C> addOpt(Unit u, V v) {
        return addOpt(v);
    }

    default C addIfPossible(V v) {
        return addOpt(v).orElse(this::self);
    }

    Opt<C> putOpt(V v);

    default Opt<C> putOpt(Unit u, V v) {
        return putOpt(v);
    }

    default C putIfPossible(V v) {
        return putOpt(v).orElse(this::self);
    }

    Opt<V> getOpt();

    default Opt<V> getOpt(Unit u) {
        return getOpt();
    }

    default V getOrFail() throws NoSuchElementException {
        return getOpt().getOrFail();
    }

    default V getOrElse(V defaultValue) {
        return getOpt().getOrElse(defaultValue);
    }

    Opt<C> removeOpt();

    default Opt<C> removeOpt(Unit u) {
        return removeOpt();
    }

    default C removeIfPossible() {
        return removeOpt().orElse(this::self);
    }

    default Iterator<V> iterator() {
        return new Iterator<V>() {
            private C _coll = UnitKey.this.self();

            @Override
            public boolean hasNext() {
                return !_coll.isEmpty();
            }

            @Override
            public V next() {
                if (hasNext()) {
                    V result = _coll.getOrFail();
                    _coll = _coll.removeOpt().getOrFail();
                    return result;
                }
                throw new NoSuchElementException();
            }
        };
    }

    default java.util.stream.Stream<V> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    @Override
    default Iterable<Pair<Unit, V>> asKeyValuePairs() {
        return () -> new Iterator<Pair<Unit, V>>() {
            private final Iterator<V> underlying = UnitKey.this.iterator();

            @Override
            public boolean hasNext() {
                return underlying.hasNext();
            }

            @Override
            public Pair<Unit, V> next() {
                return Pair.of(Unit.unit, underlying.next());
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
