package org.neco4j.collect.multiset;

import org.neco4j.collect.indexed.List;
import org.neco4j.collect.set.HashSet;
import org.neco4j.collect.unitkey.Opt;
import org.neco4j.collect.unitkey.Stack;
import org.neco4j.collect.unitkey.UnitKeyAddable;
import org.neco4j.tuple.Pair;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A collection of keys having assigned a certain amount.
 * <p>
 * Add operations change the amount of a key, while put operations overwrite it.
 * <p>
 * Add operations may add negative amounts, but are not successful if the resulting amount is negative.
 * Getting the amount of a key to zero is permitted, but will remove the key from the collection.
 * Note that the remove operation will remove the whole key. If you want to decrease the amount, use addOpt(k, -1) instead.
 *
 * @param <K> the element type of the multiset
 */
public class MultiSet<K> implements MultiSetLike<K, MultiSet<K>> {

    private final static Node<?> EMPTY = new Node<>();

    private final Node<K> _root;
    private final long _size;

    private MultiSet(Node<K> root, long size) {
        _root = root;
        _size = size;
    }

    public static <K> MultiSet<K> empty() {
        return new MultiSet<>(emptyNode(), 0L);
    }

    public static <K> MultiSet<K> of(K k0, int v0) {
        return MultiSet.<K>empty().put(k0, v0);
    }

    public static <K> MultiSet<K> of(K k0, int v0, K k1, int v1) {
        return of(k0, v0).put(k1, v1);
    }

    public static <K> MultiSet<K> of(K k0, int v0, K k1, int v1, K k2, int v2) {
        return of(k0, v0, k1, v1).put(k2, v2);
    }

    public static <K> MultiSet<K> of(K k0, int v0, K k1, int v1, K k2, int v2, K k3, int v3) {
        return of(k0, v0, k1, v1, k2, v2).put(k3, v3);
    }

    public static <K> MultiSet<K> of(K k0, int v0, K k1, int v1, K k2, int v2, K k3, int v3, K k4, int v4) {
        return of(k0, v0, k1, v1, k2, v2, k3, v3).put(k4, v4);
    }

    public static <K> MultiSet<K> of(K k0, int v0, K k1, int v1, K k2, int v2, K k3, int v3, K k4, int v4, K k5, int v5) {
        return of(k0, v0, k1, v1, k2, v2, k3, v3, k4, v4).put(k5, v5);
    }

    public static <K> MultiSet<K> ofAll(Iterable<K> iterable) {
        MultiSet<K> result = empty();
        for(K k : iterable) {
            result = result.add(k);
        }
        return result;
    }

    @Override
    public Opt<MultiSet<K>> addOpt(K k, Integer v) {
        if (v == 0) {
            return Opt.some(this);
        }
        int newValue = _root.get(k) + v;
        return newValue < 0
                   ? Opt.none()
                   : Opt.some(put(k, newValue));
    }

    public boolean containsKey(K key) {
        return _root.contains(key);
    }

    @Override
    public Iterable<Pair<K, Integer>> asKeyValuePairs() {
        return _root.asKeyValuePairs().collect(Collectors.toList());
    }

    @Override
    public int get(K k) {
        return _root.get(k);
    }

    public Stream<Pair<K, Integer>> stream() {
        return StreamSupport.stream(asKeyValuePairs().spliterator(), false);
    }

    @Override
    public Opt<MultiSet<K>> removeOpt(K k) {
        return _root.removeOpt(k).map(root -> new MultiSet<>(root, _size - 1));
    }

    @Override
    public long size() {
        return _size;
    }

    @Override
    public MultiSet<K> put(K k, Integer v) {
        if (v < 0) {
            throw new IllegalArgumentException("Can't set an amount in a MultiSet smaller than 0");
        } else if (v == 0) {
            return removeIfPossible(k);
        } else {
            return new MultiSet<>(_root.put(k, v), _size + (_root.contains(k) ? 0 : 1));
        }
    }

    public HashSet<K> keys() {
        return HashSet.from(_root.asKeyValuePairs().map(Pair::get1).collect(Collectors.toList()));
    }

    public List<Integer> values() {
        return List.ofAll(_root.asKeyValuePairs().map(Pair::get2).collect(Collectors.toList()));
    }

    @SuppressWarnings("unchecked")
    private static <K> Node<K> emptyNode() {
        return (Node<K>) EMPTY;
    }

    private static class Node<K> {
        final int _hashCode;
        final Stack<Pair<K, Integer>> _kv;
        final Node<K> _left;
        final Node<K> _right;

        private Node() {
            _hashCode = 0;
            _kv = null;
            _left = null;
            _right = null;
        }

