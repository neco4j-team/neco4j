package org.neco4j.either;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

public abstract class Right<A, B> extends Either<A, B> {

    Right() {
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public A getLeft() throws NoSuchElementException {
        throw new NoSuchElementException("getLeft() call on Right");
    }

    @Override
    public A getLeftOrElse(A defaultValue) {
        return defaultValue;
    }

    @Override
    public B getRightOrElse(B defaultValue) {
        return getRight();
    }

    @Override
    public Optional<A> getLeftOpt() {
        return Optional.empty();
    }

    @Override
    public Optional<B> getRightOpt() {
        return Optional.of(getRight());
    }

    @Override
    public <A1> Either<A1, B> mapLeft(Function<? super A, ? extends A1> fn) {
        return right(getRight());
    }

    @Override
    public <B1> Either<A, B1> mapRight(Function<? super B, ? extends B1> fn) {
        return right(fn.apply(getRight()));
    }

    @Override
    public <A1, B1> Either<A1, B1> bimap(Function<? super A, ? extends A1> fnA, Function<? super B, ? extends B1> fnB) {
        return right(fnB.apply(getRight()));
    }

    @Override
    public <C> C either(Function<? super A, ? extends C> fnA, Function<? super B, ? extends C> fnB) {
        return fnB.apply(getRight());
    }

    @Override
    public Either<B, A> swap() {
        return left(getRight());
    }

    @Override
    public String toString() {
        return String.format("Right(%s)", getRight());
    }

    @Override
    public int hashCode() {
        return 21 * getRight().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Right) {
            Object value = ((Right) obj).getRight();
            return getRight().equals(value);
        }
        return false;
    }

}
