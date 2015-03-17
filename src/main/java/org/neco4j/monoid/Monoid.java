package org.neco4j.monoid;

import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * A semigroup (<code>BinaryOperator<code/>) with an identity element:
 * apply(x, identity) == apply(identity, x) == x
 */
public interface Monoid<A> extends BinaryOperator<A> {

    A identity();

    default A fold(Iterable<A> as) {
        A result = identity();
        for (A a : as) {
            result = apply(result, a);
        }
        return result;
    }

    default A fold(A... as) {
        A result = identity();
        for (A a : as) {
            result = apply(result, a);
        }
        return result;
    }

    default <B> A foldMap(Iterable<B> bs, Function<B, A> fn) {
        A result = identity();
        for (B b : bs) {
            result = apply(result, fn.apply(b));
        }
        return result;
    }

}