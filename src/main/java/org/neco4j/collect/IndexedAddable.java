package org.neco4j.collect;

/**
 * An indexed collection that can always grow
 * @param <V> the element type
 * @param <C> the collection self-type
 */
public interface IndexedAddable<V, C extends IndexedAddable<V,C>> extends Indexed<V,C> {

    /**
     * Add an element at the beginning of the indexed collection
     * @param v the new element
     * @return the enlarged collection
     */
    default C prepend(V v) {
        return addOpt(0, v).getOrFail();
    }

    /**
     * Add an element at the end of the indexed collection
     * @param v the new element
     * @return the enlarged collection
     */
    default C append(V v) {
        return addOpt((int) size(), v).getOrFail();
    }
}
