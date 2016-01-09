package org.neco4j.matcher;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Optional.of;

public interface Extractor<A,R> {

    public Optional<R> unapply(A a);

    default <S> Extractor<A,S> andThen(Extractor<R, S> that) {
        return a -> this.unapply(a).flatMap(that::unapply);
    }

    public static <A,R> Extractor<A,R> create(Predicate<A> isDefined, Function<A, ? extends R> fn) {
        return a -> isDefined.test(a) ? Optional.of(fn.apply(a)) : Optional.<R>empty();
    }

    public static <A,R> Extractor<A,R> create(Predicate<A> isDefined, Supplier<? extends R> supplier) {
        return a -> isDefined.test(a) ? Optional.of(supplier.get()) : Optional.<R>empty();
    }

    public static <A,R> Extractor<A,R> create(Predicate<A> isDefined, R result) {
        return a -> isDefined.test(a) ? Optional.of(result) : Optional.<R>empty();
    }

    public static <A,R> Extractor<A,R> useDefault(Supplier<? extends  R> supplier) {
        return a -> of(supplier.get());
    }

    public static <A,R> Extractor<A,R> useDefault(R result) {
        return value -> of(result);
    }

}
