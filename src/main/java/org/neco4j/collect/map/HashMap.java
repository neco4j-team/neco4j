package org.neco4j.collect.map;

import org.neco4j.collect.Puttable;
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

public class HashMap<K, V> implements Puttable<K, V, HashMap<K, V>> {

    private final static Node<?, ?> EMPTY = new Node<>();

    private final Node<K, V> _root;
    private final long _size;

    private HashMap(Node<K, V> root, long size) {
        _root = root;
        _size = size;
    }

    public static <K, V> HashMap<K, V> empty() {
        return new HashMap<>(emptyNode(), 0L);
    }

    public static <K, V> HashMap<K, V> of(K k0, V v0) {
        return HashMap.<K, V>empty().put(k0, v0);
    }

    public static <K, V> HashMap<K, V> of(K k0, V v0, K k1, V v1) {
        return of(k0, v0).put(k1, v1);
    }

    public static <K, V> HashMap<K, V> of(K k0, V v0, K k1, V v1, K k2, V v2) {
        return of(k0, v0, k1, v1).put(k2, v2);
    }

    public static <K, V> HashMap<K, V> of(K k0, V v0, K k1, V v1, K k2, V v2, K k3, V v3) {
        return of(k0, v0, k1, v1, k2, v2).put(k3, v3);
    }

    public static <K, V> HashMap<K, V> of(K k0, V v0, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return of(k0, v0, k1, v1, k2, v2, k3, v3).put(k4, v4);
    }

    public static <K, V> HashMap<K, V> of(K k0, V v0, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return of(k0, v0, k1, v1, k2, v2, k3, v3, k4, v4).put(k5, v5);
    }

    @Override
    public Opt<HashMap<K, V>> addOpt(K k, V v) {
        return _root.addOpt(k, v).map(root -> new HashMap<>(root, _size + 1));
    }

    @Override
    public Opt<V> getOpt(K k) {
        return _root.getOpt(k);
    }

    @Override
    public Iterable<Pair<K, V>> asKeyValuePairs() {
        return _root.asKeyValuePairs().collect(Collectors.toList());
    }

    public Stream<Pair<K, V>> stream() {
        return StreamSupport.stream(asKeyValuePairs().spliterator(), false);
    }

    @Override
    public Opt<HashMap<K, V>> removeOpt(K k) {
        return _root.removeOpt(k).map(root -> new HashMap<>(root, _size - 1));
    }

    @Override
    public long size() {
        return _size;
    }

    @Override
    public HashMap<K, V> put(K k, V v) {
        return new HashMap<>(_root.put(k, v), _size + 1);
    }

    public HashSet<K> keys() {
        return HashSet.from(_root.asKeyValuePairs().map(Pair::get1).collect(Collectors.toList()));
    }

    public List<V> values() {
        return List.ofAll(_root.asKeyValuePairs().map(Pair::get2).collect(Collectors.toList()));
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Node<K, V> emptyNode() {
        return (Node<K, V>) EMPTY;
    }

    private static class Node<K, V> {
        final int _hashCode;
        final Stack<Pair<K, V>> _kv;
        final Node<K, V> _left;
        final Node<K, V> _right;

        private Node() {
            _hashCode = 0;
            _kv = null;
            _left = null;
            _right = null;
        }

        private Node(int hashCode, Stack<Pair<K, V>> kv, Node<K, V> left, Node<K, V> right) {
            _hashCode = hashCode;
            _kv = Objects.requireNonNull(kv);
            _left = left == null || left.isEmpty() ? null : left;
            _right = right == null || right.isEmpty() ? null : right;
        }


        private Node(K key, V value, Node<K, V> left, Node<K, V> right) {
            this(key.hashCode(), Stack.of(Pair.of(key, value)), left, right);
        }

        public Stream<Pair<K, V>> asKeyValuePairs() {
            return isEmpty()
                       ? Stream.empty()
                       : Stream.concat(Stream.concat(_kv.stream(), left().asKeyValuePairs()), right().asKeyValuePairs());
        }

        public boolean isEmpty() {
            return this == EMPTY;
        }

        private Node<K, V> left() {
            return _left == null ? emptyNode() : _left;
        }

        private Node<K, V> right() {
            return _right == null ? emptyNode() : _right;
        }

        private boolean contains(K key) {
            if (key.hashCode() == hashCode()) {
                return _kv.stream().anyMatch(pair -> pair.get1().equals(key));
            }
            return key.hashCode() < _hashCode
                       ? left().contains(key)
                       : right().contains(key);
        }

        private Opt<V> getOpt(K key) {
            if (isEmpty()) {
                return Opt.none();
            } else if (_hashCode == key.hashCode()) {
                return Opt.fromOptional(_kv.stream()
                                           .filter(pair -> pair.get1().equals(key))
                                           .map(Pair::get2)
                                           .findFirst());
            } else {
                return key.hashCode() < _hashCode
                           ? left().getOpt(key)
                           : right().getOpt(key);
            }
        }

        private Opt<Node<K, V>> removeOpt(K key) {
            if (isEmpty()) {
                return Opt.none();
            }
            if (key.hashCode() == _hashCode) {
                if (_kv.stream().noneMatch(pair -> pair.get1().equals(key))) {
                    return Opt.none();
                } else if (_kv.size() == 1) {
                    return Opt.some(merge(left(), right()));
                } else {
                    Stack<Pair<K, V>> kv = _kv.stream()
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

        private Opt<Node<K, V>> addOpt(K key, V value) {
            if (isEmpty()) {
                return Opt.some(new Node<>(key, value, null, null));
            }
            if (key.hashCode() == _hashCode) {
                if (_kv.stream().anyMatch(pair -> pair.get1().equals(key))) {
                    return Opt.none();
                } else {
                    return Opt.some(new Node<>(_hashCode, _kv.add(Pair.of(key, value)), _left, _right));
                }
            }
            if (key.hashCode() < _hashCode) {
                return left().addOpt(key, value).map(left -> new Node<>(_hashCode, _kv, left, _right));
            } else {
                return right().addOpt(key, value).map(right -> new Node<>(_hashCode, _kv, _left, right));
            }
        }

        private Node<K, V> put(K key, V value) {
            if (isEmpty()) {
                return new Node<>(key, value, null, null);
            }
            if (key.hashCode() == _hashCode) {
                if (_kv.stream().anyMatch(pair -> pair.get1().equals(key))) {
                    java.util.List<Pair<K, V>> kv = _kv.stream()
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

        static <K, V> Node<K, V> merge(Node<K, V> left, Node<K, V> right) {
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
        for (Pair<K, V> pair : this.asKeyValuePairs()) {
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
        HashMap<?, ?> that = (HashMap<?, ?>) o;
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
