package org.neco4j.loop;

import org.neco4j.collect.ImmutableArray;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public interface Loop<A> extends Iterable<A> {

    static <A> Loop<A> unfold(A start, Function<A, Optional<A>> step) {
        return () -> new Iterator<A>() {
            private Optional<A> value = Optional.of(start);

            @Override
            public boolean hasNext() {
                return value.isPresent();
            }

            @Override
            public A next() {
                A result = value.orElseThrow(NoSuchElementException::new);
                value = value.flatMap(step);
                return result;
            }
        };
    }

    static <A> Loop<A> of(A start, UnaryOperator<A> step, Predicate<A> condition) {
        return () -> new Iterator<A>() {
            private A value = start;
            private boolean test = true;

            @Override
            public boolean hasNext() {
                return test;
            }

            @Override
            public A next() {
                if (!test) {
                    throw new NoSuchElementException();
                }
                A result = value;
                value = step.apply(value);
                test = condition.test(value);
                return result;
            }
        };
    }

    static <A> Loop<A> of(Iterable<A> iterable) {
        return iterable::iterator;
    }

    static <A> Loop<A> of(Iterator<A> iterator) {
        return () -> iterator;
    }

    static <A> Loop<A> of(Optional<A> optional) {
        return () -> optional
                .map(Collections::singletonList)
                .orElse(Collections.emptyList())
                .iterator();
    }

    @SafeVarargs
    static <A> Loop<A> of(A ... values) {
        return ImmutableArray.of(values);
    }

    static Loop<Integer> from(int start) {
        return of(start, n -> n + 1, n -> true);
    }

    static Loop<Integer> range(int start, int endExclusive) {
        return of(start, n -> n + 1, n -> n < endExclusive);
    }

    static Loop<Integer> range(int start, int step, int endExclusive) {
        return of(start, n -> n + step, n -> n < endExclusive);
    }

    static Loop<Integer> rangeInclusive(int start, int end) {
        return of(start, n -> n + 1, n -> n <= end);
    }

    static Loop<Integer> rangeInclusive(int start, int step, int end) {
        return of(start, n -> n + step, n -> n <= end);
    }

    default BiLoop<A, Integer> indexed() {
        return BiLoop.zip(this, from(0));
    }

    default <B> BiLoop<A,B> zip(Iterator<B> iteratorB) {
        return BiLoop.zip(iterator(), iteratorB);
    }

    default <B> BiLoop<A,B> zip(Iterable<B> iterableB) {
        return BiLoop.zip(this, iterableB);
    }

    default <B> BiLoop<A,B> zip(B ... values) {
        return BiLoop.zip(this, ImmutableArray.of(values));
    }

    default void loop(Consumer<? super A> consumer) {
        forEach(consumer);
    }

}
