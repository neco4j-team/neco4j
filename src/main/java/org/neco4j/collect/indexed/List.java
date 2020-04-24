package org.neco4j.collect.indexed;

import org.neco4j.collect.unitkey.Opt;
import org.neco4j.collect.unitkey.Stack;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class List<V> implements IndexedAddable<V, List<V>> {

    private final int _heapSize;
    private final Heap<V> _heap;
    private final List<V> _next;

    private final static List<?> EMPTY = new List<>(0, null, null);

    private List(int heapSize, Heap<V> heap, List<V> next) {
        _heapSize = heapSize;
        _heap = heap;
        _next = next;
    }

    @Override
    public boolean isEmpty() {
        return this == EMPTY;
    }

    @SuppressWarnings("unchecked")
    public static <V> List<V> empty() {
        return (List<V>) EMPTY;
    }

    public static <V> List<V> singleton(V v) {
        return List.<V>empty().prepend(v);
    }

    @SafeVarargs
    public static <V> List<V> of(V ... vs) {
        List<V> result = List.empty();
        for(int i = vs.length - 1; i >= 0; i--) {
            result = result.prepend(vs[i]);
        }
        return result;
    }

    @Override
    public List<V> prepend(V v) {
        if (isEmpty() || _heapSize != _next._heapSize) {
            return new List<>(1, new Leaf<>(v), this);
        } else {
            return new List<>(2 * _heapSize + 1,
                    new Branch<>(v, _heap, _next._heap),
                    _next._next);
        }
    }

    //TODO: use better algorithm, store Stack<Heap<V>> and rebuild whole nodes
    @Override
    public Opt<List<V>> putOpt(Integer index, V v) {
        return manipulateAt(index, current -> current.isEmpty()
                ? Opt.none()
                : Opt.some(current.removeFirstOpt().getOrFail().prepend(v)));
    }

    @Override
    public Opt<List<V>> addOpt(Integer index, V v) {
        return manipulateAt(index, current -> Opt.some(current.prepend(v)));
    }

    @Override
    public Opt<V> getOpt(Integer index) {
        if (index < 0 || isEmpty()) {
            return Opt.none();
        } else if (index < _heapSize) {
            return  Opt.some(_heap.get(_heapSize, index));
        } else {
            return _next.getOpt(index - _heapSize);
        }
    }

    @Override
    public Opt<List<V>> removeOpt(Integer index) {
        return manipulateAt(index, List::removeFirstOpt);
    }

    private Opt<List<V>> manipulateAt(int index, Function<List<V>, Opt<List<V>>> op) {
        if (index < 0) {
            return Opt.none();
        }
        Stack<V> stack = Stack.empty();
        List<V> current = this;
        while (! current.isEmpty() && index >= current._heapSize) {
           stack = stack.addAll(current._heap);
           index -= current._heapSize;
           current = current._next;
        }
        while(index-- > 0) {
            if (current.isEmpty()) {
                return Opt.none();
            }
            stack = stack.add(current.getOpt(0).getOrFail());
            current = current.removeFirstOpt().getOrFail();
        }

        Opt<List<V>> result = op.apply(current);
        if (result.isEmpty()) {
            return Opt.none();
        }
        current = result.getOrFail();

        while (! stack.isEmpty()) {
            current = current.prepend(stack.getOrFail());
            stack = stack.removeOpt().getOrFail();
        }
        return Opt.some(current);
    }

    public Opt<List<V>> removeFirstOpt() {
        if (isEmpty()) {
            return Opt.none();
        } else if (_heapSize == 1) {
            return Opt.some(_next);
        } else {
            Branch<V> branch = (Branch<V>) _heap;
            return Opt.some(new List<>(_heapSize/2, branch._left,
                    new List<>(_heapSize / 2, branch._right, _next)));
        }
    }

    @Override
    public long size() {
        return isEmpty() ? 0L : _heapSize + _next.size();
    }

    private static abstract class Heap<V> implements Iterable<V> {
        protected final V _value;

        private Heap(V value) {
            _value = value;
        }

        abstract V get(int size, int index);

        @Override
        public Iterator<V> iterator() {
            return new Iterator<V>() {
                private Stack<Heap<V>> _todo = Stack.singleton(Heap.this);

                @Override
                public boolean hasNext() {
                    return ! _todo.isEmpty();
                }

                @Override
                public V next() {
                    if (hasNext()) {
                        Heap<V> current = _todo.getOrFail();
                        _todo = _todo.removeOpt().getOrFail();
                        if (current instanceof Branch) {
                            Branch<V> branch = (Branch<V>) current;
                            _todo = _todo.add(branch._right).add(branch._left);
                        }
                        return current._value;
                    }
                    throw new NoSuchElementException();
                }
            };
        }
    }

    private static class Leaf<V> extends Heap<V> {
        private Leaf(V value) {
            super(value);
        }

        @Override
        V get(int size, int index) {
            if (size != 1 || index != 0) {
                throw new AssertionError("size " + size + " index " + index + " on Leaf");
            }
            return _value;
        }
    }

    private static class Branch<V> extends Heap<V> {
        private Heap<V> _left;
        private Heap<V> _right;

        private Branch(V value, Heap<V> left, Heap<V> right) {
            super(value);
            _left = left;
            _right = right;
        }

        @Override
        V get(int size, int index) {
            if (index == 0) {
                return _value;
            } else if (index <= size / 2) {
               return _left.get(size / 2, index - 1);
            } else {
               return _right.get(size / 2, index - size/2 - 1);
            }
        }
    }

}