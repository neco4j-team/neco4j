package org.neco4j.function;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface QuadFunction<T, U, V, W, R> {

    R apply(T t, U u, V v, W w);

    default <X> QuadFunction<T, U, V, W, X> andThen(Function<? super R, ? extends X> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v, W w) -> after.apply(apply(t, u, v, w));
    }
}
