package org.neco4j.collect;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class StrictList<A> implements List<A> {

    private A head;
    private StrictList<A> tail;

    private final static StrictList<?> NIL = new StrictList<Object>(null, null) {
        @Override
        public boolean isEmpty() {
            return true;
        }
    };

    private StrictList(A head, StrictList<A> tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <A> StrictList<A> cons(A head, StrictList<A> tail) {
        return new StrictList<A>(head, tail);
    }

    @SafeVarargs
    public static <A> StrictList<A> of(A... elements) {
        StrictList<A> result = empty();
        for (int i = elements.length - 1; i >= 0; i--) {
            result = cons(elements[i], result);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <A> StrictList<A> empty() {
        return (StrictList<A>) NIL;
    }

    @Override
    public A head() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("head() call on NIL");
        }
        return head;
    }

    @Override
    public Optional<A> headOpt() {
        if (isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(head);
    }


    @Override
    public StrictList<A> tail() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("tail() call on NIL");
        }
        return tail;
    }

    @Override
    public Optional<StrictList<A>> tailOpt() {
        if (isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(tail);
    }

    @Override
    public A last() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("last() call on NIL");
        }
        StrictList<A> resultList = this;
        while (!resultList.tail.isEmpty()) {
            resultList = resultList.tail;
        }
        return resultList.head;
    }

    @Override
    public StrictList<A> init() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("init() call on NIL");
        }
        StrictList<A> result = empty();
        for(A a : this) {
            result = cons(a, result);
        }
        return result.tail.reverse();
    }

    @Override
    public Optional<A> getOpt(long index) {
        StrictList<A> current = this;
        long i = index;
        while (i-- > 0) {
            if (current.isEmpty()) {
                return Optional.empty();
            }
            current = current.tail();
        }
        return Optional.of(current.head());
    }

    @Override
    public A get(long index) throws IndexOutOfBoundsException {
        StrictList<A> current = this;
        long i = index;
        while (i-- > 0) {
            if (current.isEmpty()) {
                throw new IndexOutOfBoundsException();
            }
            current = current.tail;
        }
        return current.head;
    }

    public StrictList<A> plus(A a) {
        return cons(a, this);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    //all prefixes of the current list, ordered from empty to full list
    @Override
    public StrictList<List<A>> inits() {
        StrictList<StrictList<A>> result = of(empty());
        for(A a : this.reverse()) {
            result = cons(empty(), result.map(partial -> cons(a, partial)));
        }
        return result.map(StrictList::reverse);
    }

    //all suffixes of the current stream ordered from full list to empty
    @Override
    public StrictList<List<A>> tails() {
        StrictList<List<A>> result = empty();
        for(StrictList<A> current = this; !current.isEmpty(); current = current.tail) {
            result = cons(current, result);
        }
        result = cons(empty(), result);
        return result.reverse();
    }

    @Override
    public <B> StrictList<B> map(Function<? super A, ? extends B> fn) {
        StrictList<B> result = empty();
        for(A a : this) {
            result = cons(fn.apply(a), result);
        }
        return result.reverse();
    }

    @Override
    public <B> StrictList<B> flatMap(Function<A, ? extends List<B>> fn) {
        return flatten(map(fn));
    }

    public static <A> StrictList<A> flatten(StrictList<List<A>> nested) {
        StrictList<A> result = empty();
        for(List<A> partial : nested.reverse()) {
             for(A a : partial.reverse()) {
                 result = cons(a, result);
             }
        }
        return result;
    }

    @Override
    public StrictList<A> reverse() {
        StrictList<A> result = empty();
        for(StrictList<A> current = this; !current.isEmpty(); current = current.tail) {
            result = cons(current.head, result);
        }
        return result;
    }

    @Override
    public StrictList<A> take(int n) {
        StrictList<A> result = StrictList.empty();
        for (A a : this) {
            if (n-- <= 0) {
                break;
            }
            result = cons(a, result);
        }
        return result.reverse();
    }

    @Override
    public StrictList<A> takeWhile(Predicate<A> predicate) {
        StrictList<A> result = empty();
        for(A a : this) {
            if (! predicate.test(a)) {
                break;
            }
            result = cons(a, result);
        }
        return result.reverse();
    }

    @Override
    public StrictList<A> filter(Predicate<? super A> predicate) {
        StrictList<A> result = empty();
        for(A a : this) {
            if (predicate.test(a)) {
                result = cons(a, result);
            }
        }
        return result.reverse();
    }

    @Override
    public <B> StrictList<B> scanLeft(B seed, BiFunction<? super B, ? super A, ? extends B> fn) {
        B b = seed;
        StrictList<B> result = of(b);
        for(A a : this) {
            b = fn.apply(b,a);
            result = cons(b, result);
        }
        return result.reverse();
    }

    @Override
    public <B> StrictList<B> scanRight(BiFunction<? super A, ? super B, ? extends B> fn, B seed) {
        B b = seed;
        StrictList<B> result = of(b);
        for(A a : this.reverse()) {
            b = fn.apply(a,b);
            result = cons(b, result);
        }
        return result;
    }

    @Override
    public StrictList<A> scanLeft1(BiFunction<? super A, ? super A, ? extends A> fn) {
        return isEmpty()
                ? empty()
                : tail().scanLeft(head(), fn);
    }

    @Override
    public StrictList<A> scanRight1(BiFunction<? super A, ? super A, ? extends A> fn) {
        return reverse().scanLeft1((a1,a2) -> fn.apply(a2,a1));
    }

    @Override
    public Iterator<A> iterator() {
        return new Iterator<A>() {

            private StrictList<A> list = StrictList.this;

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

    @Override
    public boolean isLazy() {
        return false;
    }

    @Override
    public List<A> lazy() {
        LazyList<A> result = LazyList.empty();
        for(A a : this.reverse()) {
            result = LazyList.cons(a, result);
        }
        return result;
    }

    @Override
    public List<A> strict() {
        return this;
    }

}
