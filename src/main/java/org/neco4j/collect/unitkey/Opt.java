package org.neco4j.collect.unitkey;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A collection which may contain zero or one elements.
 *
 * @param <V> the element type
 */
public class Opt<V> implements UnitKeyPuttable<V, Opt<V>> {

    private final V _value;

    private Opt(V value) {
        _value = value;
    }

    private final static Opt<?> NONE = new Opt<Object>(null) {
        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public String toString() {
            return "None";
        }
    };

    @SuppressWarnings("unchecked")
    public static <V> Opt<V> none() {
        return (Opt<V>) NONE;
    }

    public static <V> Opt<V> some(V v) {
        return new Opt<>(Objects.requireNonNull(v));
    }

    public Opt<Opt<V>> addOpt(V v) {
        return isEmpty() ? some(some(v)) : none();
    }

    @Override
    public Opt<V> put(V v) {
        return some(v);
    }

    @Override
    public Opt<Opt<V>> removeOpt() {
        return isEmpty() ? none() : some(none());
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long size() {
        return isEmpty() ? 0L : 1L;
    }

    public V getOrFail() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return _value;
    }

    @Override
    public String toString() {
        return "Some(" + _value + ")";
    }

    @Override
    public Opt<V> getOpt() {
        return this;
    }

    public static <V> Opt<V> from(V v) {
        return v == null ? none() : some(v);
    }

    @Override
    public V getOrElse(V v) {
        return isEmpty() ? v : getOrFail();
    }

    public V orElse(Supplier<V> supplier) {
        return isEmpty() ? supplier.get() : getOrFail();
    }

    public Opt<V> or(Opt<V> that) {
        return this.isEmpty() ? that : this;
    }

    public <W> Opt<W> map(Function<? super V, ? extends W> fn) {
        return isEmpty() ? none() : some(fn.apply(_value));
    }

    public <W> Opt<W> flatMap(Function<? super V, Opt<W>> fn) {
        return isEmpty() ? none() : fn.apply(_value);
    }

    @Override
    public int hashCode() {
        return isEmpty() ? 0 : getOrFail().hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that instanceof Opt) {
            Opt<?> thatOpt = (Opt<?>) that;
            return this.isEmpty()
                    ? thatOpt.isEmpty()
                    : this.getOrFail().equals(thatOpt.getOrElse( null));
        }
        return false;
    }

    public Optional<V> toOptional() {
        return isEmpty() ? Optional.empty() : Optional.of(getOrFail());
    }

    public static <V> Opt<V> fromOptional(Optional<V> optional) {
        return optional.map(Opt::some).orElse(Opt.none());
    }

}
