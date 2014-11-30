package org.neco4j.collect.old;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

class LazyCons<A> implements NecoList<A> {

    private final A head;
    private NecoList<A> tail = null;
    private Supplier<NecoList<A>> tailSupplier;

    LazyCons(A head, Supplier<NecoList<A>> tailSupplier) {
        this.head = Objects.requireNonNull(head);
        this.tailSupplier = Objects.requireNonNull(tailSupplier);
    }

    @Override
    public A head() {
        return head;
    }

    @Override
    public Optional<A> headOpt() {
        return Optional.of(head());
    }

    @Override
    public NecoList<A> tail() {
        if (tail == null) {
            tail = Objects.requireNonNull(tailSupplier.get());
            tailSupplier = null;
        }
        return tail;
    }

    @Override
    public Optional<NecoList<A>> tailOpt() {
        return Optional.of(tail());
    }

    @Override
    public A last() {
        return foldLeft(null, (a,b) -> b);
    }

    public Optional<A> lastOpt() {
        return Optional.of(last());
    }

    public NecoList<A> init() {
        return reverse().tail().reverse();
    }

    public Optional<NecoList<A>> initOpt() {
        return Optional.of(init());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (A a : this) {
            sb.append(sb.length() == 0 ? "[" : ",").append(a);
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NecoList<?>)) {
            return false;
        }
        NecoList<?> one = this;
        NecoList<?> two = (NecoList<?>) obj;
        while (!one.isEmpty() && !two.isEmpty()) {
            if (!one.head().equals(two.head())) {
                return false;
            }
            one = one.tail();
            two = two.tail();
        }
        return one.isEmpty() && two.isEmpty();
    }

    @Override
    public int hashCode() {
        return this.foldLeft(0, (a, b) -> 31 * a + b.hashCode());
    }
}
