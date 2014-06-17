package org.neco4j.collect;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Nil<A> implements NecoList<A> {

    final static Nil<?> NIL = new Nil<>();

    private Nil() {
    }

    @Override
    public A head() throws NoSuchElementException {
        throw new NoSuchElementException("head() call on empty list");
    }

    @Override
    public Optional<A> headOpt() {
        return Optional.empty();
    }

    @Override
    public NecoList<A> tail() throws NoSuchElementException {
        throw new NoSuchElementException("tail() call on empty list");
    }

    @Override
    public Optional<NecoList<A>> tailOpt(){
        return Optional.empty();
    }

    @Override
    public A last() throws NoSuchElementException {
        throw new NoSuchElementException("last() call on empty list");
    }

    @Override
    public Optional<A> lastOpt() {
        return Optional.empty();
    }

    @Override
    public NecoList<A> init() throws NoSuchElementException {
        throw new NoSuchElementException("init() call on empty list");
    }

    @Override
    public Optional<NecoList<A>> initOpt() {
        return Optional.empty();
    }

    @Override
    public A get(int index) throws IndexOutOfBoundsException {
        throw new IndexOutOfBoundsException("get() call on empty list");
    }

    @Override
    public Optional<A> getOpt(int index) {
        return Optional.empty();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public NecoList<A> with(int index, A value) throws IndexOutOfBoundsException {
        throw new IndexOutOfBoundsException("with() call on empty list");
    }

    @Override
    public Iterator<A> iterator() {
        return Collections.emptyIterator();
    }

    public String toString() {
        return "[]";
    }
}
