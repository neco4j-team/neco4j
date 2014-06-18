package org.neco4j.collect;

import org.neco4j.either.Either;
import org.neco4j.tuple.Pair;

import static org.neco4j.collect.NecoList.cons;

public final class NecoLists {

    private NecoLists(){
    }

    public static <A, B> NecoList<A> lefts(NecoList<Either<A, B>> list) {
        return list.foldRight((e, ls) -> e.fold((A left) -> cons(left, ls), (B right) -> ls), NecoList.empty());
    }

    public static <A, B> NecoList<B> rights(NecoList<Either<A, B>> list) {
        return list.foldRight((e, rs) -> e.fold((A left) -> rs, (B right) -> cons(right, rs)), NecoList.empty());
    }

    public static <A, B> Pair<NecoList<A>, NecoList<B>> leftsRights(NecoList<Either<A, B>> list) {
        return list.foldRight((e, ps) -> e.fold(
                        left -> Pair.of(cons(left, ps.get1()), ps.get2()),
                        right -> Pair.of(ps.get1(), cons(right, ps.get2()))),
                Pair.of(NecoList.<A>empty(), NecoList.<B>empty()));
    }

    public static <A, B> Pair<NecoList<A>, NecoList<B>> unzip(NecoList<Pair<A, B>> list) {
        return list.foldRight((p, ps) -> Pair.<NecoList<A>, NecoList<B>>of(cons(p.get1(), ps.get1()), cons(p.get2(), ps.get2())),
                Pair.of(NecoList.<A>empty(), NecoList.<B>empty()));
    }

    /*
    public static <A> NecoList<A> iterateN(A start, int length, Function<? super A, ? extends A> fn) throws IndexOutOfBoundsException {
        if (length < 0) {
            throw new IndexOutOfBoundsException();
        }
        A value = start;
        NecoList<A> result = NecoList.empty();
        for (int i = 0; i < length; i++) {
            result = NecoList.cons(value, result);
            if (i + 1 < length) {
                value = fn.apply(value);
            }
        }
        return result.reverse();
    }

    public static <A> NecoList<A> iterateWhile(A start, Predicate<? super A> predicate, Function<? super A, ? extends A> fn) {
        A value = start;
        NecoList<A> result = NecoList.empty();
        while (predicate.test(value)) {
            result = NecoList.cons(value, result);
            value = fn.apply(value);
        }
        return result.reverse();
    }
    */

}
