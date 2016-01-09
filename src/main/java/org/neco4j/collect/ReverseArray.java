package org.neco4j.collect;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ReverseArray<A> implements Iterable<A> {

    private final A[] array;

    public ReverseArray(A[] array) {
        this.array = array;
    }

    @Override
    public Iterator<A> iterator() {
        return new Iterator<A>() {

            private int last = array.length;

            @Override
            public boolean hasNext() {
                return last > 0;
            }

            @Override
            public A next() {
                if (! hasNext()) {
                    throw new NoSuchElementException();
                }
                return array[--last];
            }
        };
    }
}
