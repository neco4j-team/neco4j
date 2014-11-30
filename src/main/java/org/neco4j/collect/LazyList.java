package org.neco4j.collect;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LazyList<A> implements List<A> {

    private Supplier<A> head;
    private Supplier<LazyList<A>> tail;

    private final static LazyList<?> NIL = new LazyList<Object>(null, null) {
        @Override
        public boolean isEmpty() {
            return true;
        }
    };

    private LazyList(Supplier<A> head, Supplier<LazyList<A>> tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <A> LazyList<A> cons(A head, LazyList<A> tail) {
        return new LazyList<A>(new Evaluated<A>(head), new Evaluated<LazyList<A>>(tail));
    }

    public static <A> LazyList<A> cons(A head, Supplier<LazyList<A>> tailSupplier) {
        return new LazyList<A>(new Evaluated<A>(head), tailSupplier);
    }

    public static <A> LazyList<A> cons(Supplier<A> headSupplier, LazyList<A> tail) {
        return new LazyList<A>(headSupplier, new Evaluated<>(tail));
    }

    public static <A> LazyList<A> cons(Supplier<A> headSupplier, Supplier<LazyList<A>> tailSupplier) {
        return new LazyList<>(headSupplier, tailSupplier);
    }

    @SafeVarargs
    public static <A> LazyList<A> of(A... elements) {
        LazyList<A> result = empty();
        for (int i = elements.length - 1; i >= 0; i--) {
            result = cons(elements[i], result);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <A> LazyList<A> empty() {
        return (LazyList<A>) NIL;
    }

    public A head() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("head() call on NIL");
        }
        A result = head.get();
        if (!(head instanceof Evaluated)) {
            head = new Evaluated<>(result);
        }
        return result;
    }

    public Optional<A> headOpt() {
        if (isEmpty()) {
            return Optional.empty();
        }
        A result = head.get();
        if (!(head instanceof Evaluated)) {
            head = new Evaluated<>(result);
        }
        return Optional.of(result);
    }


    public LazyList<A> tail() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("tail() call on NIL");
        }
        LazyList<A> result = tail.get();
        if (!(tail instanceof Evaluated)) {
            tail = new Evaluated<>(result);
        }
        return result;
    }

    public Optional<LazyList<A>> tailOpt() {
        if (isEmpty()) {
            return Optional.empty();
        }
        LazyList<A> result = tail.get();
        if (!(tail instanceof Evaluated)) {
            tail = new Evaluated<>(result);
        }
        return Optional.of(result);
    }

    public A last() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("last() call on NIL");
        }
        LazyList<A> resultList = this;
        while (!resultList.tail().isEmpty()) {
            resultList = resultList.tail();
        }
        return resultList.head();
    }

    public LazyList<A> init() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("init() call on NIL");
        }
        if (tail().isEmpty()) {
            return empty();
        }
        return cons(head, () -> tail().init());
    }

    public Optional<A> getOpt(long index) {
        LazyList<A> current = this;
        long i = index;
        while (i-- > 0) {
            if (current.isEmpty()) {
                return Optional.empty();
            }
            current = current.tail();
        }
        return Optional.of(current.head());
    }

    public A get(long index) throws IndexOutOfBoundsException {
        LazyList<A> current = this;
        long i = index;
        while (i-- > 0) {
            if (current.isEmpty()) {
                throw new IndexOutOfBoundsException();
            }
            current = current.tail();
        }
        return current.head();
    }

    public LazyList<A> plus(A a) {
        return cons(a, this);
    }

    public boolean isEmpty() {
        return false;
    }

    //all prefixes of the current list, ordered from empty to full list
    public LazyList<List<A>> inits() {
        return LazyList.cons(empty(), () -> isEmpty() ? empty()
                : tail().inits().map(list -> LazyList.<A>cons(this::head, (LazyList) list)));
    }

    //all suffixes of the current stream ordered from full list to empty
    public LazyList<List<A>> tails() {
        return LazyList.cons(this, () -> isEmpty() ? LazyList.empty() : tail().tails());
    }

    public <B> LazyList<B> map(Function<? super A, ? extends B> fn) {
        return isEmpty() ? empty() : LazyList.<B>cons(() -> fn.apply(head()), () -> tail().map(fn));
    }

    public <B> LazyList<B> flatMap(Function<A, ? extends List <B>> fn) {
        if (isEmpty()) {
            return empty();
        }
        List<B> headResult = fn.apply(head()).reverse();
        if (headResult.isEmpty()) {
            return tail().flatMap(fn);
        }  else {
            LazyList<B> result = LazyList.<B>cons(headResult::head, () -> tail().flatMap(fn));
            for(B b : headResult.tail()) {
                result = cons(b, result);
            }
            return result;
        }
    }

    public LazyList<A> reverse() {
        LazyList<A> result = empty();
        for(LazyList<A> current = this; ! current.isEmpty(); current = current.tail()) {
            result = cons(current.head, result);
        }
        return result;
    }

    public LazyList<A> take(int n) {
        LazyList<A> result = LazyList.empty();
        for (A a : this) {
            if (n-- <= 0) {
                break;
            }
            result = cons(a, result);
        }
        return result.reverse();
    }

    public LazyList<A> takeWhile(Predicate<A> predicate) {
        return predicate.test(head()) ? cons(head(), () -> tail().takeWhile(predicate)) : empty();
    }

    public LazyList<A> filter(Predicate<? super A> predicate) {
        List<A> list = this.dropWhile(predicate.negate());
        return list.isEmpty()
                ? empty()
                : LazyList.<A>cons(list.head(), () -> (LazyList) list.tail().filter(predicate));
    }

    public <B> LazyList<B> scanLeft(B seed, BiFunction<? super B, ? super A, ? extends B> fn) {
        return isEmpty()
                ? empty()
                : LazyList.<B>cons(seed, () -> tail().scanLeft(fn.apply(seed, head()), fn));
    }

    public <B> LazyList<B> scanRight(BiFunction<? super A, ? super B, ? extends B> fn, B seed) {
         return reverse().scanLeft(seed, (b,a) -> fn.apply(a,b));
    }

    public LazyList<A> scanLeft1(BiFunction<? super A, ? super A, ? extends A> fn) {
        return isEmpty()
                ? empty()
                : tail().scanLeft(head(), fn);
    }

    public LazyList<A> scanRight1(BiFunction<? super A, ? super A, ? extends A> fn) {
        return reverse().scanLeft1((a1,a2) -> fn.apply(a2,a1));
    }

    @Override
    public Iterator<A> iterator() {
        return new Iterator<A>() {

            private LazyList<A> list = LazyList.this;

            @Override
            public boolean hasNext() {
                return !list.isEmpty();
            }

            @Override
            public A next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                A result = list.head();
                list = list.tail();
                return result;
            }
        };
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(head().toString());
        for (A a : this.tail()) {
            sb.append(",").append(a);
        }
        return sb.append("]").toString();
    }

    private static class Evaluated<A> implements Supplier<A> {

        private final A a;

        private Evaluated(A a) {
            this.a = a;
        }

        @Override
        public A get() {
            return a;
        }
    }

    @Override
    public boolean isLazy() {
        return true;
    }

    @Override
    public List<A> lazy() {
        return this;
    }

    @Override
    public List<A> strict() {
        StrictList<A> result = StrictList.empty();
        for(A a : this) {
            result = StrictList.cons(a, result);
        }
        return result.reverse();
    }
}
