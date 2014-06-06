package org.neco4j.either;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class Right<A, B> extends Either<A, B> {

    private final B b;

    Right(B b) {
        this.b = requireNonNull(b);
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
    public A left() throws NoSuchElementException {
        throw new NoSuchElementException("left() call on Right");
    }

    @Override
    public B right() {
        return b;
    }

    @Override
    public A leftOrElse(A defaultValue) {
        return defaultValue;
    }

    @Override
    public B rightOrElse(B defaultValue) {
        return right();
    }

    @Override
    public A leftOrElseGet(Supplier<A> defaultSupplier) {
        return defaultSupplier.get();
    }

    @Override
    public B rightOrElseGet(Supplier<B> defaultSupplier) {
        return right();
    }

    @Override
    public <X extends Exception> A leftOrElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        throw exceptionSupplier.get();
    }

    @Override
    public <X extends Exception> B rightOrElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return right();
    }

    @Override
    public Optional<A> leftOpt() {
        return Optional.empty();
    }

    @Override
    public Optional<B> rightOpt() {
        return Optional.of(right());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A1> Either<A1, B> mapLeft(Function<? super A, ? extends A1> fn) {
        return (Either<A1, B>) this;
    }

    @Override
    public <B1> Either<A, B1> mapRight(Function<? super B, ? extends B1> fn) {
        return rightOf(fn.apply(right()));
    }

    @Override
    public <A1, B1> Either<A1, B1> bimap(Function<? super A, ? extends A1> fnA, Function<? super B, ? extends B1> fnB) {
        return rightOf(fnB.apply(right()));
    }

    @Override
    public <C> C fold(Function<? super A, ? extends C> fnA, Function<? super B, ? extends C> fnB) {
        return fnB.apply(right());
    }

    @Override
    public boolean test(Predicate<A> predicateA, Predicate<B> predicateB) {
        return predicateB.test(b);
    }

    @Override
    public Either<B, A> swap() {
        return leftOf(right());
    }

    @Override
    public String toString() {
        return String.format("Right(%s)", right());
    }

    @Override
    public int hashCode() {
        return 21 * right().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Right) {
            Right<?, ?> that = (Right) obj;
            return this.right().equals(that.right());
        }
        return false;
    }
}
