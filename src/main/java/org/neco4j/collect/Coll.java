package org.neco4j.collect;

/**
 * A general-purpose immutable collection.
 * @param <K> the key or index type for accessing elements
 * @param <V> the element type
 * @param <C> the collection self-type
 */
public interface Coll<K, V, C extends Coll<K, V, C>> {

    /**
     * Adds an entry to the collection, if possible
     * @param k the key or index
     * @param v the value
     * @return if successful, an enlarged collection wrapped in an Opt, else Opt.none
     */
    Opt<C> addOpt(K k, V v);

    /**
     * Retrieves an element of the collection, if possible
     * @param k the key or index
     * @return if successful, the element wrapped in an Opt, else Opt.none
     */
    Opt<V> getOpt(K k);

    /**
     * Removes an element from the collection, if possible
     * @param k the key or index
     * @return if successful, an narrowed collection wrapped in an Opt, else Opt.none
     */
    Opt<C> removeOpt(K k);

    default boolean isEmpty() {
        return size() == 0;
    }

    long size();

    /**
     * Returns the collection casted to its self-type
     * @return the collection
     */
    @SuppressWarnings("unchecked")
    default C self() {
        return (C) this;
    }
}
