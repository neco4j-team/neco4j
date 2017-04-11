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
       return addOpt(v);
    }

    Opt<V> getOpt();

    default Opt<V> getOpt(Unit u) {
        return getOpt();
    }

    Opt<C> removeOpt();

    default Opt<C> removeOpt(Unit u) {
        return removeOpt();
    }

    default Iterator<V> iterator() {
       return new Iterator<V>() {
           C coll = WithUnitKey.this.self();

           @Override
           public boolean hasNext() {
               return ! coll.isEmpty();
           }

           @Override
           public V next() {
               if (hasNext()) {
                  V result = coll.getOpt().getOrFail();
                  coll = coll.removeOpt().getOrFail();
                  return result;
               }
               throw new NoSuchElementException();
           }
       };
    }
}
