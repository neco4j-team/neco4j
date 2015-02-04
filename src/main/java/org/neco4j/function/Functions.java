package org.neco4j.function;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.neco4j.tuple.Pair;
import org.neco4j.tuple.Quadruple;
import org.neco4j.tuple.Triple;

/**
 * @author Daniel Gronau <daniel.gronau@skillcert.de>
 */
public final class Functions {

    private Functions() {
        throw new UnsupportedOperationException();
    }

    public static <A, B, R> Function<Pair<A, B>, R> toFunction(
            BiFunction<A, B, R> biFunction) {
        return pair -> biFunction.apply(pair.get1(), pair.get2());
    }

    public static <A, B, C, R> Function<Triple<A, B, C>, R> toFunction(
            TriFunction<A, B, C, R> triFunction) {
        return triple -> triFunction.apply(triple.get1(), triple.get2(), triple.get3());
    }

    public static <A, B, C, D, R> Function<Quadruple<A, B, C, D>, R> toFunction(
            QuadFunction<A, B, C, D, R> quadFunction) {
        return quad -> quadFunction.apply(quad.get1(), quad.get2(), quad.get3(), quad.get4());
    }

    public static <A, B, R> BiFunction<A, B, R> toBiFunction(
            Function<Pair<A, B>, R> function) {
        return (a, b) -> function.apply(Pair.of(a, b));
    }

    public static <A, B, C, R> TriFunction<A, B, C, R> toTriFunction(
            Function<Triple<A, B, C>, R> function) {
        return (a, b, c) -> function.apply(Triple.of(a, b, c));
    }

    public static <A, B, C, D, R> QuadFunction<A, B, C, D, R> toQuadFunction(
            Function<Quadruple<A, B, C, D>, R> function) {
        return (a, b, c, d) -> function.apply(Quadruple.of(a, b, c, d));
    }

    public static <A, B, R> Function<A, Function<B, R>> curry(
            BiFunction<A, B, R> biFunction) {
        return a -> b -> biFunction.apply(a, b);
    }

    public static <A, B, C, R> Function<A, Function<B, Function<C, R>>> curry(
            TriFunction<A, B, C, R> triFunction) {
        return a -> b -> c -> triFunction.apply(a, b, c);
    }

    public static <A, B, C, D, R> Function<A, Function<B, Function<C, Function<D, R>>>> curry(
            QuadFunction<A, B, C, D, R> quadFunction) {
        return a -> b -> c -> d -> quadFunction.apply(a, b, c, d);
    }

    public static <A, B, R> BiFunction<A, B, R> uncurryBiFunction(
            Function<A, Function<B, R>> function) {
        return (a, b) -> function.apply(a).apply(b);
    }

    public static <A, B, C, R> TriFunction<A, B, C, R> uncurryTriFunction(
            Function<A, Function<B, Function<C, R>>> function) {
        return (a, b, c) -> function.apply(a).apply(b).apply(c);
    }

    public static <A, B, C, D, R> QuadFunction<A, B, C, D, R> uncurryQuadFunction(
            Function<A, Function<B, Function<C, Function<D, R>>>> function) {
        return (a, b, c, d) -> function.apply(a).apply(b).apply(c).apply(d);
    }

    public static <A, B, R> BiFunction<B, A, R> swap(
            BiFunction<A, B, R> biFunction) {
        return (b, a) -> biFunction.apply(a, b);
    }

    /**
     * The Y-Combinator for writing recursive functions using lambdas.
     * <p>
     * Java's lambdas have no means to reference themselves (this refers to the enclosing context),
     * hence you can't write recursive lambdas directly. With the yCombinator function,
     * you write your recursive lambda as a BiFunction with the original parameter and
     * a second parameter representing the lambda itself.
     * <p>
     * Example:
     * <code>Function<Integer,Long> factorial = yCombinator(
     * (n, self) -> n == 0 ? 1L : n * self.apply(n-1));</code>
     *
     * @param f   the BiFunction with an explicit self-reference
     * @param <X> the original parameter type
     * @param <Y> the result type
     * @return the recursive function
     */
    public static <X, Y> Function<X, Y> yCombinator(BiFunction<X, Function<X, Y>, Y> f) {
        return x -> f.apply(x, yCombinator(f));
    }

}
