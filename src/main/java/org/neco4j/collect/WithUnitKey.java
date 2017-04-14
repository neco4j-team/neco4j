package org.neco4j.collect;

import org.neco4j.tuple.Unit;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A collection where values are stored independent from a key or index.
 *
 * Hence this interface provides versions of the collection methods which don't need key arguments.
 * Further, as the order of elements is fixed, the collection can implement the Iterator interface
 * @param <V> the element type
 * @param <C> the collection self-type
 */
public interface WithUnitKey<V, C extends WithUnitKey<V, C>> extends Coll<Unit, V, C>, Iterable<V> {

    Opt<C> addOpt(V v);

    default Opt<C> addOpt(Unit u, V v) {
        return putOpt(v);
    }

    Opt<C> putOpt(V v);

    default Opt<C> putOpt(Unit u, V v) {
       return putOpt(v);
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

    default Iterator<V> iterator() {
       return new Iterator<V>() {
           private C _coll = WithUnitKey.this.self();

           @Override
           public boolean hasNext() {
               return ! _coll.isEmpty();
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

    default String show(String collectionName) {
        StringBuilder sb = new StringBuilder();
        for(V v : this) {
            sb.append(sb.length() == 0 ? "": ", ").append(v);
        }
        return String.format("%s[%s]", collectionName, sb);
    }

    default String show() {
        return show(getClass().getSimpleName());
    }
}
