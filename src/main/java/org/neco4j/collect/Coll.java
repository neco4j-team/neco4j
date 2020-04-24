package org.neco4j.collect;

import org.neco4j.collect.unitkey.Opt;
import org.neco4j.tuple.Pair;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * A general-purpose immutable collection.
 * @param <K> the key or index type for accessing elements
 * @param <V> the element type
 * @param <C> the collection self-type
 */
public interface Coll<K, V, C extends Coll<K, V, C>> {

    /**
     * Adds an entry to the collection, if possible. Keeps other elements stored under that key.
     *
     * Usually, after a successful call, the size of the collection has increased by one (with exceptions
     * where "adding" is done different, e.g. MultiSet).
     * @param k the key or index
     * @param v the value
     * @return if successful, an enlarged collection wrapped in an Opt, else Opt.none
     */
    Opt<C> addOpt(K k, V v);

    default C addIfPossible(K k, V v) {
        return addOpt(k, v).orElse(() -> self());
    }

    /**
     * Adds an entry to the collection, if possible. Removes the element stored under this key.
     *
     * Usually, the size of the collection should not change by this operation if there exists already an element under
     * that key.
     * @param k the key or index
     * @param v the value
     * @return if successful, a modified collection wrapped in an Opt, else Opt.none
     */
    Opt<C> putOpt(K k, V v);

    default C putIfPossible(K k, V v) {
        return putOpt(k, v).orElse(() -> self());
    }

    /**
     * Retrieves an element of the collection, if possible
     *
     * Usually, this operation should be able to retrieve elements which were stored by putAll or addAll.
     * @param k the key or index
     * @return if successful, the element wrapped in an Opt, else Opt.none
     */
    Opt<V> getOpt(K k);

    /**
     * Returns this collection as {@link Iterable} of key-value pairs. Note that {@link Coll} doesn't
     * implement {@link Iterable} directly, as there might be more useful element types than key-value pairs.
     * @return an {@link Iterable}
     */
    Iterable<Pair<K,V>> asKeyValuePairs();

    /**
     * Retrieves an element of the collection, or throws an exception if the collection is empty.
     * @param k the key or index
     * @return the element, if successful
     * @throws NoSuchElementException  if empty
     */
    default V getOrFail(K k) throws NoSuchElementException {
        return getOpt(k).getOrFail();
    }

    /**
     * Retrieves an element of the collection, or returns a given default value
     * @param k the key or index
     * @param defaultValue the defaultValue
     * @return the element, or the default value if empty
     */
    default V getOrElse(K k, V defaultValue) {
        return getOpt(k).getOrElse(defaultValue);
    }

    /**
     * Retrieves an element of the collection, or returns a given default value
     * @param k the key or index
     * @param supplier the Supplier for the defaultValue
     * @return the element, or the default value if empty
     */
    default V getOrElse(K k, Supplier<V> supplier) {
        return getOpt(k).orElse(supplier);
    }


    /**
     * Removes an element from the collection, if possible
     * @param k the key or index
     * @return if successful, an narrowed collection wrapped in an Opt, else Opt.none
     */
    Opt<C> removeOpt(K k);

    default C removeIfPossible(K k) {
        return removeOpt(k).orElse(() -> self());
    }

    /**
     * Checks whether a collection is empty.
     * @return true if empty
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the size of the collection, or Long.MAX_VALUE for infinite collections
     * @return the size
     */
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
