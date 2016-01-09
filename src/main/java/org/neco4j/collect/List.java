package org.neco4j.collect;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public interface List<A> extends Sequence<A> {

    @Override
    A head() throws NoSuchElementException;

    @Override
    Optional<A> headOpt();

    @Override
    List<A> tail() throws NoSuchElementException;

    @Override
    Optional<? extends List<A>> tailOpt();

    A last() throws NoSuchElementException;

    List<A> init() throws NoSuchElementException;

    @Override
    List<A> plus(A... as);

    @Override
    boolean isEmpty();

    default int size() {
        int length = 0;
        for (List<A> current = this; !current.isEmpty(); current = current.tail()) {
            length++;
        }
        return length;
    }

    //all prefixes of the current list, ordered from empty to full list
    @Override
    List<List<A>> inits();

    //all suffixes of the current stream ordered from full list to empty
    @Override
    List<List<A>> tails();

    @Override
    <B> List<B> map(Function<? super A, ? extends B> fn);

    <B> List<B> flatMap(Function<A, ? extends List<B>> fn);

    List<A> reverse();

    @Override
    List<A> take(int n);

    @Override
    List<A> takeWhile(Predicate<A> predicate);

    default List<A> drop(int n) {
        List<A> result = this;
        for (int index = 0; index < n && !this.isEmpty(); index++) {
            result = result.tail();
        }
        return result;
    }

    default List<A> dropWhile(Predicate<? super A> predicate) {
        for (List<A> list = this; !list.isEmpty(); list = list.tail()) {
            if (!predicate.test(list.head())) {
                return list;
            }
        }
        return StrictList.empty();
    }

    @Override
    List<A> filter(Predicate<? super A> predicate);

    default <B> B foldLeft(B seed, BiFunction<? super B, ? super A, ? extends B> fn) {
        B result = seed;
        for (A a : this) {
            result = fn.apply(result, a);
        }
        return result;
    }

    default <B> B foldRight(BiFunction<? super A, ? super B, ? extends B> fn, B seed) {
        return reverse().foldLeft(seed, (b, a) -> fn.apply(a, b));
    }


    default A foldLeft1(BiFunction<? super A, ? super A, ? extends A> fn) {
        if (isEmpty()) {
            throw new NoSuchElementException("foldLeft1() call on NIL");
        }
        A result = head();
        for (A a : this.tail()) {
            result = fn.apply(result, a);
        }
        return result;
    }

    default A foldRight1(BiFunction<? super A, ? super A, ? extends A> fn) {
        if (isEmpty()) {
            throw new NoSuchElementException("foldRight1() call on NIL");
        }

        List<A> reversed = reverse();
        A result = reversed.head();
        for (A a : reversed.tail()) {
            result = fn.apply(a, result);
        }
        return result;
    }

    @Override
    <B> List<B> scanLeft(B seed, BiFunction<? super B, ? super A, ? extends B> fn);

    <B> List<B> scanRight(BiFunction<? super A, ? super B, ? extends B> fn, B seed);

    @Override
    List<A> scanLeft1(BiFunction<? super A, ? super A, ? extends A> fn);

    List<A> scanRight1(BiFunction<? super A, ? super A, ? extends A> fn);

    List<A> strict();

    List<A> lazy();

    @Override
    default boolean hasMinimumSize(int n) {
        List<A> list = this;
        for (int i = 0; i < n; i++) {
            if (list.isEmpty()) {
                return false;
            }
            list = list.tail();
        }
        return true;
    }
}
