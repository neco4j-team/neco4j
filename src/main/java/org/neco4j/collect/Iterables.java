package org.neco4j.collect;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Iterables {

    private Iterables() {
        throw new UnsupportedOperationException("do not instantiate");
    }

    @SafeVarargs
    public static <A> Iterable<A> reverse(A... array) {
        return () -> new Iterator<A>() {

            private int last = array.length;

            @Override
            public boolean hasNext() {
                return last > 0;
            }

            @Override
            public A next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return array[--last];
            }
        };
    }
}
