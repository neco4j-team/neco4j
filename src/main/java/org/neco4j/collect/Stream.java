package org.neco4j.collect;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Stream<V> implements InfiniteWithUnitKey<V, Stream<V>>, AlwaysAddableWithUnitKey<V, Stream<V>> {

    private final V _value;
    private final Supplier<Stream<V>> _next;

    private Stream(V value, Supplier<Stream<V>> next) {
        _value = value;
        _next = next;
    }

    @Override
    public Stream<V> add(V v) {
        return new Stream<>(v, () -> this);
    }

    @Override
    public V get() {
        return _value;
    }

    @Override
    public Stream<V> remove() {
        return _next.get();
    }

    public static <V> Stream<V> constant(V v) {
        return new Stream<>(v, () -> constant(v));
    }

    public static <V> Stream<V> iterate(V v, UnaryOperator<V> operator) {
        return new Stream<>(v, () -> iterate(operator.apply(v), operator));
    }
}
