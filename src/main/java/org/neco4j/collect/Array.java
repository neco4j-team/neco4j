package org.neco4j.collect;

import java.util.Arrays;

public abstract class Array<V> implements IndexedAddable<V, Array<V>> {

    @SafeVarargs
    public static <V> Array<V> of(V ... vs) {
        return new Array<V>() {

            @Override
            @SuppressWarnings("unchecked")
            public Opt<Array<V>> addOpt(Integer index, V v) {
                if (0 <= index && index <= size()) {
                    V[] newArray = (V[]) java.lang.reflect.Array.newInstance(vs.getClass().getComponentType(), (int) size() + 1);
                    newArray[index] = v;
                    System.arraycopy(vs, 0, newArray, 0, index);
                    System.arraycopy(vs, index, newArray, index + 1, (int) size() - index);
                    return Opt.some(of(newArray));
                }
                return Opt.none();
            }

            @Override
            public Opt<V> getOpt(Integer index) {
                return 0 <= index && index < size() ? Opt.some(vs[index]) : Opt.none();
            }

            @Override
            @SuppressWarnings("unchecked")
            public Opt<Array<V>> removeOpt(Integer index) {
                if (0 <= index && index < size()) {
                    V[] newArray = (V[]) java.lang.reflect.Array.newInstance(vs.getClass().getComponentType(), (int) size() - 1);
                    System.arraycopy(vs, 0, newArray, 0, index);
                    System.arraycopy(vs, index + 1, newArray, index, (int) size() - index - 1);
                    return Opt.some(of(newArray));
                }
                return Opt.none();
            }

            @Override
            public long size() {
                return vs.length;
            }

            @Override
            public String toString() {
                return "Array" + Arrays.toString(vs);
            }
        };
    }

    public static Array<Byte> bytes(byte ... vs) {
        return new Array<Byte>() {

            @Override
            @SuppressWarnings("unchecked")
            public Opt<Array<Byte>> addOpt(Integer index, Byte v) {
                if (0 <= index && index <= size()) {
                    byte[] newArray = new byte[(int) size() + 1];
                    newArray[index] = v;
                    System.arraycopy(vs, 0, newArray, 0, index);
                    System.arraycopy(vs, index, newArray, index + 1, (int) size() - index);
                    return Opt.some(bytes(newArray));
                }
                return Opt.none();
            }

            @Override
            public Opt<Byte> getOpt(Integer index) {
                return 0 <= index && index < size() ? Opt.some(vs[index]) : Opt.none();
            }

            @Override
            @SuppressWarnings("unchecked")
            public Opt<Array<Byte>> removeOpt(Integer index) {
                if (0 <= index && index < size()) {
                    byte[] newArray = new byte[(int) size() - 1];
                    System.arraycopy(vs, 0, newArray, 0, index);
                    System.arraycopy(vs, index + 1, newArray, index, (int) size() - index - 1);
                    return Opt.some(bytes(newArray));
                }
                return Opt.none();
            }

            @Override
            public long size() {
                return vs.length;
            }

            @Override
            public String toString() {
                return "Array" + Arrays.toString(vs);
            }
        };
    }

    public static Array<Integer> ints(int ... vs) {
        return new Array<Integer>() {

            @Override
            @SuppressWarnings("unchecked")
            public Opt<Array<Integer>> addOpt(Integer index, Integer v) {
                if (0 <= index && index <= size()) {
                    int[] newArray = new int[(int) size() + 1];
                    newArray[index] = v;
                    System.arraycopy(vs, 0, newArray, 0, index);
                    System.arraycopy(vs, index, newArray, index + 1, (int) size() - index);
                    return Opt.some(ints(newArray));
                }
                return Opt.none();
            }

            @Override
            public Opt<Integer> getOpt(Integer index) {
                return 0 <= index && index < size() ? Opt.some(vs[index]) : Opt.none();
            }

            @Override
            @SuppressWarnings("unchecked")
            public Opt<Array<Integer>> removeOpt(Integer index) {
                if (0 <= index && index < size()) {
                    int[] newArray = new int[(int) size() - 1];
                    System.arraycopy(vs, 0, newArray, 0, index);
                    System.arraycopy(vs, index + 1, newArray, index, (int) size() - index - 1);
                    return Opt.some(ints(newArray));
                }
                return Opt.none();
            }

            @Override
            public long size() {
                return vs.length;
            }

            @Override
            public String toString() {
                return "Array" + Arrays.toString(vs);
            }
        };
    }

