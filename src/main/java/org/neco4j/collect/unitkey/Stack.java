package org.neco4j.collect.unitkey;

import java.util.Objects;

public class Stack<V> implements UnitKeyAddable<V,Stack<V>> {

    private final V _value;
    private final Stack<V> _below;

    private Stack(V value, Stack<V> below){
       _value = value;
       _below = below;
    }

    private static Stack<?> EMPTY = new Stack<Object>(null, null){
        public boolean isEmpty(){
            return true;
        }
    };

    public boolean isEmpty(){
        return false;
    }

    @Override
    public long size() {
        return isEmpty() ? 0 : 1 + _below.size();
    }

    @SuppressWarnings("unckecked")
    public static <V> Stack<V> empty() {
        return (Stack<V>) EMPTY;
    }

    public static <V> Stack<V> singleton(V v) {
        return Stack.<V>empty().add(v);
    }

    @SafeVarargs
    public static <V> Stack<V> of(V ... vs) {
        return Stack.<V>empty().addAll(vs);
    }

    @Override
    public Stack<V> add(V v) {
        return new Stack<>(Objects.requireNonNull(v), this);
    }

    @Override
    public Opt<Stack<V>> putOpt(V v) {
        return isEmpty() ? Opt.none() : Opt.some(new Stack<>(Objects.requireNonNull(v), this._below));
    }

    @Override
    public Opt<V> getOpt() {
        return Opt.from(_value);
    }

    public Opt<V> lastOpt() {
        V result = null;
        for(V v : this) {
            result = v;
        }
        return Opt.from(result);
    }

    public Stack<V> reverse() {
        Stack<V> result = empty();
        for(V v : this) {
            result = result.add(v);
        }
        return result;
    }

    @Override
    public Opt<Stack<V>> removeOpt() {
        return Opt.from(_below);
    }

    @Override
    public String toString() {
        return show("Stack");
    }
}
