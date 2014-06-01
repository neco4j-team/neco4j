package org.neco4j.either;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

public abstract class Left<A, B> extends Either<A, B> {

    Left() {
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public B getRight() throws NoSuchElementException {
        throw new NoSuchElementException("getRight() call on Left");
    }

    @Override
    public A getLeftOrElse(A defaultValue) {
        return getLeft();
    }

    @Override
    public B getRightOrElse(B defaultValue) {
        return defaultValue;
    }

    @Override
    public Optional<A> getLeftOpt() {
        return Optional.of(getLeft());
    }

    @Override
    public Optional<B> getRightOpt() {
        return Optional.empty();
    }

    @Override
    public <A1> Either<A1, B> mapLeft(Function<? super A, ? extends A1> fn) {
        return left(fn.apply(getLeft()));
    }

    @Override
    public <B1> Either<A, B1> mapRight(Function<? super B, ? extends B1> fn) {
        return left(getLeft());
    }

    @Override
    public <A1, B1> Either<A1, B1> bimap(Function<? super A, ? extends A1> fnA, Function<? super B, ? extends B1> fnB) {
        return left(fnA.apply(getLeft()));
    }

    @Override
    public <C> C either(Function<? super A, ? extends C> fnA, Function<? super B, ? extends C> fnB) {
        return fnA.apply(getLeft());
    }

    @Override
    public Either<B, A> swap() {
        return right(getLeft());
    }

    @Override
    public String toString() {
        return String.format("Left(%s)", getLeft());
    }

    @Override
    public int hashCode() {
        return 29 * getLeft().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Left) {
            Object value = ((Left) obj).getLeft();
            return getLeft().equals(value);
        }
        return false;
    }
}
