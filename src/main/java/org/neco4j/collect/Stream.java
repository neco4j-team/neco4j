package org.neco4j.collect;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Stream<A> implements Sequence<A> {

    private Supplier<A> head;
    private Supplier<Stream<A>> tail;

    private Stream(Supplier<A> head, Supplier<Stream<A>> tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <A> Stream<A> cons(A head, Stream<A> tail) {
        return new Stream<A>(new Evaluated<A>(head), new Evaluated<Stream<A>>(tail));
    }

    public static <A> Stream<A> cons(A head, Supplier<Stream<A>> tailSupplier) {
        return new Stream<A>(new Evaluated<A>(head), tailSupplier);
    }

    public static <A> Stream<A> cons(Supplier<A> headSupplier, Stream<A> tail) {
        return new Stream<>(headSupplier, new Evaluated<>(tail));
    }

    public static <A> Stream<A> cons(Supplier<A> headSupplier, Supplier<Stream<A>> tailSupplier) {
        return new Stream<>(headSupplier, tailSupplier);
    }

    public Stream<A> plus(A a) {
        return cons(a, this);
    }

    @Override
    public A head() {
        A result = head.get();
        if (!(head instanceof Evaluated)) {
            head = new Evaluated<>(result);
        }
        return result;
    }

    @Override
    public Stream<A> tail() {
        Stream<A> result = tail.get();
        if (!(tail instanceof Evaluated)) {
            tail = new Evaluated<>(result);
        }
        return result;
    }

    @Override
    public Optional<A> headOpt() {
        return Optional.of(head());
    }

    @Override
    public Optional<Sequence<A>> tailOpt() {
        return Optional.of(tail());
    }


    public Optional<A> getOpt(long index) {
        return Optional.of(get(index));
    }

    public A get(long index) throws IndexOutOfBoundsException {
        Stream<A> current = this;
        for (long i = index; i > 0; i--) {
            current = current.tail();
        }
        return current.head();
    }

    //all prefixes of the current stream
    @Override
    public Stream<List<A>> inits() {
        return Stream.cons(LazyList.<A>empty(), () -> tail().inits().map(list -> LazyList.cons(head(), (LazyList) list)));
    }

    //all suffixes of the current stream
    @Override
    public Stream<Stream<A>> tails() {
        return Stream.<Stream<A>>cons(this, () -> tail().tails());
    }

    @Override
    public <B> Stream<B> map(Function<? super A, ? extends B> fn) {
        return Stream.<B>cons(() -> fn.apply(head()), () -> tail().map(fn));
    }

    //takes the "main diagonal" when the generated stream of streams is written as 2D table
    public <B> Stream<B> diagonal(Function<A, ? extends Sequence<B>> fn) {
        return flatten(map(fn));
    }

    //takes the "main diagonal" when the stream of stream is written as 2D table
    public static <A> Stream<A> flatten(Stream<Sequence<A>> nested) {
        return Stream.<A>cons(() -> nested.head().head(), () -> flatten(nested.tail().map(a -> a.tail())));
    }

    @Override
    public Stream<A> dropWhile(Predicate<? super A> predicate) {
        for (Stream<A> stream = this; ; stream = stream.tail()) {
            if (!predicate.test(stream.head())) {
                return stream;
            }
        }

    }

    @Override
    public List<A> take(int n) {
        return n <= 0 ? LazyList.empty() : LazyList.<A>cons(() -> head(), () -> (LazyList) (tail().take(n - 1)));
    }

    @Override
    public List<A> takeWhile(Predicate<A> predicate) {
        return predicate.test(head())
                ? LazyList.<A>cons(() -> head(), () -> (LazyList) (tail().takeWhile(predicate)))
                : LazyList.empty();
    }

    @Override
    public Stream<A> drop(int n) {
        Stream<A> result = this;
        while (n-- > 0) {
            result = result.tail();
        }
        return result;
    }

    @Override
    public Stream<A> filter(Predicate<? super A> predicate) {
        Stream<A> stream = this.dropWhile(predicate.negate());
        return Stream.<A>cons(stream.head(), () -> stream.tail().filter(predicate));
    }

    @Override
    public <B> Stream<B> scanLeft(B seed, BiFunction<? super B, ? super A, ? extends B> fn) {
        return Stream.<B>cons(seed, () -> tail().scanLeft(fn.apply(seed, head()), fn));
    }

    @Override
    public Stream<A> scanLeft1(BiFunction<? super A, ? super A, ? extends A> fn) {
        return tail().scanLeft(head(), fn);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Stream(");
        int i = 10;
        for (Stream<A> stream = this; i-- > 0; stream = stream.tail()) {
            sb.append(stream.head()).append(i > 0 ? "," : "...)");
        }
        return sb.toString();
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

    @Override
    public boolean isLazy() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}
