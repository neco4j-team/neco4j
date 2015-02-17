package org.neco4j.monad;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.neco4j.function.Functions;
import org.neco4j.function.QuadFunction;
import org.neco4j.function.TriFunction;

public enum OptionalMonad {

    INSTANCE;

    public <A> Optional<A> zero() {
        return Optional.empty();
    }

    public <A> Optional<A> pure(A a) {
        return Optional.of(a);
    }

    public <A, B> Optional<B> map(Function<? super A, ? extends B> fn, Optional<A> optional) {
        return optional.map(fn);
    }

    public <A, B> Optional<B> apply(Optional<Function<A, B>> fn, Optional<A> optional) {
        return optional.flatMap(a -> fn.map(f -> f.apply(a)));
    }

    public <A, B> Optional<B> flatMap(Function<? super A, Optional<B>> fn, Optional<A> optional) {
        return optional.flatMap(fn);
    }

    public <A, B> Function<Optional<A>, Optional<B>> lift(Function<A, B> fn) {
        return a -> a.map(fn);
    }

    public <A, B, C> BiFunction<Optional<A>, Optional<B>, Optional<C>> lift(BiFunction<A, B, C> fn) {
        return (a, b) -> apply(map(Functions.curry(fn), a), b);
    }

    public <A, B, C, D> TriFunction<Optional<A>, Optional<B>, Optional<C>, Optional<D>> lift(TriFunction<A, B, C, D> fn) {
        BiFunction<Optional<A>, Optional<B>, Optional<Function<C, D>>> fn2 = lift((a, b) -> c -> fn.apply(a, b, c));
        return (a, b, c) -> apply(fn2.apply(a, b), c);
    }

    public <A, B, C, D, E> QuadFunction<Optional<A>, Optional<B>, Optional<C>, Optional<D>, Optional<E>> lift(QuadFunction<A, B, C, D, E> fn) {
        TriFunction<Optional<A>, Optional<B>, Optional<C>, Optional<Function<D, E>>> fn3 = lift((a, b, c) -> d -> fn.apply(a, b, c, d));
        return (a, b, c, d) -> apply(fn3.apply(a, b, c), d);
    }
}
