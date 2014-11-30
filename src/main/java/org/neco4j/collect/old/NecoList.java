package org.neco4j.collect.old;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface NecoList<A> extends Iterable<A> {

    @SuppressWarnings("unchecked")
    public static <A> NecoList<A> empty() {
        return (NecoList<A>) Nil.NIL;
    }

    public static <A> NecoList<A> cons(A head, NecoList<A> tail) {
        return new Cons<>(head, tail);
    }

    public static <A> NecoList<A> cons(A head, Supplier<NecoList<A>> tailSupplier) {
        return new LazyCons<>(head, tailSupplier);
    }

    @SafeVarargs
    public static <A> NecoList<A> of(A... values) {
        NecoList<A> result = empty();
        for (int i = values.length - 1; i >= 0; i--) {
            result = cons(values[i], result);
        }
        return result;
    }

    public static <A> NecoList<A> from(Iterable<A> iterable) {
        return from(iterable.iterator());
    }

    public static <A> NecoList<A> from(Iterator<A> iterator) {
        return iterator.hasNext()
                ? NecoList.<A>cons(iterator.next(), () -> from(iterator))
                : NecoList.empty();
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
        if (isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        return index == 0
                ? cons(value, tail())
                : cons(head(), () -> tail().with(index - 1, value));
    }

    public default NecoList<A> concat(NecoList<A> that) {
        return isEmpty() ? that : cons(head(), () -> tail().concat(that));
    }

    public default NecoList<A> take(int count) {
        return isEmpty() || count <= 0
                ? empty()
                : cons(head(), () -> tail().take(count - 1));
    }

    public default NecoList<A> takeWhile(Predicate<A> predicate) {
        return isEmpty() || !predicate.test(head())
                ? empty()
                : cons(head(), () -> tail().takeWhile(predicate));
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
        return isEmpty()
                ? NecoList.empty()
                : NecoList.<B>cons(fn.apply(head()), () -> tail().map(fn));
    }

    public default <B> NecoList<B> flatMap(Function<? super A, ? extends Iterable<B>> fn) {
        return isEmpty()
                ? NecoList.<B>empty()
                : from(fn.apply(head())).concat(tail().flatMap(fn));
    }

    public default NecoList<A> filter(Predicate<? super A> predicate) {
        return isEmpty()
                ? empty()
                : predicate.test(head()) ? cons(head(), () -> tail().filter(predicate)) : tail().filter(predicate);
    }

    public default boolean all(Predicate<? super A> predicate) {
        for (A a : this) {
            if (!predicate.test(a)) {
                return false;
            }
        }
        return true;
    }

    public default boolean any(Predicate<? super A> predicate) {
        for (A a : this) {
            if (predicate.test(a)) {
                return true;
            }
        }
        return false;
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

    public default void force() {
        for(A a : this);
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
