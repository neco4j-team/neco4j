package org.neco4j.tuple;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import org.neco4j.function.TriFunction;

public final class Triple<A, B, C> {

    private final A a;
    private final B b;
    private final C c;

    private Triple(A a, B b, C c) {
        this.a = requireNonNull(a);
        this.b = requireNonNull(b);
        this.c = requireNonNull(c);
    }

    public static <A, B, C> Triple<A, B, C> of(A a, B b, C c) {
        return new Triple<>(a, b, c);
    }

    public A get1() {
        return a;
    }

    public B get2() {
        return b;
    }

    public C get3() {
        return c;
    }

    public <A1> Triple<A1, B, C> with1(A1 a1) {
        return of(a1, get2(), get3());
    }

    public <B1> Triple<A, B1, C> with2(B1 b1) {
        return of(get1(), b1, get3());
    }

    public <C1> Triple<A, B, C1> with3(C1 c1) {
        return of(get1(), get2(), c1);
    }

    public <A1> Triple<A1, B, C> map1(Function<? super A, ? extends A1> fn) {
        return of(fn.apply(get1()), get2(), get3());
    }

    public <B1> Triple<A, B1, C> map2(Function<? super B, ? extends B1> fn) {
        return of(get1(), fn.apply(get2()), get3());
    }

    public <C1> Triple<A, B, C1> map3(Function<? super C, ? extends C1> fn) {
        return of(get1(), get2(), fn.apply(get3()));
    }

    public <A1, B1, C1> Triple<A1, B1, C1> trimap(
            Function<? super A, ? extends A1> fnA,
            Function<? super B, ? extends B1> fnB,
            Function<? super C, ? extends C1> fnC
    ) {
        return of(fnA.apply(get1()), fnB.apply(get2()), fnC.apply(get3()));
    }

    public <D> D collapse(TriFunction<? super A, ? super B, ? super C, ? extends D> fn) {
        return fn.apply(get1(), get2(), get3());
    }

    @Override
    public String toString() {
        return String.format("(%s,%s,%s)", get1(), get2(), get3());
    }

    @Override
    public int hashCode() {
        return 37 * get1().hashCode() + 73 * get2().hashCode() + 91 * get3().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Triple) {
            Triple<?, ?, ?> that = (Triple) obj;
            return this.get1().equals(that.get1()) &&
                    this.get2().equals(that.get2()) &&
                    this.get3().equals(that.get3());
        }
        return false;
    }

}