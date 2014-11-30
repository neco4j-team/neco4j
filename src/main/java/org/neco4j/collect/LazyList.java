package org.neco4j.collect;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LazyList<A> implements Iterable<A> {

    private Supplier<A> head;
    private Supplier<LazyList<A>> tail;

    private final static LazyList<?> NIL = new LazyList<Object>(null, null) {
        @Override
        public boolean isEmpty() {
            return true;
        }
    };

    private LazyList(Supplier<A> head, Supplier<LazyList<A>> tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <A> LazyList<A> of(A head, LazyList<A> tail) {
        return new LazyList<A>(new Evaluated<A>(head), new Evaluated<LazyList<A>>(tail));
    }

    public static <A> LazyList<A> of(A head, Supplier<LazyList<A>> tailSupplier) {
        return new LazyList<A>(new Evaluated<A>(head), tailSupplier);
    }

    public static <A> LazyList<A> of(Supplier<A> headSupplier, LazyList<A> tail) {
        return new LazyList<A>(headSupplier, new Evaluated<>(tail));
    }

    public static <A> LazyList<A> of(Supplier<A> headSupplier, Supplier<LazyList<A>> tailSupplier) {
        return new LazyList<>(headSupplier, tailSupplier);
    }

    @SafeVarargs
    public static <A> LazyList<A> of(A... elements) {
        LazyList<A> result = empty();
        for (int i = elements.length - 1; i >= 0; i--) {
            result = of(elements[i], result);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <A> LazyList<A> empty() {
        return (LazyList<A>) NIL;
    }

    public A head() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("head() call on NIL");
        }
        A result = head.get();
        if (!(head instanceof Evaluated)) {
            head = new Evaluated<>(result);
        }
        return result;
    }

    public LazyList<A> tail() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("tail() call on NIL");
        }
        LazyList<A> result = tail.get();
        if (!(tail instanceof Evaluated)) {
            tail = new Evaluated<>(result);
        }
        return result;
    }

    public A last() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("last() call on NIL");
        }
        LazyList<A> resultList = this;
        while (!resultList.tail().isEmpty()) {
            resultList = resultList.tail();
        }
        return resultList.head();
    }

    public LazyList<A> init() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("init() call on NIL");
        }
        if (tail().isEmpty()) {
            return empty();
        }
        return of(head, () -> tail().init());
    }

    public Optional<A> getOpt(long index) {
        LazyList<A> current = this;
        long i = index;
        while (i-- > 0) {
            if (current.isEmpty()) {
                return Optional.empty();
            }
            current = current.tail();
        }
        return Optional.of(current.head());
    }

    public A get(long index) throws IndexOutOfBoundsException {
        LazyList<A> current = this;
        long i = index;
        while (i-- > 0) {
            if (current.isEmpty()) {
                throw new IndexOutOfBoundsException();
            }
            current = current.tail();
        }
        return current.head();
    }

    public boolean isEmpty() {
        return false;
    }

    public int size() {
        int length = 0;
        for (LazyList<A> current = this; !current.isEmpty(); current = current.tail()) {
            length++;
        }
        return length;
    }

    //all prefixes of the current list, ordered from empty to full list
    public LazyList<LazyList<A>> inits() {
        return LazyList.of(empty(), () -> isEmpty() ? empty()
                : tail().inits().map(list -> LazyList.<A>of(this::head, list)));
    }

    //all suffixes of the current stream ordered from full list to empty
    public LazyList<LazyList<A>> tails() {
        return LazyList.of(this, () -> isEmpty() ? LazyList.empty() : tail().tails());
    }

    public <B> LazyList<B> map(Function<? super A, ? extends B> fn) {
        return isEmpty() ? empty() : LazyList.<B>of(() -> fn.apply(head()), () -> tail().map(fn));
    }

    public <B> LazyList<B> flatMap(Function<A, LazyList<B>> fn) {
        return flatten(map(fn));
    }

    public static <A> LazyList<A> flatten(LazyList<LazyList<A>> nested) {
        if (nested.isEmpty()) {
            return empty();
        }
        if (nested.head().isEmpty()) {
            return flatten(nested.tail());
        }
        return LazyList.<A>of(nested.head().head, () -> LazyList.flatten(LazyList.<LazyList<A>>of(nested.head().tail, nested.tail)));
    }

    public LazyList<A> reverse() {
        LazyList<A> result = empty();
        LazyList<A> current = this;
        while (!current.isEmpty()) {
            result = of(current.head, result);
            current = current.tail();
        }
        return result;
    }

    public LazyList<A> take(int n) {
        LazyList<A> result = LazyList.empty();
        for (A a : this) {
            if (n-- <= 0) {
                break;
            }
            result = of(a, result);
        }
        return result.reverse();
    }

    public LazyList<A> takeWhile(Predicate<A> predicate) {
        return predicate.test(head()) ? of(head(), () -> tail().takeWhile(predicate)) : empty();
    }

    public LazyList<A> drop(int n) {
        LazyList<A> result = this;
        while (n-- > 0 && !this.isEmpty()) {
            result = result.tail();
        }
        return result;
    }

    public LazyList<A> dropWhile(Predicate<? super A> predicate) {
        for (LazyList<A> list = this; !list.isEmpty(); list = list.tail()) {
            if (!predicate.test(list.head())) {
                return list;
            }
        }
        return empty();
    }

    public LazyList<A> filter(Predicate<? super A> predicate) {
        LazyList<A> list = this.dropWhile(predicate.negate());
        return list.isEmpty()
                ? empty()
                : LazyList.<A>of(list.head(), () -> list.tail().filter(predicate));
    }

    public <B> B foldLeft(B seed, BiFunction<? super B, ? super A, ? extends B> fn) {
        B result = seed;
        for (A a : this) {
            result = fn.apply(result, a);
        }
        return result;
    }

    public A foldLeft1(BiFunction<? super A, ? super A, ? extends A> fn) {
        if (isEmpty()) {
            throw new NoSuchElementException("foldLeft1() call on NIL");
        }
        A result = head();
        for (A a : this.tail()) {
            result = fn.apply(result, a);
        }
        return result;
    }

    public <B> LazyList<B> scanLeft(B seed, BiFunction<? super B, ? super A, ? extends B> fn) {
        return isEmpty()
                ? empty()
                : LazyList.<B>of(seed, () -> tail().scanLeft(fn.apply(seed, head()), fn));
    }

    public LazyList<A> scanLeft1(BiFunction<? super A, ? super A, ? extends A> fn) {
        return isEmpty()
                ? empty()
                : tail().scanLeft(head(), fn);
    }

    @Override
    public Iterator<A> iterator() {
        return new Iterator<A>() {

            private LazyList<A> list = LazyList.this;

            @Override
            public boolean hasNext() {
                return !list.isEmpty();
            }

            @Override
            public A next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                A result = list.head();
                list = list.tail();
                return result;
            }
        };
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(head().toString());
        for (A a : this.tail()) {
            sb.append(",").append(a);
        }
        return sb.append("]").toString();
    }

    private static class Evaluated<A> implements Supplier<A> {

        private final A a;

        private Evaluated(A a) {
            this.a = a;
        }

        @Override
        public A get() {
            return a;
        }
    }


}
