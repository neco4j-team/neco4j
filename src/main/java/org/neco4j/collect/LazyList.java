package org.neco4j.collect;

import java.util.Iterator;
import java.util.NoSuchElementException;
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

    public A head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head() call on NIL");
        }
        A result = head.get();
        if (!(head instanceof Evaluated)) {
            head = new Evaluated<>(result);
        }
        return result;
    }

    public LazyList<A> tail() {
        if (isEmpty()) {
            throw new NoSuchElementException("tail() call on NIL");
        }
        LazyList<A> result = tail.get();
        if (!(tail instanceof Evaluated)) {
            tail = new Evaluated<>(result);
        }
        return result;
    }

    public boolean isEmpty() {
        return false;
    }

    public int size() {
        int length = 0;
        for (A a : this) {
            length++;
        }
        return length;
    }

    //all prefixes of the current stream
    public LazyList<LazyList<A>> inits() {
        return isEmpty()
                ? empty()
                : LazyList.<LazyList<A>>of(LazyList.<A>empty(), () -> tail().inits().map(list -> LazyList.<A>of(head(), list)));
    }

    //all suffixes of the current stream
    public LazyList<LazyList<A>> tails() {
        return isEmpty()
                ? empty()
                : LazyList.<LazyList<A>>of(this, () -> tail().tails());
    }

    public <B> LazyList<B> map(Function<? super A, ? extends B> fn) {
        return isEmpty() ? empty() : LazyList.<B>of(() -> fn.apply(head()), () -> tail().map(fn));
    }

    //takes the "main diagonal" when the generated stream of streams is written as 2D table
    public <B> LazyList<B> flatMap(Function<A, LazyList<B>> fn) {
        return flatten(map(fn));
    }

    //takes the "main diagonal" when the stream of stream is written as 2D table
    public static <A> LazyList<A> flatten(LazyList<LazyList<A>> nested) {
        return nested.isEmpty()
                ? empty()
                : LazyList.<A>of(() -> nested.head().head(), () -> flatten(nested.tail().map(a -> a.tail())));
    }

    public LazyList<A> dropWhile(Predicate<? super A> predicate) {
        return dropUntil(predicate.negate());
    }

    public LazyList<A> dropUntil(Predicate<? super A> predicate) {
        for (LazyList<A> list = this; !list.isEmpty(); list = list.tail()) {
            if (predicate.test(list.head())) {
                return list;
            }
        }
        return empty();
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
            result = LazyList.of(a, result);
        }
        return result.reverse();
    }

    public LazyList<A> drop(int n) {
        LazyList<A> result = this;
        while (n-- > 0 && !this.isEmpty()) {
            result = result.tail();
        }
        return result;
    }

    public LazyList<A> filter(Predicate<? super A> predicate) {
        LazyList<A> list = this.dropUntil(predicate);
        return list.isEmpty()
                ? empty()
                : LazyList.<A>of(list.head(), () -> list.tail().filter(predicate));
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
                return ! list.isEmpty();
            }

            @Override
            public A next() {
                if (! hasNext()) {
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
        StringBuilder sb = new StringBuilder();
        for (A a : this) {
            sb.append(sb.length() == 0 ? "[" : ",").append(a);
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
