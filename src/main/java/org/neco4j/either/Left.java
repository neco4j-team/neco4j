package org.neco4j.either;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class Left<A, B> extends Either<A, B> {

    private final A a;

    Left(A a) {
        this.a = requireNonNull(a);
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
    public A left() {
        return a;
    }

    @Override
    public B right() throws NoSuchElementException {
        throw new NoSuchElementException("right() call on Left");
    }

    @Override
    public A leftOrElse(A defaultValue) {
        return left();
    }

    @Override
    public B rightOrElse(B defaultValue) {
        return defaultValue;
    }

    @Override
    public A leftOrElseGet(Supplier<A> defaultSupplier) {
        return left();
    }

    @Override
    public B rightOrElseGet(Supplier<B> defaultSupplier) {
        return defaultSupplier.get();
    }

    @Override
    public Optional<A> leftOpt() {
        return Optional.of(left());
    }

    @Override
    public Optional<B> rightOpt() {
        return Optional.empty();
    }

    @Override
    public <A1> Either<A1, B> mapLeft(Function<? super A, ? extends A1> fn) {
        return leftOf(fn.apply(left()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B1> Either<A, B1> mapRight(Function<? super B, ? extends B1> fn) {
        return (Either<A, B1>) this;
    }

    @Override
    public <A1, B1> Either<A1, B1> bimap(Function<? super A, ? extends A1> fnA, Function<? super B, ? extends B1> fnB) {
        return leftOf(fnA.apply(left()));
    }

    @Override
    public <C> C fold(Function<? super A, ? extends C> fnA, Function<? super B, ? extends C> fnB) {
        return fnA.apply(left());
    }

    @Override
    public boolean test(Predicate<A> predicateA, Predicate<B> predicateB) {
        return predicateA.test(a);
    }

    @Override
    public Either<B, A> swap() {
        return rightOf(left());
    }

    @Override
    public String toString() {
        return String.format("Left(%s)", left());
    }

    @Override
    public int hashCode() {
        return 29 * left().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Left) {
            Left<?, ?> that = (Left) obj;
            return this.left().equals(that.left());
        }
        return false;
    }
}
