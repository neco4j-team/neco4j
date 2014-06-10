package org.neco4j.collect;

import java.util.Objects;
import java.util.Optional;

class Cons<A> implements List<A> {

    private final A head;
    private final List<A> tail;

    Cons(A head, List<A> tail) {
        this.head = Objects.requireNonNull(head);
        this.tail = Objects.requireNonNull(tail);
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
    public List<A> tail() {
        return tail;
    }

    @Override
    public Optional<List<A>> tailOpt() {
        return Optional.of(tail());
    }

    @Override
    public A last() {
        return foldLeft(null, (a,b) -> b);
    }

    public Optional<A> lastOpt() {
        return Optional.of(last());
    }

    public List<A> init() {
        return reverse().tail().reverse();
    }

    public Optional<List<A>> initOpt() {
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
        if (!(obj instanceof List<?>)) {
            return false;
        }
        List<?> one = this;
        List<?> two = (List<?>) obj;
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
