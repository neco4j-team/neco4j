package org.neco4j.tuple;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;
import java.util.function.Predicate;

import org.neco4j.function.QuadFunction;

/**
 * @author Daniel Gronau <daniel.gronau@skillcert.de>
 */
public class Quadruple<A, B, C, D> {

    private final A a;
    private final B b;
    private final C c;
    private final D d;

    private Quadruple(A a, B b, C c, D d) {
        this.a = requireNonNull(a);
        this.b = requireNonNull(b);
        this.c = requireNonNull(c);
        this.d = requireNonNull(d);
    }

    public static <A, B, C, D> Quadruple<A, B, C, D> of(A a, B b, C c, D d) {
        return new Quadruple<>(a, b, c, d);
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

    public D get4() {
        return d;
    }

    public <A1> Quadruple<A1, B, C, D> with1(A1 a1) {
        return of(a1, get2(), get3(), get4());
    }

    public <B1> Quadruple<A, B1, C, D> with2(B1 b1) {
        return of(get1(), b1, get3(), get4());
    }

    public <C1> Quadruple<A, B, C1, D> with3(C1 c1) {
        return of(get1(), get2(), c1, get4());
    }

    public <D1> Quadruple<A, B, C, D1> with4(D1 d1) {
        return of(get1(), get2(), get3(), d1);
    }

    public <A1> Quadruple<A1, B, C, D> map1(Function<? super A, ? extends A1> fn) {
        return of(fn.apply(get1()), get2(), get3(), get4());
    }

    public <B1> Quadruple<A, B1, C, D> map2(Function<? super B, ? extends B1> fn) {
        return of(get1(), fn.apply(get2()), get3(), get4());
    }

    public <C1> Quadruple<A, B, C1, D> map3(Function<? super C, ? extends C1> fn) {
        return of(get1(), get2(), fn.apply(get3()), get4());
    }

    public <D1> Quadruple<A, B, C, D1> map4(Function<? super D, ? extends D1> fn) {
        return of(get1(), get2(), get3(), fn.apply(get4()));
    }

    public <A1, B1, C1, D1> Quadruple<A1, B1, C1, D1> quadmap(
            Function<? super A, ? extends A1> fnA,
            Function<? super B, ? extends B1> fnB,
            Function<? super C, ? extends C1> fnC,
            Function<? super D, ? extends D1> fnD
    ) {
        return of(fnA.apply(get1()), fnB.apply(get2()), fnC.apply(get3()), fnD.apply(get4()));
    }

    public <E> E fold(QuadFunction<? super A, ? super B, ? super C, ? super D, ? extends E> fn) {
        return fn.apply(get1(), get2(), get3(), get4());
    }

    public boolean testAnd(
            Predicate<A> predicateA,
            Predicate<B> predicateB,
            Predicate<C> predicateC,
            Predicate<D> predicateD
    ) {
        return predicateA.test(get1()) && predicateB.test(get2()) &&
                predicateC.test(get3()) && predicateD.test(get4());
    }

    public boolean testOr(
            Predicate<A> predicateA,
            Predicate<B> predicateB,
            Predicate<C> predicateC,
            Predicate<D> predicateD
    ) {
        return predicateA.test(get1()) || predicateB.test(get2()) ||
                predicateC.test(get3()) || predicateD.test(get4());
    }

    @Override
    public String toString() {
        return String.format("(%s,%s,%s,%s)", get1(), get2(), get3(), get4());
    }

    @Override
    public int hashCode() {
        return 37 * get1().hashCode() + 73 * get2().hashCode() +
                91 * get3().hashCode() + 101 * get4().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Quadruple) {
            Quadruple<?, ?, ?, ?> that = (Quadruple) obj;
            return this.get1().equals(that.get1()) &&
                    this.get2().equals(that.get2()) &&
                    this.get3().equals(that.get3()) &&
                    this.get4().equals(that.get4());
        }
        return false;
    }

}
