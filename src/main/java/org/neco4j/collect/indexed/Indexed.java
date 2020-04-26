package org.neco4j.collect.indexed;

import org.neco4j.collect.Coll;
import org.neco4j.collect.unitkey.Opt;
import org.neco4j.tuple.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.StreamSupport;

/**
 * A random-access collection with successive integers (starting at 0) as keys.
 *
 * Note that the semantic of the add operation is to move all existing elements stored under the
 * given key or greater to their successor index.
 *
 * @param <V> the value type
 * @param <C> the collection self-type
 */
public interface Indexed<V, C extends Indexed<V,C>> extends Coll<Integer, V, C>, Iterable<V> {

    /**
     * Retrieves the first element of the indexed collection
     * @return the first element
     */
    default Opt<V> first() {
        return getOpt(0);
    }

    /**
     * Retrieves the last element of the indexed collection
     * @return the last element
     */
    default Opt<V> last() {
        return getOpt((int)size() - 1);
    }

    @Override
    default Iterator<V> iterator() {
        return new Iterator<V>() {

            private int _index = 0;

            @Override
            public boolean hasNext() {
                return _index < size();
            }

            @Override
            public V next() throws NoSuchElementException {
                if (hasNext()) {
                   return getOrFail(_index++);
                }
                throw new NoSuchElementException();
            }
        };
    }

    default java.util.stream.Stream<V> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    @Override
    default Iterable<Pair<Integer, V>> asKeyValuePairs() {
        return () -> new Iterator<Pair<Integer, V>>() {
            private int _index = 0;

            @Override
            public boolean hasNext() {
                return _index < size();
            }

            @Override
            public Pair<Integer, V> next() throws NoSuchElementException {
                if (hasNext()) {
                    return Pair.of(_index, getOrFail(_index++));
                }
                throw new NoSuchElementException();
            }
        };
    }

    default List<V> toList() {
        List<V> result = new ArrayList<>((int)size());
        for (V v : this) {
            result.add(v);
        }
        return result;
    }
}
