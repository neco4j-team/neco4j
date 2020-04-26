package org.neco4j.collect.unitkey;

import org.neco4j.collect.Reversible;

public final class Queue<V> implements UnitKeyAddable<V, Queue<V>>, Reversible<Queue<V>> {

    private final Stack<V> _front;
    private final Stack<V> _rear;

    private Queue(Stack<V> front, Stack<V> rear) {
        _front = front;
        _rear = rear;
    }

    @Override
    public Queue<V> add(V v) {
        return new Queue<>(_front.add(v), _rear);
    }

    @Override
    public Opt<Queue<V>> putOpt(V v) {
        Queue<V> frontFilled = fillFront();
        return frontFilled._front.putOpt(v)
            .map(newFront -> new Queue<>(newFront, frontFilled._rear));
    }

    @Override
    public Opt<V> getOpt() {
        return _rear.getOpt().or(_front.lastOpt());
    }

    @Override
    public Opt<Queue<V>> removeOpt() {
        Queue<V> rearFilled = fillRear();
        return rearFilled._rear.removeOpt()
                    .map(newRear -> new Queue<>(rearFilled._front, newRear));
    }

    @Override
    public boolean isEmpty() {
        return _front.isEmpty() && _rear.isEmpty();
    }

    @Override
    public long size() {
        return _front.size() + _rear.size();
    }

    public Queue<V> reverse() {
        return new Queue<>(_rear, _front);
    }

    public static <V> Queue<V> empty() {
        return new Queue<>(Stack.empty(), Stack.empty());
    }

    public static <V> Queue<V> singleton(V v) {
        return new Queue<>(Stack.empty(), Stack.singleton(v));
    }

    @SafeVarargs
    public static <V> Queue<V> of(V... vs) {
        return Queue.<V>empty().addAll(vs);
    }

    @Override
    public String toString() {
        return show();
    }

    private Queue<V> fillFront() {
        return _front.isEmpty()
                   ? new Queue<>(_rear.reverse(), Stack.empty())
                   : this;
    }

    private Queue<V> fillRear() {
        return _rear.isEmpty()
                   ? new Queue<>(Stack.empty(), _front.reverse())
                   : this;
    }
}
