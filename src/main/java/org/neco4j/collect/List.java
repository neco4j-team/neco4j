package org.neco4j.collect;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface List<A> extends Iterable<A> {

    public A head() throws NoSuchElementException;

    public List<A> tail() throws NoSuchElementException;

    public A last() throws NoSuchElementException;

    public List<A> init() throws NoSuchElementException;

    public Optional<A> headOpt();

    public Optional<List<A>> tailOpt();

    public A get(int index) throws IndexOutOfBoundsException;

    public Optional<A> getOpt(int index);

    public int size();

    public boolean isEmpty();

    public List<A> with(int index, A value);

    public List<A> concat(List<? extends A> that);

    public List<A> take(int count);

    public List<A> drop(int count);

    public <B> List<B> map(Function<? super A, ? extends B> fn);

    public <B> List<B> flatMap(Function<? super A, ? extends Iterable<? extends A>> fn);

    public <A> List<A> filter(Predicate<? super A> predicate);
}
