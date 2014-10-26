package org.neco4j.collect.memo;

import java.util.function.Supplier;

public class Unevaluated<T> implements Memoized<T> {

    private final Supplier<T> supplier;

    public Unevaluated(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Evaluated<T> evaluate() {
        return new Evaluated<>(supplier.get());
    }
}
