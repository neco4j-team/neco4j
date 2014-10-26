package org.neco4j.collect;

import org.neco4j.tuple.Pair;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class Streams {
    private Streams() {
    }

    public static <S, T> Stream<T> unfold(S seed, Function<? super S, Pair<S, T>> fn) {
        Pair<S, T> pair = fn.apply(seed);
        return Stream.<T>of(pair.get2(), () -> unfold(pair.get1(), fn));
    }

    public static <A> Stream<A> iterate(A a, Function<? super A, ? extends A> fn) {
        return Stream.<A>of(a, () -> Streams.<A>iterate(fn.apply(a), fn));
    }

    public static <A> Stream<A> repeat(A a) {
        Stream<?>[] ref = new Stream<?>[1];
        Stream<A> result = Stream.<A>of(a, () -> (Stream<A>) ref[0]);
        ref[0] = result;
        return result;
    }

    @SafeVarargs
    public static <A> Stream<A> cycle(A... as) {
        switch (as.length) {
            case 0:
                throw new IllegalArgumentException();
            case 1:
                return repeat(as[0]);
            default:
                Stream<?>[] ref = new Stream<?>[1];
                Stream<A> result = Stream.<A>of(as[as.length - 1], () -> (Stream<A>) ref[0]);
                for (int i = as.length - 2; i >= 0; i--) {
                    result = Stream.of(as[i], result);
                }
                ref[0] = result;
                return result;
        }
    }

    public static <A> Stream<A> intersperse(Stream<A> stream, A a) {
        return Stream.<A>of(stream::head, () -> Stream.<A>of(a, intersperse(stream.tail(), a)));
    }

    public static <A> Stream<A> interleave(Stream<A> first, Stream<A> second) {
        return Stream.<A>of(first::head, () -> interleave(second, first.tail()));
    }

    public static <A, B, C> Stream<C> zipWith(Stream<A> streamA, Stream<B> streamB, BiFunction<? super A, ? super B, ? extends C> fn) {
        return Stream.<C>of(() -> fn.apply(streamA.head(), streamB.head()),
                () -> zipWith(streamA.tail(), streamB.tail(), fn));
    }

    public static <A, B> Stream<Pair<A, B>> zip(Stream<A> streamA, Stream<B> streamB) {
        return zipWith(streamA, streamB, Pair::of);
    }

}
