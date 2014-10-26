package org.neco4j.either;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <code>Either</code> is a wrapper, which can hold either a value of one or another type (the "left" and "right" type).
 * E.g. <code>Either</code> is often used to return a result of calculation which may fail: If the calculation succeeds,
 * the "right" value is returned as result, else the "left" value contains some description of the problem.
 *
 * In this implementation, there are two subtypes <code>Left</code> and <code>Right</code> holding the actual value.
 * @param <A> the "left" value type
 * @param <B> the "right" value type.
 */
public abstract class Either<A, B> {

    Either() {
    }

    public static <A, B> Either<A, B> leftOf(A a) {
        return new Left<>(a);
    }

    public static <A, B> Either<A, B> rightOf(B b) {
        return new Right<>(b);
    }

    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract A left() throws NoSuchElementException;

    public abstract B right() throws NoSuchElementException;

    public abstract A leftOrElse(A defaultValue);

    public abstract B rightOrElse(B defaultValue);

    public abstract A leftOrElseGet(Supplier<A> defaultSupplier);

    public abstract B rightOrElseGet(Supplier<B> defaultSupplier);

    public abstract <X extends Exception> A leftOrElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    public abstract <X extends Exception> B rightOrElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    public abstract Optional<A> leftOpt();

    public abstract Optional<B> rightOpt();

    public abstract <A1> Either<A1, B> mapLeft(Function<? super A, ? extends A1> fn);

    public abstract <B1> Either<A, B1> mapRight(Function<? super B, ? extends B1> fn);

    public abstract <A1, B1> Either<A1, B1> bimap(Function<? super A, ? extends A1> fnA, Function<? super B, ? extends B1> fnB);

    public abstract <C> C fold(Function<? super A, ? extends C> fnA, Function<? super B, ? extends C> fnB);

    public abstract boolean test(Predicate<A> predicateA, Predicate<B> predicateB);

    public abstract Either<B, A> swap();
}
