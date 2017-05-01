package org.neco4j.either;

import org.neco4j.collect.unitkey.Opt;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.*;

public abstract class Either<A, B> {

    private Either() {
    }

    public static <A, B> Left<A, B> leftOf(A a) {
        return new Left<>(a);
    }

    public static <A, B> Right<A, B> rightOf(B b) {
        return new Right<>(b);
    }

    public boolean isLeft() {
        return either(a -> true, b -> false);
    }

    public boolean isRight() {
        return either(a -> false, b -> true);
    }

    public A leftOrFail() throws NoSuchElementException {
        return either(a -> a, b -> { throw new NoSuchElementException("Called leftOrFail() on Right"); });
    }

    public B rightOrFail() throws NoSuchElementException {
        return either(a -> { throw new NoSuchElementException("Called rightOrFail() on Left"); }, b -> b);
    }

    public A leftOrElse(A defaultValue) {
        return either(a -> a, b -> defaultValue);
    }

    public B rightOrElse(B defaultValue) {
        return either(a -> defaultValue, b -> b);
    }

    public A leftOrElseGet(Supplier<A> defaultSupplier) {
        return either(a -> a, b -> defaultSupplier.get());
    }

    public B rightOrElseGet(Supplier<B> defaultSupplier) {
        return either(a -> defaultSupplier.get(), b -> b);
    }

    public Opt<A> leftOpt() {
        return either(Opt::some, b -> Opt.none());
    }

    public Opt<B> rightOpt() {
        return either(a -> Opt.none(), Opt::some);
    }

    public <A1> Either<A1, B> mapLeft(Function<? super A, ? extends A1> fn) {
        return either(a -> leftOf(fn.apply(a)), Either::rightOf);
    }

    public <B1> Either<A, B1> mapRight(Function<? super B, ? extends B1> fn) {
        return either(Either::leftOf, b -> rightOf(fn.apply(b)));
    }

    public <A1, B1> Either<A1, B1> bimap(Function<? super A, ? extends A1> fnA, Function<? super B, ? extends B1> fnB) {
        return either(a -> leftOf(fnA.apply(a)), b -> rightOf(fnB.apply(b)));
    }

    public abstract <C> C either(Function<? super A, ? extends C> fnA, Function<? super B, ? extends C> fnB);

    public Either<B, A> swap() {
        return either(Either::rightOf, Either::leftOf);
    }

    @Override
    public String toString() {
        return either(a -> String.format("Left(%s)", a), b -> String.format("Right(%s)", b));
    }

    @Override
    public int hashCode() {
        return either(a -> 29 * a.hashCode(), b -> 31 * b.hashCode());
    }

    public final static class Left<A, B> extends Either<A, B> {

        private final A _aValue;

        private Left(A a) {
            this._aValue = requireNonNull(a);
        }

        @Override
        public <C> C either(Function<? super A, ? extends C> fnA, Function<? super B, ? extends C> fnB) {
            return fnA.apply(_aValue);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof Left) {
                Left<?, ?> that = (Left) obj;
                return this._aValue.equals(that._aValue);
            }
            return false;
        }
    }

    public final static class Right<A, B> extends Either<A, B> {

        private final B _bValue;

        private Right(B b) {
            this._bValue = requireNonNull(b);
        }

        @Override
        public <C> C either(Function<? super A, ? extends C> fnA, Function<? super B, ? extends C> fnB) {
            return fnB.apply(_bValue);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof Right) {
                Right<?, ?> that = (Right) obj;
                return this._bValue.equals(that._bValue);
            }
            return false;
        }
    }
}
