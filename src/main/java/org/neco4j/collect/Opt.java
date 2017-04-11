package org.neco4j.collect;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * A collection which may contain zero or one elements.
 *
 * @param <V> the element type
 */
public abstract class Opt<V> implements WithUnitKey<V, Opt<V>> {

    private Opt() {
    }

    private static Opt<?> NONE = new Opt<Object>() {

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public long size() {
            return 0;
        }

        @Override
        public Opt<Opt<Object>> addOpt(Object o) {
            return Opt.some(some(o));
        }

        @Override
        public Opt<Opt<Object>> removeOpt() {
            return Opt.none();
        }

        public Object getOrFail() {
            throw new NoSuchElementException();
        }

        @Override
        public String toString() {
            return "None";
        }
    };

    public static <V> Opt<V> none() {
        return (Opt<V>) NONE;
    }

    public static <V> Opt<V> some(V v) {
        return new Opt<V>() {
            @Override
            public Opt<Opt<V>> addOpt(V v) {
                return Opt.none();
            }

            @Override
            public Opt<Opt<V>> removeOpt() {
                return Opt.some(none());
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long size() {
                return 1;
            }

            public V getOrFail() {
                return v;
            }

            @Override
            public String toString() {
                return "Some(" + v + ")";
            }
        };
    }

    @Override
    public Opt<V> getOpt() {
        return this;
    }

    public static <V> Opt<V> from(V v) {
        return v == null ? none() : some(v);
    }

    public abstract V getOrFail();

    public V getOrElse(V v) {
        return isEmpty() ? v : getOrFail();
    }

    public Opt<V> or(Opt<V> that) {
        return this.isEmpty() ? that : this;
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
                    : this.getOrFail().equals(thatOpt.getOrElse(null));
        }
        return false;
    }

    public Optional<V> toOptional() {
        return isEmpty() ? Optional.empty() : Optional.of(getOrFail());
    }

    public static <V> Opt fromOptional(Optional<V> optional) {
        return optional.map(Opt::some).orElse(Opt.none());
    }

}
