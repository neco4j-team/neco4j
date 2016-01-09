package org.neco4j.matcher;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import static java.util.Optional.of;

public interface BiExtractor<A, B, R> {

    Optional<R> unapply(A a, B b);

    static <A, B, R> BiExtractor<A, B, R> create(BiPredicate<A, B> isDefined, BiFunction<A, B, ? extends R> fn) {
        return (a, b) -> isDefined.test(a, b) ? Optional.of(fn.apply(a, b)) : Optional.<R>empty();
    }

    static <A, B, R> BiExtractor<A, B, R> create(BiPredicate<A, B> isDefined, Supplier<? extends R> supplier) {
        return (a, b) -> isDefined.test(a, b) ? Optional.of(supplier.get()) : Optional.<R>empty();
    }

    static <A, B, R> BiExtractor<A, B, R> create(BiPredicate<A, B> isDefined, R result) {
        return (a, b) -> isDefined.test(a, b) ? Optional.of(result) : Optional.<R>empty();
    }

    static <A, B, R> BiExtractor<A, B, R> useDefault(Supplier<? extends R> supplier) {
        return (a, b) -> of(supplier.get());
    }

    static <A, B, R> BiExtractor<A, B, R> useDefault(R result) {
        return (a, b) -> of(result);
    }

}