        private Node(int hashCode, Stack<Pair<K, Integer>> kv, Node<K> left, Node<K> right) {
            _hashCode = hashCode;
            _kv = Objects.requireNonNull(kv);
            _left = left == null || left.isEmpty() ? null : left;
            _right = right == null || right.isEmpty() ? null : right;
        }


        private Node(K key, Integer value, Node<K> left, Node<K> right) {
            this(key.hashCode(), Stack.of(Pair.of(key, value)), left, right);
        }

        public Stream<Pair<K, Integer>> asKeyValuePairs() {
            return isEmpty()
                       ? Stream.empty()
                       : Stream.concat(Stream.concat(_kv.stream(), left().asKeyValuePairs()), right().asKeyValuePairs());
        }

        public boolean isEmpty() {
            return this == EMPTY;
        }

        private Node<K> left() {
            return _left == null ? emptyNode() : _left;
        }

        private Node<K> right() {
            return _right == null ? emptyNode() : _right;
        }

        private boolean contains(K key) {
            if (isEmpty()) {
                return false;
            }
            if (key.hashCode() == _hashCode) {
                return _kv.stream().anyMatch(pair -> pair.get1().equals(key));
            }
            return key.hashCode() < _hashCode
                       ? left().contains(key)
                       : right().contains(key);
        }

        private int get(K key) {
            if (isEmpty()) {
                return 0;
            } else if (_hashCode == key.hashCode()) {
                return _kv.stream()
                          .filter(pair -> pair.get1().equals(key))
                          .map(Pair::get2)
                          .findFirst()
                          .orElse(0);
            } else {
                return key.hashCode() < _hashCode
                           ? left().get(key)
                           : right().get(key);
            }
        }

        private Opt<Node<K>> removeOpt(K key) {
            if (isEmpty()) {
                return Opt.none();
            }
            if (key.hashCode() == _hashCode) {
                if (_kv.stream().noneMatch(pair -> pair.get1().equals(key))) {
                    return Opt.none();
                } else if (_kv.size() == 1) {
                    return Opt.some(merge(left(), right()));
                } else {
                    Stack<Pair<K, Integer>> kv = _kv.stream()
                                                    .filter(pair -> !pair.get1().equals(key))
                                                    .collect(Stack::empty, Stack::add, UnitKeyAddable::addAll);
                    return Opt.some(new Node<>(_hashCode, kv, _left, _right));
                }
            }
            if (key.hashCode() < _hashCode) {
                return left().removeOpt(key).map(
                    left -> new Node<>(_hashCode, _kv, left, _right));
            } else {
                return right().removeOpt(key).map(
                    right -> new Node<>(_hashCode, _kv, _left, right));
            }
        }

        private Node<K> put(K key, Integer value) {
            if (isEmpty()) {
                return new Node<>(key, value, null, null);
            }
            if (key.hashCode() == _hashCode) {
                if (_kv.stream().anyMatch(pair -> pair.get1().equals(key))) {
                    java.util.List<Pair<K, Integer>> kv = _kv.stream()
                                                             .map(pair -> pair.get1().equals(key) ? Pair.of(key, value) : pair)
                                                             .collect(Collectors.toList());
                    return new Node<>(_hashCode, Stack.ofAll(kv), _left, _right);
                } else {
                    return new Node<>(_hashCode, _kv.add(Pair.of(key, value)), _left, _right);
                }
            }
            if (key.hashCode() < _hashCode) {
                return new Node<>(_hashCode, _kv, left().put(key, value), _right);
            } else {
                return new Node<>(_hashCode, _kv, _left, right().put(key, value));
            }
        }

        static <K> Node<K> merge(Node<K> left, Node<K> right) {
            if (left.isEmpty()) {
                return right;
            }
            if (right.isEmpty()) {
                return left;
            }
            return new Node<>(left._hashCode, left._kv, left.left(), merge(left.right(), right));
        }

    }

    @Override
    public int hashCode() {
        int hc = 0;
        for (Pair<K, Integer> pair : this.asKeyValuePairs()) {
            hc += pair.hashCode();
        }
        return hc;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(this.getClass())) {
            return false;
        }
        if (o == this) {
            return true;
        }
        MultiSet<?> that = (MultiSet<?>) o;
        if (this._size != that.size() || this.hashCode() != that.hashCode()) {
            return false;
        }
        for (Pair<?, ?> pair : that.asKeyValuePairs()) {
            try {
                if (!getOpt((K) pair.get1())
                         .map(v -> v.equals(pair.get2()))
                         .getOrElse(false)) {
                    return false;
                }
            } catch (ClassCastException ex) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");
        this.asKeyValuePairs().forEach(p -> joiner.add(p.get1().toString() + ":" + p.get2().toString()));
        return String.format("%s[%s]", getClass().getSimpleName(), joiner.toString());
    }

}
