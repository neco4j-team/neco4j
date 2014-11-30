package org.neco4j.collect.old;

import org.neco4j.either.Either;
import org.neco4j.tuple.Pair;

public class NecoLists {

    private NecoLists() {
    }

    public static <A> NecoList<A> fromIterable(Iterable<A> iterable) throws NullPointerException {
        NecoList<A> result = NecoList.empty();
        for (A a : iterable) {
            result = NecoList.cons(a, result);
        }
        return result.reverse();
    }

    public static <A, B> Pair<NecoList<A>, NecoList<B>> eithers(NecoList<Either<A, B>> list) {
        return list.foldRight((e, ps) -> e.fold(
                        left -> Pair.of(NecoList.cons(left, ps.get1()), ps.get2()),
                        right -> Pair.of(ps.get1(), NecoList.cons(right, ps.get2()))),
                Pair.of(NecoList.<A>empty(), NecoList.<B>empty()));
    }

    public static <A, B> Pair<NecoList<A>, NecoList<B>> unzip(NecoList<Pair<A, B>> list) {
        return list.foldRight((p, ps) -> Pair.<NecoList<A>, NecoList<B>>of(
                        NecoList.cons(p.get1(), ps.get1()), NecoList.cons(p.get2(), ps.get2())),
                Pair.of(NecoList.<A>empty(), NecoList.<B>empty()));
    }

    public static <A, B> NecoList<Pair<A, B>> zip(NecoList<A> first, NecoList<B> second) {
        NecoList<A> aList = first;
        NecoList<B> bList = second;
        NecoList<Pair<A, B>> result = NecoList.empty();
        while (!aList.isEmpty() && !bList.isEmpty()) {
            result = NecoList.cons(Pair.of(aList.head(), bList.head()), result);
            aList = aList.tail();
            bList = bList.tail();
        }
        return result.reverse();
    }

    public static <A, B> NecoList<Pair<A, B>> strictZip(NecoList<A> first, NecoList<B> second) throws IllegalArgumentException {
        if (first.size() != second.size()) {
            throw new IllegalArgumentException("list sizes must match");
        }
        return zip(first, second);
    }
}
