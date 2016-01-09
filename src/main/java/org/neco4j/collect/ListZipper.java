package org.neco4j.collect;

import java.util.NoSuchElementException;
import java.util.function.Function;

public class ListZipper<A> {

    private final List<A> front;
    private final List<A> end;
    private final int position;

    ListZipper(List<A> front, List<A> end, int position) {
        this.front = front;
        this.end = end;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public boolean atStart() {
        return front.isEmpty();
    }

    public boolean atEnd() {
        return end.isEmpty();
    }

    public List<A> toList() {
        List<A> result = end;
        for(A a : front) {
            result = result.plus(a);
        }
        return result;
    }

    public List<A> toReverseList() {
        List<A> result = front;
        for(A a : end) {
            result = result.plus(a);
        }
        return result;
    }

    public ListZipper<A> reverse() {
        return new ListZipper<A>(end, front, end.size());
    }

    public ListZipper<A> insert(A ...  as) {
        return new ListZipper<>(front, end.plus(as), position);
    }

    public ListZipper<A> remove(int n) throws NoSuchElementException {
        return new ListZipper<>(front, end.drop(n), position);
    }

    public ListZipper<A> set(A ... as) throws NoSuchElementException {
        return new ListZipper<>(front, end.drop(as.length).plus(as), position);
    }

    public ListZipper<A> change(Function<? super A, ? extends A> fn) throws NoSuchElementException {
        if (end.isEmpty()) {
            throw new NoSuchElementException();
        }
        return new ListZipper<>(front, end.tail().plus(fn.apply(end.head())), position);
    }

    public ListZipper<A> move(int n) throws IndexOutOfBoundsException {
        List<A> newFront = front;
        List<A> newEnd = end;
        if (n >= 0) {
            for(int i = 0; i < n; i++) {
                if (newEnd.isEmpty()) {
                    throw new IndexOutOfBoundsException();
                }
                newFront = newFront.plus(newEnd.head());
                newEnd = newEnd.tail();
            }
        } else {
            for(int i = 0; i < -n; i++) {
                if (newFront.isEmpty()) {
                    throw new IndexOutOfBoundsException();
                }
                newEnd = newEnd.plus(newFront.head());
                newFront = newFront.tail();
            }
        }
        return new ListZipper<>(newFront, newEnd, position + n);
    }

}
