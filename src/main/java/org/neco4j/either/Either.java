package org.neco4j.either;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Either<A, B> {

    Either() {
    }

    public static <A, B> Left<A, B> left(A a) {
        Objects.requireNonNull(a);
        return new Left<A, B>() {
            @Override
            public A getLeft() throws NoSuchElementException {
                return a;
            }
        };
    }

    public static <A, B> Left<A, B> lazyLeft(Supplier<? extends A> supA) {
        Objects.requireNonNull(supA);
        return new Left<A, B>() {
            @Override
            public A getLeft() throws NoSuchElementException {
                return supA.get();
            }
        };
    }

    public static <A, B> Right<A, B> right(B b) {
        Objects.requireNonNull(b);
        return new Right<A, B>() {
            @Override
            public B getRight() throws NoSuchElementException {
                return b;
            }
        };
    }

    public static <A, B> Right<A, B> lazyRight(Supplier<? extends B> supB) {
        Objects.requireNonNull(supB);
        return new Right<A, B>() {
            @Override
            public B getRight() throws NoSuchElementException {
                return supB.get();
            }
        };
    }

    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract A getLeft() throws NoSuchElementException;

    public abstract B getRight() throws NoSuchElementException;

    public abstract A leftOrElse(A defaultValue);

    public abstract B rightOrElse(B defaultValue);

    public abstract A leftOrElseGet(Supplier<A> defaultSupplier);

    public abstract B rightOrElseGet(Supplier<B> defaultSupplier);

    public abstract Optional<A> getLeftOpt();

    public abstract Optional<B> getRightOpt();

    public abstract <A1> Either<A1, B> mapLeft(Function<? super A, ? extends A1> fn);

    public abstract <B1> Either<A, B1> mapRight(Function<? super B, ? extends B1> fn);

    public abstract <A1, B1> Either<A1, B1> bimap(Function<? super A, ? extends A1> fnA, Function<? super B, ? extends B1> fnB);

    public abstract <C> C either(Function<? super A, ? extends C> fnA, Function<? super B, ? extends C> fnB);

    public abstract Either<B, A> swap();
}
