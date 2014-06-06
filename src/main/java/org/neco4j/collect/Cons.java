package org.neco4j.collect;

import org.neco4j.tuple.Pair;

import java.util.Objects;

class Cons<A> implements List<A> {

    private final A head;
    private final List<A> tail;

    Cons(A head, List<A> tail) {
        this.head = Objects.requireNonNull(head);
        this.tail = Objects.requireNonNull(tail);
    }

    public A head() {
        return head;
    }

    public List<A> tail() {
        return tail;
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
        List<?> that = (List<?>) obj;
        for (Pair<A, ?> pair : this.zip(that)) {
            if (!pair.get1().equals(pair.get2())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.foldLeft(0, (a, b) -> 31 * a.hashCode() + b.hashCode());
    }
}