    public static Array<Long> longs(long ... vs) {
        return new Array<Long>() {

            @Override
            @SuppressWarnings("unchecked")
            public Opt<Array<Long>> addOpt(Integer index, Long v) {
                if (0 <= index && index <= size()) {
                    long[] newArray = new long[(int) size() + 1];
                    newArray[index] = v;
                    System.arraycopy(vs, 0, newArray, 0, index);
                    System.arraycopy(vs, index, newArray, index + 1, (int) size() - index);
                    return Opt.some(longs(newArray));
                }
                return Opt.none();
            }

            @Override
            public Opt<Long> getOpt(Integer index) {
                return 0 <= index && index < size() ? Opt.some(vs[index]) : Opt.none();
            }

            @Override
            @SuppressWarnings("unchecked")
            public Opt<Array<Long>> removeOpt(Integer index) {
                if (0 <= index && index < size()) {
                    long[] newArray = new long[(int) size() - 1];
                    System.arraycopy(vs, 0, newArray, 0, index);
                    System.arraycopy(vs, index + 1, newArray, index, (int) size() - index - 1);
                    return Opt.some(longs(newArray));
                }
                return Opt.none();
            }

            @Override
            public long size() {
                return vs.length;
            }

            @Override
            public String toString() {
                return "Array" + Arrays.toString(vs);
            }
        };
    }

    public static Array<Double> doubles(double ... vs) {
        return new Array<Double>() {

            @Override
            @SuppressWarnings("unchecked")
            public Opt<Array<Double>> addOpt(Integer index, Double v) {
                if (0 <= index && index <= size()) {
                    double[] newArray = new double[(int) size() + 1];
                    newArray[index] = v;
                    System.arraycopy(vs, 0, newArray, 0, index);
                    System.arraycopy(vs, index, newArray, index + 1, (int) size() - index);
                    return Opt.some(doubles(newArray));
                }
                return Opt.none();
            }

            @Override
            public Opt<Double> getOpt(Integer index) {
                return 0 <= index && index < size() ? Opt.some(vs[index]) : Opt.none();
            }

            @Override
            @SuppressWarnings("unchecked")
            public Opt<Array<Double>> removeOpt(Integer index) {
                if (0 <= index && index < size()) {
                    double[] newArray = new double[(int) size() - 1];
                    System.arraycopy(vs, 0, newArray, 0, index);
                    System.arraycopy(vs, index + 1, newArray, index, (int) size() - index - 1);
                    return Opt.some(doubles(newArray));
                }
                return Opt.none();
            }

            @Override
            public long size() {
                return vs.length;
            }

            @Override
            public String toString() {
                return "Array" + Arrays.toString(vs);
            }
        };
    }

    public static Array<Character> chars(char ... vs) {
        return new Array<Character>() {

            @Override
            @SuppressWarnings("unchecked")
            public Opt<Array<Character>> addOpt(Integer index, Character v) {
                if (0 <= index && index <= size()) {
                    char[] newArray = new char[(int) size() + 1];
                    newArray[index] = v;
                    System.arraycopy(vs, 0, newArray, 0, index);
                    System.arraycopy(vs, index, newArray, index + 1, (int) size() - index);
                    return Opt.some(chars(newArray));
                }
                return Opt.none();
            }

            @Override
            public Opt<Character> getOpt(Integer index) {
                return 0 <= index && index < size() ? Opt.some(vs[index]) : Opt.none();
            }

            @Override
            @SuppressWarnings("unchecked")
            public Opt<Array<Character>> removeOpt(Integer index) {
                if (0 <= index && index < size()) {
                    char[] newArray = new char[(int) size() - 1];
                    System.arraycopy(vs, 0, newArray, 0, index);
                    System.arraycopy(vs, index + 1, newArray, index, (int) size() - index - 1);
                    return Opt.some(chars(newArray));
                }
                return Opt.none();
            }

            @Override
            public long size() {
                return vs.length;
            }

            @Override
            public String toString() {
                return "Array" + Arrays.toString(vs);
            }
        };
    }

    public static Array<Boolean> booleans(boolean ... vs) {
        return new Array<Boolean>() {

            @Override
            @SuppressWarnings("unchecked")
            public Opt<Array<Boolean>> addOpt(Integer index, Boolean v) {
                if (0 <= index && index <= size()) {
                    boolean[] newArray = new boolean[(int) size() + 1];
                    newArray[index] = v;
                    System.arraycopy(vs, 0, newArray, 0, index);
                    System.arraycopy(vs, index, newArray, index + 1, (int) size() - index);
                    return Opt.some(booleans(newArray));
                }
                return Opt.none();
            }

            @Override
            public Opt<Boolean> getOpt(Integer index) {
                return 0 <= index && index < size() ? Opt.some(vs[index]) : Opt.none();
            }

            @Override
            @SuppressWarnings("unchecked")
            public Opt<Array<Boolean>> removeOpt(Integer index) {
                if (0 <= index && index < size()) {
                    boolean[] newArray = new boolean[(int) size() - 1];
                    System.arraycopy(vs, 0, newArray, 0, index);
                    System.arraycopy(vs, index + 1, newArray, index, (int) size() - index - 1);
                    return Opt.some(booleans(newArray));
                }
                return Opt.none();
            }

            @Override
            public long size() {
                return vs.length;
            }

            @Override
            public String toString() {
                return "Array" + Arrays.toString(vs);
            }
        };
    }

}
