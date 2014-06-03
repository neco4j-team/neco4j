package org.neco4j.tuple;

import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class Pair<A, B> {

    private final A a;
    private final B b;

    private Pair(A a, B b) {
        this.a = requireNonNull(a);
        this.b = requireNonNull(b);
    }

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }

    public A get1() {
        return a;
    }

    public B get2() {
        return b;
    }

    public <A1> Pair<A1, B> with1(A1 a1) {
        return of(a1, get2());
    }

    public <B1> Pair<A, B1> with2(B1 b1) {
        return of(get1(), b1);
    }

    public <A1> Pair<A1, B> map1(Function<? super A, ? extends A1> fn) {
        return of(fn.apply(get1()), get2());
    }

    public <B1> Pair<A, B1> map2(Function<? super B, ? extends B1> fn) {
        return of(get1(), fn.apply(get2()));
    }

    public <A1, B1> Pair<A1, B1> bimap(Function<? super A, ? extends A1> fnA, Function<? super B, ? extends B1> fnB) {
        return of(fnA.apply(get1()), fnB.apply(get2()));
    }

    public <C> C collapse(BiFunction<? super A, ? super B, ? extends C> fn) {
        return fn.apply(get1(), get2());
    }

    public Pair<B, A> swap() {
        return of(get2(), get1());
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)", get1(), get2());
    }

    @Override
    public int hashCode() {
        return 37 * get1().hashCode() + 73 * get2().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Pair) {
            Pair<?, ?> that = (Pair) obj;
            return this.get1().equals(that.get1()) &&
                    this.get2().equals(that.get2());
        }
        return false;
    }

}