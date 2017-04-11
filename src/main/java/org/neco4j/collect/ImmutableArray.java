package org.neco4j.collect;

import org.neco4j.loop.Loop;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

public class ImmutableArray<A> implements Loop<A> {
    private final A[] array;

    @SafeVarargs
    private ImmutableArray(A... array) {
        this.array = array;
    }

    @SafeVarargs
    public static <A> ImmutableArray<A> of(A... array) {
        return new ImmutableArray<>(array);
    }

    @Override
    public Iterator<A> iterator() {
        return new Iterator<A>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public A next() {
                if(! hasNext()) {
                    throw new NoSuchElementException();
                }
                return array[index++];
            }
        };
    }

    public A get(int index) throws ArrayIndexOutOfBoundsException {
        return array[index];
    }

    public Optional<A> getOptional(int index) {
        return 0 <= index && index < array.length
                ? Optional.of(array[index])
                : Optional.empty();
    }

    public ImmutableArray<A> set(int index, A value) throws ArrayIndexOutOfBoundsException {
        A[] copy = getArrayCopy();
        copy[index] = value;
        return ImmutableArray.of(copy);
    }

    public <B> ImmutableArray<B> map(Function<? super A, ? extends  B> function) {
        @SuppressWarnings("unchecked")
        B[] resultArray = (B[]) new Object[size()];
        for(int i = 0; i < size(); i++) {
            resultArray[i] = function.apply(array[i]);
        }
        return ImmutableArray.of(resultArray);
    }

    public int size() {
        return array.length;
    }

    public A[] getArrayCopy() {
        return array.clone();
    }

    public static <A> ImmutableArray<A> append(ImmutableArray<A> first, ImmutableArray<A> second) {
        @SuppressWarnings("unchecked")
        A[] array = (A[]) new Object[first.size() + second.size()];
        System.arraycopy(first.array,0,array,0, first.size());
        System.arraycopy(second.array,0,array,first.size(), second.size());
        return new ImmutableArray<>(array);
    }
}
