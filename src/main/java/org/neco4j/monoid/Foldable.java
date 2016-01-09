package org.neco4j.monoid;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.neco4j.collect.List;
import org.neco4j.collect.StrictList;
import org.neco4j.function.Functions;

public interface Foldable<A> extends Iterable<A> {

    default A fold(Monoid<A> monoid) {
        A result = monoid.identity();
        for (A a : this) {
            result = monoid.apply(result, a);
        }
        return result;
    }

    default <B> B foldMap(Monoid<B> monoid, Function<? super A, ? extends B> fn) {
        B result = monoid.identity();
        for (A a : this) {
            result = monoid.apply(result, fn.apply(a));
        }
        return result;
    }

    default <B> B foldLeft(B seed, BiFunction<? super B, ? super A, ? extends B> fn) {
        B result = seed;
        for (A a : this) {
            result = fn.apply(result, a);
        }
        return result;
    }

    //please override if the data structure allows a reverse traversal
    default <B> B foldRight(BiFunction<? super A, ? super B, ? extends B> fn, B seed) {
        List<A> list = StrictList.empty();
        for (A a : this) {
            list = list.plus(a);
        }
        return list.foldLeft(seed, Functions.swap(fn));
    }

    default boolean any(Predicate<? super A> predicate) {
        return foldMap(Monoids.booleanOr, a -> predicate.test(a));
    }

    default boolean all(Predicate<? super A> predicate) {
        return foldMap(Monoids.booleanAnd, a -> predicate.test(a));
    }

    default <B> Foldable<B> map(Function<? super A, ? extends B> fn) {
        return () -> new Iterator<B>() {
            Iterator<A> it = Foldable.this.iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public B next() {
                return fn.apply(it.next());
            }
        };
    }
}
