package org.neco4j.matcher.extractors;

import org.neco4j.matcher.Extractor;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class OptionalExtractors {
    private OptionalExtractors(){ throw new UnsupportedOperationException(); }

    public static <A,R> Extractor<Optional<A>,R> isNone(Supplier<? extends R> supplier) {
        return Extractor.create(optA -> ! optA.isPresent(), supplier);
    }

    public static <A,R> Extractor<Optional<A>,R> isSome(A value, Supplier<? extends R> supplier) {
        return optA -> optA.filter(a -> a.equals(value)).map(a -> supplier.get());
    }

    public static <A,R> Extractor<Optional<A>,R> isSome(Extractor<A,R> ExtractorA) {
        return optA -> optA.flatMap(ExtractorA::unapply);
    }

    public static <A,R> Extractor<Optional<A>,R> isSome(Predicate<A> condition, Function<? super A, ? extends R> fn) {
        return Extractor.create(optA -> optA.isPresent() && condition.test(optA.get()), optA -> fn.apply(optA.get()));
    }

}