package org.neco4j.collect.set;

import org.neco4j.collect.indexed.List;
import org.neco4j.collect.unitkey.Opt;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class HashSet<K> implements SetLike<K, HashSet<K>>, SetLikePuttable<K, HashSet<K>> {

    private final static Node<?> EMPTY = new Node<>();

    private final Node<K> _root;
    private final long _size;

    private HashSet(Node<K> root, long size) {
        _root = root;
        _size = size;
    }

    public static <K> HashSet<K> empty() {
        return new HashSet<>(emptyNode(), 0L);
    }

    @SafeVarargs
    public static <K> HashSet<K> of(K... ks) {
        HashSet<K> result = empty();
        for (K k : ks) {
            result = result.put(k);
        }
        return result;
    }

    public static <K> HashSet<K> from(Iterable<K> iterable) {
        HashSet<K> result = empty();
        for (K k : iterable) {
            result = result.put(k);
        }
        return result;
    }

    @Override
    public Opt<HashSet<K>> addOpt(K key) {
        return _root.addOpt(key).map(n -> new HashSet<>(n, _size + 1));
    }

    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {
            List<Node<K>> nodes = List.of(_root);

            @Override
            public boolean hasNext() {
                return nodes.first().map(n -> !n.isEmpty()).getOrElse(false);
            }

            @Override
            public K next() {
                if (! hasNext()) {
                    throw new NoSuchElementException();
                }
                Node<K> node = nodes.first().getOrFail();
                nodes = nodes.removeFirstOpt().getOrFail();
                if (!node.right().isEmpty()) {
                    nodes = nodes.prepend(node.right());
                }
                if (!node.left().isEmpty()) {
                    nodes = nodes.prepend(node.left());
                }
                return node._key;
            }
        };
    }

    @Override
    public HashSet<K> put(K key) {
        return addOpt(key).getOrElse(this);
    }

    @Override
    public boolean contains(K key) {
        return _root.contains(key);
    }

    @Override
    public Opt<HashSet<K>> removeOpt(K key) {
        return _root.removeOpt(key).map(n -> new HashSet<>(n, _size - 1));
    }

    @Override
    public long size() {
        return _size;
    }

    @SuppressWarnings("unchecked")
    private static <K> Node<K> emptyNode() {
        return (Node) EMPTY;
    }

    private static class Node<K> {
        final K _key;
        final Node<K> _left;
        final Node<K> _right;

        private Node() {
            _key = null;
            _left = null;
            _right = null;
        }

        private Node(K key, Node<K> left, Node<K> right) {
            _key = Objects.requireNonNull(key);
            _left = left == null || left.isEmpty() ? null : left;
            _right = right == null || right.isEmpty() ? null : right;
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
            return !isEmpty() &&
                           (key.equals(_key) ||
                                    (key.hashCode() < _key.hashCode()
                                             ? left().contains(key)
                                             : right().contains(key)));
        }

        private Opt<Node<K>> removeOpt(K key) {
            if (isEmpty()) {
                return Opt.none();
            }
            if (key.equals(_key)) {
                return Opt.some(merge(left(), right()));
            }
            if (key.hashCode() < _key.hashCode()) {
                return left().removeOpt(key).map(
                        left -> new Node<>(_key, left, _right));
            } else {
                return right().removeOpt(key).map(
                        right -> new Node<>(_key, _left, right));
            }
        }

        private Opt<Node<K>> addOpt(K key) {
            if (isEmpty()) {
                return Opt.some(new Node<>(key, null, null));
            }
            if (key.equals(_key)) {
                return Opt.none();
            }
            if (key.hashCode() < _key.hashCode()) {
                return left().addOpt(key).map(left -> new Node<>(_key, left, _right));
            } else {
                return right().addOpt(key).map(right -> new Node<>(_key, _left, right));
            }
        }

        static <K> Node<K> merge(Node<K> left, Node<K> right) {
            if (left.isEmpty()) {
                return right;
            }
            if (right.isEmpty()) {
                return left;
            }
            return new Node<>(left._key, left.left(), merge(left.right(), right));
        }

    }

}
