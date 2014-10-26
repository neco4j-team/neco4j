package org.neco4j.collect;

import org.neco4j.collect.memo.Evaluated;
import org.neco4j.collect.memo.Memoized;
import org.neco4j.collect.memo.Unevaluated;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Stream<A> implements Iterable<A> {

    private Memoized<A> head;
    private Memoized<Stream<A>> tail;

    private Stream(Memoized<A> head, Memoized<Stream<A>> tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <A> Stream<A> of(A head, Stream<A> tail) {
        return new Stream<A>(new Evaluated<A>(head), new Evaluated<Stream<A>>(tail));
    }

    public static <A> Stream<A> of(A head, Supplier<Stream<A>> tailSupplier) {
        return new Stream<A>(new Evaluated<A>(head), new Unevaluated<Stream<A>>(tailSupplier));
    }

    public static <A> Stream<A> of(Supplier<A> headSupplier, Stream<A> tail) {
        return new Stream<A>(new Unevaluated<A>(headSupplier), new Evaluated<Stream<A>>(tail));
    }

    public static <A> Stream<A> of(Supplier<A> headSupplier, Supplier<Stream<A>> tailSupplier) {
        return new Stream<A>(new Unevaluated<A>(headSupplier), new Unevaluated<Stream<A>>(tailSupplier));
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

    //all prefixes of the current stream
    public Stream<NecoList<A>> inits() {
        return Stream.<NecoList<A>>of(NecoList.<A>empty(), () -> tail().inits().map(list -> NecoList.cons(head(), list)));
    }

    //all suffixes of the current stream
    public Stream<Stream<A>> tails() {
        return Stream.<Stream<A>>of(this, () -> tail().tails());
    }

    public <B> Stream<B> map(Function<? super A, ? extends B> fn) {
        return Stream.<B>of(() -> fn.apply(head()), () -> tail().map(fn));
    }

    public Stream<A> dropWhile(Predicate<? super A> predicate) {
        return dropUntil(predicate.negate());
    }

    public Stream<A> dropUntil(Predicate<? super A> predicate) {
        for (Stream<A> stream = this; ; stream = stream.tail()) {
            if (predicate.test(stream.head())) {
                return stream;
            }
        }
    }

    public NecoList<A> take(int n) {
        NecoList<A> result = NecoList.empty();
        for (A a : this) {
            if (n-- <= 0) {
                break;
            }
            result = NecoList.cons(a, result);
        }
        return result.reverse();
    }

    public Stream<A> drop(int n) {
        Stream<A> result = this;
        while (n-- > 0) {
            result = result.tail();
        }
        return result;
    }

    public Stream<A> filter(Predicate<? super A> predicate) {
        Stream<A> stream = this.dropUntil(predicate);
        return Stream.<A>of(stream.head(), () -> stream.tail().filter(predicate));
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

}
