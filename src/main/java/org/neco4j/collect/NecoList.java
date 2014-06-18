package org.neco4j.collect;

import org.neco4j.either.Either;
import org.neco4j.tuple.Pair;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public interface NecoList<A> extends Iterable<A> {

    @SuppressWarnings("unchecked")
    public static <A> NecoList<A> empty() {
        return (NecoList<A>) Nil.NIL;
    }

    public static <A> NecoList<A> cons(A head, NecoList<A> tail) {
        return new Cons<>(head, tail);
    }

    @SafeVarargs
    public static <A> NecoList<A> of(A... values) {
        NecoList<A> result = empty();
        for (int i = values.length - 1; i >= 0; i--) {
            result = cons(values[i], result);
        }
        return result;
    }

    public A head() throws NoSuchElementException;

    public Optional<A> headOpt();

    public NecoList<A> tail() throws NoSuchElementException;

    public Optional<NecoList<A>> tailOpt();

    public A last() throws NoSuchElementException;

    public Optional<A> lastOpt();

    public NecoList<A> init() throws NoSuchElementException;

    public Optional<NecoList<A>> initOpt();

    public default A get(int index) throws IndexOutOfBoundsException {
        return getOpt(index).orElseThrow(IndexOutOfBoundsException::new);
    }

    public default Optional<A> getOpt(int index) {
        if (index < 0) {
            return Optional.empty();
        }
        NecoList<A> current = this;
        for (int i = 0; i < index && !current.isEmpty(); i++) {
            current = current.tail();
        }
        if (current.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(current.head());
    }

    public default int size() {
        return foldLeft(0, (n, a) -> n + 1);
    }

    public default boolean isEmpty() {
        return false;
    }

    public default NecoList<A> with(int index, A value) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException("with() call with negative index");
        }
        int i = 0;
        NecoList<A> result = empty();
        for (A a : this) {
            result = cons(i == index ? value : a, result);
            i++;
        }
        if (i < index) {
            throw new IndexOutOfBoundsException();
        }
        return result.reverse();
    }

    public default NecoList<A> concat(NecoList<A> that) {
        NecoList<A> result = that;
        for (A a : this.reverse()) {
            result = cons(a, result);
        }
        return result;
    }

    public default NecoList<A> take(int count) {
        NecoList<A> result = empty();
        int index = 0;
        for (A a : this) {
            if (index >= count) {
                break;
            }
            index++;
            result = cons(a, result);
        }
        return result.reverse();
    }

    public default NecoList<A> takeWhile(Predicate<A> predicate) {
        NecoList<A> result = empty();
        for (A a : this) {
            if (!predicate.test(a)) {
                break;
            }
            result = cons(a, result);
        }
        return result.reverse();
    }

    public default NecoList<A> drop(int count) {
        NecoList<A> current = this;
        for (int index = 0; index < count && !current.isEmpty(); index++) {
            current = current.tail();
        }
        return current;
    }

    public default NecoList<A> dropWhile(Predicate<A> predicate) {
        NecoList<A> current = this;
        while (!current.isEmpty() && predicate.test(current.head())) {
            current = current.tail();
        }
        return current;
    }

    public default NecoList<A> reverse() {
        NecoList<A> result = empty();
        for (A a : this) {
            result = cons(a, result);
        }
        return result;
    }

    public default <B> NecoList<B> map(Function<? super A, ? extends B> fn) {
        NecoList<B> result = empty();
        for (A a : this) {
            result = cons(fn.apply(a), result);
        }
        return result.reverse();
    }

    public default <B> NecoList<B> flatMap(Function<? super A, ? extends Iterable<? extends B>> fn) {
        NecoList<B> result = empty();
        for (A a : this) {
            for (B b : fn.apply(a)) {
                result = cons(b, result);
            }
        }
        return result.reverse();
    }

    public default NecoList<A> filter(Predicate<? super A> predicate) {
        NecoList<A> result = empty();
        for (A a : this) {
            if (predicate.test(a)) {
                result = cons(a, result);
            }
        }
        return result.reverse();
    }

    public default <B> B foldLeft(B start, BiFunction<? super B, ? super A, ? extends B> fn) {
        B value = start;
        for (A a : this) {
            value = fn.apply(value, a);
        }
        return value;
    }

    public default <B> B foldRight(BiFunction<? super A, ? super B, ? extends B> fn, B start) {
        B value = start;
        for (A a : this.reverse()) {
            value = fn.apply(a, value);
        }
        return value;
    }

    public default <B, C> NecoList<C> zipWith(NecoList<B> that, BiFunction<? super A, ? super B, ? extends C> fn) {
        NecoList<A> aList = this;
        NecoList<B> bList = that;
        NecoList<C> result = NecoList.empty();
        while (!aList.isEmpty() && !bList.isEmpty()) {
            result = cons(fn.apply(aList.head(), bList.head()), result);
            aList = aList.tail();
            bList = bList.tail();
        }
        return result.reverse();
    }

    public default <B, C> NecoList<C> strictZipWith(NecoList<B> that, BiFunction<? super A, ? super B, ? extends C> fn) {
        if (this.size() != that.size()) {
            throw new IllegalArgumentException("list sizes must match");
        }
        return zipWith(that, fn);
    }

    public default <B> NecoList<Pair<A, B>> zip(NecoList<B> that) {
        return zipWith(that, Pair::of);
    }

    public default <B> NecoList<Pair<A, B>> strictZip(NecoList<B> that) throws IllegalArgumentException {
        if (this.size() != that.size()) {
            throw new IllegalArgumentException("list sizes must match");
        }
        return zip(that);
    }

    @Override
    public default Iterator<A> iterator() {
        return new Iterator<A>() {

            private NecoList<A> current = NecoList.this;

            @Override
            public boolean hasNext() {
                return !current.isEmpty();
            }

            @Override
            public A next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                A value = current.head();
                current = current.tail();
                return value;
            }
        };
    }

}
