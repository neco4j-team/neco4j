package org.neco4j.collect;

import java.util.AbstractList;
import java.util.Iterator;

public class MutableList<A> extends AbstractList<A> {

    public List<A> wrapped;

    public MutableList(List<A> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public A get(int index) {
        return wrapped.get(index);
    }

    @Override
    public int size() {
        return wrapped.size();
    }

    @Override
    public Iterator<A> iterator() {
        return wrapped.iterator();
    }

    @Override
    public boolean isEmpty() {
        return wrapped.isEmpty();
    }

    @Override
    public A set(int index, A element) {
        return super.set(index, element);
    }

    @Override
    public boolean add(A a) {
        wrapped = wrapped.plusAll(a);
        return true;
    }

    @Override
    public void add(int index, A element) {
        super.add(index, element);
    }

    @Override
    public A remove(int index) {
        return super.remove(index);
    }

}
