package org.neco4j.monad;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import org.neco4j.either.Either;
import org.neco4j.function.Functions;
import org.neco4j.function.QuadFunction;
import org.neco4j.function.TriFunction;
import org.neco4j.monoid.Monoid;

public class EitherMonad<S> {

    private final BinaryOperator<S> operator;

    public EitherMonad(BinaryOperator<S> operator) {
        this.operator = operator;
    }

    public <A> Either<S, A> zero() {
        if (operator instanceof Monoid) {
            return Either.leftOf(((Monoid<S>) operator).identity());
        }
        throw new UnsupportedOperationException();
    }

    public <A> Either<S, A> pure(A a) {
        return Either.rightOf(a);
    }

    public <A, B> Either<S, B> map(Function<? super A, ? extends B> fn, Either<S, A> either) {
        return either.mapRight(fn);
    }

    public <A, B> Either<S, B> apply(Either<S, Function<A, B>> fn, Either<S, A> either) {
        if (fn.isLeft()) {
            return Either.leftOf(either.isLeft() ? operator.apply(fn.left(), either.left()) : fn.left());
        } else {
            return either.mapRight(fn.right()::apply);
        }
    }

    public <A, B> Either<S, B> flatMap(Function<? super A, Either<S, B>> fn, Either<S, A> either) {
        return either.fold(Either::<S, B>leftOf, fn::apply);
    }

    public <A, B> Function<Either<S, A>, Either<S, B>> lift(Function<A, B> fn) {
        return a -> a.mapRight(fn);
    }

    public <A, B, C> BiFunction<Either<S, A>, Either<S, B>, Either<S, C>> lift(BiFunction<A, B, C> fn) {
        return (a, b) -> apply(map(Functions.curry(fn), a), b);
    }

    public <A, B, C, D> TriFunction<Either<S, A>, Either<S, B>, Either<S, C>, Either<S, D>> lift(TriFunction<A, B, C, D> fn) {
        BiFunction<Either<S, A>, Either<S, B>, Either<S, Function<C, D>>> fn2 = lift((a, b) -> c -> fn.apply(a, b, c));
        return (a, b, c) -> apply(fn2.apply(a, b), c);
    }

    public <A, B, C, D, E> QuadFunction<Either<S, A>, Either<S, B>, Either<S, C>, Either<S, D>, Either<S, E>> lift(QuadFunction<A, B, C, D, E> fn) {
        TriFunction<Either<S, A>, Either<S, B>, Either<S, C>, Either<S, Function<D, E>>> fn3 = lift((a, b, c) -> d -> fn.apply(a, b, c, d));
        return (a, b, c, d) -> apply(fn3.apply(a, b, c), d);
    }
}
