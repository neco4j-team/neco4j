package org.neco4j.tuple;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.neco4j.function.QuadFunction;
import org.neco4j.function.TriFunction;

public final class Comprehension {

    private Comprehension() {
        throw new UnsupportedOperationException();
    }

    //cross product

    public static <A, B> Iterable<Pair<A, B>> combine(
            Iterable<? extends A> itA,
            Iterable<? extends B> itB) {
        return combineWith(itA, itB, Pair::of);
    }

    public static <A, B, C> Iterable<Triple<A, B, C>> combine(
            Iterable<? extends A> itA,
            Iterable<? extends B> itB,
            Iterable<? extends C> itC) {
        return combineWith(itA, itB, itC, Triple::of);
    }

    public static <A, B, C, D> Iterable<Quad<A, B, C, D>> combine(
            Iterable<? extends A> itA,
            Iterable<? extends B> itB,
            Iterable<? extends C> itC,
            Iterable<? extends D> itD) {
        return combineWith(itA, itB, itC, itD, Quad::of);
    }

    public static <A, B, R> Iterable<R> combineWith(
            Iterable<? extends A> itA,
            Iterable<? extends B> itB,
            BiFunction<? super A, ? super B, ? extends R> fn) {
        return () -> new Iterator<R>() {

            private Iterator<? extends A> iteratorA = itA.iterator();
            private Iterator<? extends B> iteratorB = itB.iterator();
            private Optional<A> currentA = iteratorA.hasNext()
                    ? Optional.of(iteratorA.next())
                    : Optional.<A>empty();

            @Override
            public boolean hasNext() {
                if (!iteratorB.hasNext()) {
                    if (!iteratorA.hasNext()) {
                        return false;
                    }
                    iteratorB = itB.iterator();
                    currentA = Optional.of(iteratorA.next());
                }
                return iteratorB.hasNext() && currentA.isPresent();
            }

            @Override
            public R next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return fn.apply(currentA.get(), iteratorB.next());
            }
        };
    }

    public static <A, B, C, R> Iterable<R> combineWith(
            Iterable<? extends A> itA,
            Iterable<? extends B> itB,
            Iterable<? extends C> itC,
            TriFunction<? super A, ? super B, ? super C, ? extends R> fn) {
        return () -> new Iterator<R>() {

            private Iterator<? extends A> iteratorA = itA.iterator();
            private Iterator<? extends B> iteratorB = itB.iterator();
            private Iterator<? extends C> iteratorC = itC.iterator();
            private Optional<A> currentA = iteratorA.hasNext()
                    ? Optional.of(iteratorA.next())
                    : Optional.<A>empty();
            private Optional<B> currentB = iteratorB.hasNext()
                    ? Optional.of(iteratorB.next())
                    : Optional.<B>empty();

            @Override
            public boolean hasNext() {
                if (!iteratorC.hasNext()) {
                    if (!iteratorB.hasNext()) {
                        if (!iteratorA.hasNext()) {
                            return false;
                        }
                        currentA = Optional.of(iteratorA.next());
                        iteratorB = itB.iterator();
                    }
                    if (!iteratorB.hasNext()) {
                        return false;
                    }
                    currentB = Optional.of(iteratorB.next());
                    iteratorC = itC.iterator();
                }
                return iteratorC.hasNext() && currentA.isPresent() && currentB.isPresent();
            }

            @Override
            public R next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return fn.apply(currentA.get(), currentB.get(), iteratorC.next());
            }
        };
    }

    public static <A, B, C, D, R> Iterable<R> combineWith(
            Iterable<? extends A> itA,
            Iterable<? extends B> itB,
            Iterable<? extends C> itC,
            Iterable<? extends D> itD,
            QuadFunction<? super A, ? super B, ? super C, ? super D, ? extends R> fn) {
        return () -> new Iterator<R>() {

            private Iterator<? extends A> iteratorA = itA.iterator();
            private Iterator<? extends B> iteratorB = itB.iterator();
            private Iterator<? extends C> iteratorC = itC.iterator();
            private Iterator<? extends D> iteratorD = itD.iterator();
            private Optional<A> currentA = iteratorA.hasNext()
                    ? Optional.of(iteratorA.next())
                    : Optional.<A>empty();
            private Optional<B> currentB = iteratorB.hasNext()
                    ? Optional.of(iteratorB.next())
                    : Optional.<B>empty();
            private Optional<C> currentC = iteratorC.hasNext()
                    ? Optional.of(iteratorC.next())
                    : Optional.<C>empty();

            @Override
            public boolean hasNext() {
                if (!iteratorD.hasNext()) {
                    if (!iteratorC.hasNext()) {
                        if (!iteratorB.hasNext()) {
                            if (!iteratorA.hasNext()) {
                                return false;
                            }
                            currentA = Optional.of(iteratorA.next());
                            iteratorB = itB.iterator();
                        }
                        if (!iteratorB.hasNext()) {
                            return false;
                        }
                        currentB = Optional.of(iteratorB.next());
                        iteratorC = itC.iterator();
                    }
                    if (!iteratorC.hasNext()) {
                        return false;
                    }
                    currentC = Optional.of(iteratorC.next());
                    iteratorD = itD.iterator();
                }
                return iteratorD.hasNext() && currentA.isPresent() && currentB.isPresent() && currentC.isPresent();
            }

            @Override
            public R next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return fn.apply(currentA.get(), currentB.get(), currentC.get(), iteratorD.next());
            }
        };
    }

    //parallel consumption (with the length of the shortest iterable)

    public static <A, B> Iterable<Pair<A, B>> zip(
            Iterable<? extends A> itA,
            Iterable<? extends B> itB) {
        return zipWith(itA, itB, Pair::of);
    }

    public static <A, B, C> Iterable<Triple<A, B, C>> zip(
            Iterable<? extends A> itA,
            Iterable<? extends B> itB,
            Iterable<? extends C> itC) {
        return zipWith(itA, itB, itC, Triple::of);
    }

    public static <A, B, C, D> Iterable<Quad<A, B, C, D>> zip(
            Iterable<? extends A> itA,
            Iterable<? extends B> itB,
            Iterable<? extends C> itC,
            Iterable<? extends D> itD) {
        return zipWith(itA, itB, itC, itD, Quad::of);
    }

    public static <K, V> Iterable<Pair<K, V>> zip(Map<K, V> map) {
        return zipWith(map, Pair::of);
    }

    public static <A, B, R> Iterable<R> zipWith(
            Iterable<? extends A> itA,
            Iterable<? extends B> itB,
            BiFunction<? super A, ? super B, ? extends R> fn) {
        return () -> new Iterator<R>() {

            private Iterator<? extends A> iteratorA = itA.iterator();
            private Iterator<? extends B> iteratorB = itB.iterator();

            @Override
            public boolean hasNext() {
                return iteratorA.hasNext() && iteratorB.hasNext();
            }

            @Override
            public R next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return fn.apply(iteratorA.next(), iteratorB.next());
            }
        };
    }

    public static <A, B, C, R> Iterable<R> zipWith(
            Iterable<? extends A> itA,
            Iterable<? extends B> itB,
            Iterable<? extends C> itC,
            TriFunction<? super A, ? super B, ? super C, ? extends R> fn) {
        return () -> new Iterator<R>() {

            private Iterator<? extends A> iteratorA = itA.iterator();
            private Iterator<? extends B> iteratorB = itB.iterator();
            private Iterator<? extends C> iteratorC = itC.iterator();

            @Override
            public boolean hasNext() {
                return iteratorA.hasNext() && iteratorB.hasNext() && iteratorC.hasNext();
            }

            @Override
            public R next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return fn.apply(iteratorA.next(), iteratorB.next(), iteratorC.next());
            }
        };
    }

    public static <A, B, C, D, R> Iterable<R> zipWith(
            Iterable<? extends A> itA,
            Iterable<? extends B> itB,
            Iterable<? extends C> itC,
            Iterable<? extends D> itD,
            QuadFunction<? super A, ? super B, ? super C, ? super D, ? extends R> fn) {
        return () -> new Iterator<R>() {

            private Iterator<? extends A> iteratorA = itA.iterator();
            private Iterator<? extends B> iteratorB = itB.iterator();
            private Iterator<? extends C> iteratorC = itC.iterator();
            private Iterator<? extends D> iteratorD = itD.iterator();

            @Override
            public boolean hasNext() {
                return iteratorA.hasNext() && iteratorB.hasNext() && iteratorC.hasNext() && iteratorD.hasNext();
            }

            @Override
            public R next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return fn.apply(iteratorA.next(), iteratorB.next(), iteratorC.next(), iteratorD.next());
            }
        };
    }

    public static <K, V, R> Iterable<R> zipWith(
            Map<K, V> map,
            BiFunction<? super K, ? super V, ? extends R> fn) {
        return map(map.entrySet(), entry -> fn.apply(entry.getKey(), entry.getValue()));
    }

    //transformation functions

    public static <A> Iterable<A> filter(Iterable<? extends A> itA, Predicate<? super A> predA) {
        return () -> new Iterator<A>() {

            private Iterator<? extends A> iteratorA = itA.iterator();
            private Optional<A> nextA = Optional.empty();

            @Override
            public boolean hasNext() {
                while (!nextA.isPresent() && iteratorA.hasNext()) {
                    A a = iteratorA.next();
                    if (predA.test(a)) {
                        nextA = Optional.of(a);
                    }
                }
                return nextA.isPresent();
            }

            @Override
            public A next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                A result = nextA.get();
                nextA = Optional.empty();
                return result;
            }
        };
    }

    public static <A> Iterable<A> limit(Iterable<? extends A> itA, int atMost) {
        return () -> new Iterator<A>() {

            private Iterator<? extends A> iteratorA = itA.iterator();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < atMost && iteratorA.hasNext();
            }

            @Override
            public A next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                index++;
                return iteratorA.next();
            }
        };
    }

    public static <A, B> Iterable<B> map(Iterable<? extends A> itA, Function<? super A, ? extends B> fn) {
        return () -> new Iterator<B>() {
            private Iterator<? extends A> iterator = itA.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public B next() {
                return fn.apply(iterator.next());
            }
        };
    }

    public static <A> Iterable<A> flatten(Iterable<? extends Iterable<A>> itItA) {
        return () -> new Iterator<A>() {

            private Iterator<? extends Iterable<A>> nestedIterator = itItA.iterator();
            private Iterator<? extends A> iteratorA = Collections.emptyIterator();

            @Override
            public boolean hasNext() {
                while (!iteratorA.hasNext() && nestedIterator.hasNext()) {
                    iteratorA = nestedIterator.next().iterator();
                }
                return iteratorA.hasNext();
            }

            @Override
            public A next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return iteratorA.next();
            }
        };
    }

    public static <A, B> Iterable<B> flatMap(Iterable<A> itA, Function<A, ? extends Iterable<B>> fn) {
        //We could use simply flatten(map(itA, fn)), however this causes some object creation overhead
        return () -> new Iterator<B>() {

            private Iterator<? extends A> iteratorA = itA.iterator();
            private Iterator<? extends B> iteratorB = Collections.emptyIterator();

            @Override
            public boolean hasNext() {
                while (!iteratorB.hasNext() && iteratorA.hasNext()) {
                    iteratorB = fn.apply(iteratorA.next()).iterator();
                }
                return iteratorB.hasNext();
            }

            @Override
            public B next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return iteratorB.next();
            }
        };
    }

    public static <A> Iterable<Pair<A, Integer>> indexed(Iterable<? extends A> itA) {
        return () -> new Iterator<Pair<A, Integer>>() {
            private Iterator<? extends A> iterator = itA.iterator();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Pair<A, Integer> next() {
                return Pair.of(iterator.next(), index++);
            }
        };
    }

    //useful converters to iterable

    public static <A> Iterable<A> optional(Optional<? extends A> optA) {
        return optA.isPresent()
                ? Collections.singletonList(optA.get())
                : Collections.emptyList();
    }

    public static <A> Iterable<A> array(A... as) {
        //an own implementation should be cheaper than Arrays.asList()
        return () -> new Iterator<A>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < as.length;
            }

            @Override
            public A next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return as[index++];
            }
        };
    }


}
