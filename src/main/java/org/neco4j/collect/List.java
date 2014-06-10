package org.neco4j.collect;

import org.neco4j.either.Either;
import org.neco4j.tuple.Pair;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public interface List<A> extends Iterable<A> {

    @SuppressWarnings("unchecked")
    public static <A> List<A> empty() {
        return (List<A>) Nil.NIL;
    }

    public static <A> List<A> cons(A head, List<A> tail) {
        return new Cons<>(head, tail);
    }

    @SafeVarargs
    public static <A> List<A> of(A... values) {
        List<A> result = empty();
        for (int i = values.length - 1; i >= 0; i--) {
            result = cons(values[i], result);
        }
        return result;
    }

    public A head() throws NoSuchElementException;

    public Optional<A> headOpt();

    public List<A> tail() throws NoSuchElementException;

    public Optional<List<A>> tailOpt();

    public A last() throws NoSuchElementException;

    public Optional<A> lastOpt();

    public List<A> init() throws NoSuchElementException;

    public Optional<List<A>> initOpt();

    public default A get(int index) throws IndexOutOfBoundsException {
        return getOpt(index).orElseThrow(IndexOutOfBoundsException::new);
    }

    public default Optional<A> getOpt(int index) {
        if (index < 0) {
            return Optional.empty();
        }
        List<A> current = this;
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

    public default List<A> with(int index, A value) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException("with() call with negative index");
        }
        int i = 0;
        List<A> result = empty();
        for (A a : this) {
            result = cons(i == index ? value : a, result);
            i++;
        }
        if (i < index) {
            throw new IndexOutOfBoundsException();
        }
        return result.reverse();
    }

    public default List<A> concat(List<A> that) {
        List<A> result = that;
        for (A a : this.reverse()) {
            result = cons(a, result);
        }
        return result;
    }

    public default List<A> take(int count) {
        List<A> result = empty();
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

    public default List<A> takeWhile(Predicate<A> predicate) {
        List<A> result = empty();
        for (A a : this) {
            if (!predicate.test(a)) {
                break;
            }
            result = cons(a, result);
        }
        return result.reverse();
    }

    public default List<A> drop(int count) {
        List<A> current = this;
        for (int index = 0; index < count && !current.isEmpty(); index++) {
            current = current.tail();
        }
        return current;
    }

    public default List<A> dropWhile(Predicate<A> predicate) {
        List<A> current = this;
        while (!current.isEmpty() && predicate.test(current.head())) {
            current = current.tail();
        }
        return current;
    }

    public default List<A> reverse() {
        List<A> result = empty();
        for (A a : this) {
            result = cons(a, result);
        }
        return result;
    }

    public default <B> List<B> map(Function<? super A, ? extends B> fn) {
        List<B> result = empty();
        for (A a : this) {
            result = cons(fn.apply(a), result);
        }
        return result.reverse();
    }

    public default <B> List<B> flatMap(Function<? super A, ? extends Iterable<? extends B>> fn) {
        List<B> result = empty();
        for (A a : this) {
            for (B b : fn.apply(a)) {
                result = cons(b, result);
            }
        }
        return result.reverse();
    }

    public default List<A> filter(Predicate<? super A> predicate) {
        List<A> result = empty();
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

    public default <B> List<Pair<A, B>> zip(List<B> that) {
        List<A> aList = this;
        List<B> bList = that;
        List<Pair<A, B>> result = List.empty();
        while (!aList.isEmpty() && !bList.isEmpty()) {
            result = cons(Pair.of(aList.head(), bList.head()), result);
            aList = aList.tail();
            bList = bList.tail();
        }
        return result.reverse();
    }

    public default <B> List<Pair<A, B>> strictZip(List<B> other) throws IllegalArgumentException {
        if (this.size() != other.size()) {
            throw new IllegalArgumentException("list sizes must match");
        }
        return zip(other);
    }

    @Override
    public default Iterator<A> iterator() {
        return new Iterator<A>() {

            private List<A> current = List.this;

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

    public static <A, B> List<A> lefts(List<Either<A, B>> list) {
        return list.flatMap(e -> e.fold(Collections::singleton, b -> Collections.emptyList()));
    }

    public static <A, B> List<B> rights(List<Either<A, B>> list) {
        return list.flatMap(e -> e.fold(a -> Collections.emptyList(), Collections::singleton));
    }

    public static <A, B> Pair<List<A>, List<B>> leftsRights(List<Either<A, B>> list) {
        return list.foldRight((e, ps) -> e.fold(
                        left -> Pair.of(cons(left, ps.get1()), ps.get2()),
                        right -> Pair.of(ps.get1(), cons(right, ps.get2()))),
                Pair.of(List.<A>empty(), List.<B>empty()));
    }

    public static <A, B> Pair<List<A>, List<B>> unzip(List<Pair<A, B>> list) {
        return list.foldRight((p, ps) -> Pair.<List<A>, List<B>>of(cons(p.get1(), ps.get1()), cons(p.get2(), ps.get2())),
                Pair.of(List.<A>empty(), List.<B>empty()));
    }
}
