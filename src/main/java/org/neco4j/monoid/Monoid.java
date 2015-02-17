package org.neco4j.monoid;

import java.util.function.BinaryOperator;

/**
 * A semigroup (<code>BinaryOperator<code/>) with an identity element:
 * apply(x, identity) == apply(identity, x) == x
 */
public interface Monoid<A> extends BinaryOperator<A> {

    public A identity();

    public default A fold(Iterable<A> as) {
        return fold(identity(), as);
    }

    public default A fold(A seed, Iterable<A> as) {
        A result = seed;
        for (A a : as) {
            result = apply(result, a);
        }
        return result;
    }

    public default A fold(A... as) {
        return fold(identity(), as);
    }

    public default A fold(A seed, A... as) {
        A result = seed;
        for (A a : as) {
            result = apply(result, a);
        }
        return result;
    }

}