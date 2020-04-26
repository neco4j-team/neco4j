package org.neco4j.collect.deque;

import org.neco4j.collect.Reversable;
import org.neco4j.collect.unitkey.Opt;
import org.neco4j.collect.unitkey.Stack;

public class Deque<V> implements DequeLikeAddable<V, Deque<V>>, Reversable<Deque<V>> {

    private final Stack<V> _front;
    private final Stack<V> _rear;

    private Deque(Stack<V> front, Stack<V> rear) {
        _front = front;
        _rear = rear;
    }

    @Override
    public Deque<V> addFirst(V v) {
        return new Deque<>(_front.add(v), _rear);
    }

    @Override
    public Deque<V> addLast(V v) {
        return new Deque<>(_front, _rear.add(v));
    }

    @Override
    public Opt<Deque<V>> putFirstOpt(V v) {
        Deque<V> frontFilled = fillFront();
        return frontFilled._front.putOpt(v)
                                 .map(newFront -> new Deque<>(newFront, frontFilled._rear));
    }

    @Override
    public Opt<Deque<V>> putLastOpt(V v) {
        Deque<V> rearFilled = fillRear();
        return rearFilled._rear.putOpt(v)
                               .map(newRear -> new Deque<>(rearFilled._front, newRear));
    }

    @Override
    public Opt<V> getFirstOpt() {
        return _front.getOpt().or(_rear.lastOpt());
    }

    @Override
    public Opt<V> getLastOpt() {
        return _rear.getOpt().or(_front.lastOpt());
    }

    @Override
    public Opt<Deque<V>> removeFirstOpt() {
        Deque<V> frontFilled = fillFront();
        return frontFilled._front.removeOpt()
                                 .map(newFront -> new Deque<>(newFront, frontFilled._rear));
    }

    @Override
    public Opt<Deque<V>> removeLastOpt() {
        Deque<V> rearFilled = fillRear();
        return rearFilled._rear.removeOpt()
                               .map(newRear -> new Deque<>(rearFilled._front, newRear));
    }

    @Override
    public boolean isEmpty() {
        return _front.isEmpty() && _rear.isEmpty();
    }

    @Override
    public long size() {
        return _front.size() + _rear.size();
    }

    public Deque<V> reverse() {
        return new Deque<>(_rear, _front);
    }

    public static <V> Deque<V> empty() {
        return new Deque<>(Stack.empty(), Stack.empty());
    }

    public static <V> Deque<V> singleton(V v) {
        return new Deque<>(Stack.empty(), Stack.singleton(v));
    }

    @SafeVarargs
    public static <V> Deque<V> of(V... vs) {
        Deque<V> result = Deque.empty();
        for (V v : vs) {
            result = result.addLast(v);
        }
        return result;
    }

    @Override
    public String toString() {
        return show();
    }

    private Deque<V> fillFront() {
        return _front.isEmpty()
                   ? new Deque<>(_rear.reverse(), Stack.empty())
                   : this;
    }

    private Deque<V> fillRear() {
        return _rear.isEmpty()
                   ? new Deque<>(Stack.empty(), _front.reverse())
                   : this;
    }
}
