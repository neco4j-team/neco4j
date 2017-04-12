package org.neco4j.either;

import org.neco4j.collect.Opt;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Either<A, B> {

    Either() {
    }

    public static <A, B> Left<A, B> leftOf(A a) {
        return new Left<>(a);
    }

    public static <A, B> Right<A, B> rightOf(B b) {
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

    public abstract Opt<A> leftOpt();

    public abstract Opt<B> rightOpt();

    public abstract <A1> Either<A1, B> mapLeft(Function<? super A, ? extends A1> fn);

    public abstract <B1> Either<A, B1> mapRight(Function<? super B, ? extends B1> fn);

    public abstract <A1, B1> Either<A1, B1> bimap(Function<? super A, ? extends A1> fnA, Function<? super B, ? extends B1> fnB);

    public abstract <C> C either(Function<? super A, ? extends C> fnA, Function<? super B, ? extends C> fnB);

    public abstract Either<B, A> swap();
}
