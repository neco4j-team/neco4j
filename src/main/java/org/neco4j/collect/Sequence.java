package org.neco4j.collect;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.neco4j.monoid.Foldable;

public interface Sequence<A> extends Foldable<A> {

    boolean isEmpty();

    A head() throws NoSuchElementException;

    Optional<A> headOpt();

    Sequence<A> tail() throws NoSuchElementException;

    Optional<? extends Sequence<A>> tailOpt();

    Sequence<A> plus(A a);

    Optional<A> getOpt(long index);

    A get(long index) throws IndexOutOfBoundsException;

    //all prefixes of the current stream
    Sequence<List<A>> inits();

    //all suffixes of the current stream
    Sequence<? extends Sequence<A>> tails();

    <B> Sequence<B> map(Function<? super A, ? extends B> fn);

    Sequence<A> drop(int n);

    Sequence<A> dropWhile(Predicate<? super A> predicate);

    List<A> takeWhile(Predicate<A> predicate);

    List<A> take(int n);

    Sequence<A> filter(Predicate<? super A> predicate);

    <B> Sequence<B> scanLeft(B seed, BiFunction<? super B, ? super A, ? extends B> fn);

    Sequence<A> scanLeft1(BiFunction<? super A, ? super A, ? extends A> fn);

    boolean isLazy();

}
