package org.neco4j.loop;

import org.neco4j.tuple.Pair;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

public interface BiLoop<A,B> extends Iterable<Pair<A,B>> {

    static <A,B> BiLoop<A,B> of(Iterator<Pair<A,B>> iterator) {
        return () -> iterator;
    }

    static <A,B> BiLoop<A,B> of(Iterable<Pair<A,B>> iterable) {
        return iterable::iterator;
    }

    static <A,B> BiLoop<A,B> of(Map<A,B> map) {
       return () -> new Iterator<Pair<A, B>>() {
           private Iterator<Map.Entry<A,B>> iterator = map.entrySet().iterator();

           @Override
           public boolean hasNext() {
               return iterator.hasNext();
           }

           @Override
           public Pair<A, B> next() {
               Map.Entry<A,B> entry = iterator.next();
               return Pair.of(entry.getKey(), entry.getValue());
           }
       };
    }

    static <A,B> BiLoop zip(Iterator<A> iteratorA, Iterator<B> iteratorB) {
        return () -> new Iterator<Pair>() {
            @Override
            public boolean hasNext() {
                return iteratorA.hasNext() && iteratorB.hasNext();
            }

            @Override
            public Pair next() {
                return Pair.of(iteratorA.next(), iteratorB.next());
            }
        };
    }

    static <A,B> BiLoop<A,B> zip(Iterable<A> iterableA, Iterable<B> iterableB) {
        return zip(iterableA.iterator(), iterableB.iterator());
    }

    default void loop(BiConsumer<? super A, ? super B> consumer) {
        forEach(pair -> consumer.accept(pair.get1(), pair.get2()));
    }

}
