package org.neco4j.collect;

import org.neco4j.tuple.Pair;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Stream<A> implements Iterable<A> {

    private Memo<A> head;
    private Memo<Stream<A>> tail;

    public Stream(A head, Stream<A> tail) {
        this.head = new Evaluated<>(head);
        this.tail = new Evaluated<>(tail);
    }

    public Stream(A head, Supplier<Stream<A>> tailSupplier) {
        this.head = new Evaluated<>(head);
        this.tail = new UnEvaluated<>(tailSupplier);
    }

    public Stream(Supplier<A> headSupplier, Stream<A> tail) {
        this.head = new UnEvaluated<>(headSupplier);
        this.tail = new Evaluated<>(tail);
    }

    public Stream(Supplier<A> headSupplier, Supplier<Stream<A>> tailSupplier) {
        this.head = new UnEvaluated<>(headSupplier);
        this.tail = new UnEvaluated<>(tailSupplier);
    }

    public A head() {
        Evaluated<A> evaluated = head.evaluate();
        head = evaluated;
        return evaluated.get();
    }

    public Stream<A> tail() {
        Evaluated<Stream<A>> evaluated = tail.evaluate();
        tail = evaluated;
        return evaluated.get();
    }

    @Override
    public Iterator<A> iterator() {
        return new Iterator<A>() {

            private Stream<A> stream = Stream.this;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public A next() {
                A result = stream.head();
                stream = stream.tail();
                return result;
            }
        };
    }

    public <B> Stream<B> map(Function<? super A, ? extends B> fn) {
        return new Stream<B>(() -> fn.apply(head()), () -> tail().map(fn));
    }

    public Stream<A> dropWhile(Predicate<? super A> predicate) {
        return dropUntil(predicate.negate());
    }

    public Stream<A> dropUntil(Predicate<? super A> predicate) {
        for(Stream<A> stream = this; ; stream = stream.tail()) {
            if (predicate.test(stream.head())) {
                return stream;
            }
        }
    }

    public Stream<A> filter(Predicate<? super A> predicate) {
        Stream<A> stream = this.dropUntil(predicate);
        return new Stream<>(stream.head(), () -> stream.tail().filter(predicate));
    }

    private static interface Memo<T> {
        Evaluated<T> evaluate();
    }

    private static class Evaluated<T> implements Memo<T> {
        private final T t;

        private Evaluated(T t) {
            this.t = t;
        }

        @Override
        public Evaluated<T> evaluate() {
            return this;
        }

        public T get() {
            return t;
        }
    }

    private static class UnEvaluated<T> implements Memo<T> {

        private final Supplier<T> supplier;

        private UnEvaluated(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public Evaluated<T> evaluate() {
            return new Evaluated<>(supplier.get());
        }
    }

    public static <S,T> Stream<T> unfold(S seed, Function<S,Pair<S,T>> fn) {
        Pair<S,T> pair = fn.apply(seed);
        return new Stream<T>(pair.get2(), () -> unfold(pair.get1(), fn));
    }

    public static void main(String[] args) {
        Stream<Integer> stream = unfold(new int[]{0,1}, p -> Pair.of(new int[]{p[1], p[0]+p[1]}, p[0]));
        for(int i : stream) {
            System.out.println(i);
            if (i > 10000) break;
        }
    }
}
