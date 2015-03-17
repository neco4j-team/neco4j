package org.neco4j.monoid;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import org.neco4j.tuple.Pair;
import org.neco4j.tuple.Quad;
import org.neco4j.tuple.Triple;

public final class Monoids {

    private Monoids() {
        throw new UnsupportedOperationException("do not instantiate");
    }

    /**
     * A monoid with null as identity element
     *
     * @param operator the given BinaryOperator
     * @param <A>      the element type of the monoid
     * @return the monoid with null as identity
     */
    public static <A> Monoid<A> nullMonoid(BinaryOperator<A> operator) {
        return new Monoid<A>() {

            @Override
            public A identity() {
                return null;
            }

            @Override
            public A apply(A a1, A a2) {
                return a1 == null ? a2 : a2 == null ? a1 : operator.apply(a1, a2);
            }
        };
    }

    /**
     * A monoid with a swapped operator
     *
     * @param monoid the given monoid
     * @param <A>    the element type of the monoid
     * @return the dual monoid
     */
    public static <A> Monoid<A> dualMonoid(Monoid<A> monoid) {
        return new Monoid<A>() {

            @Override
            public A identity() {
                return monoid.identity();
            }

            @Override
            public A apply(A x, A y) {
                return monoid.apply(y, x);
            }
        };
    }

    /**
     * A monoid for Strings based on concatination
     *
     * @return the String monoid
     */
    public static Monoid<String> stringMonoid = new Monoid<String>() {

        @Override
        public String identity() {
            return "";
        }

        @Override
        public String apply(String x, String y) {
            return (x == null ? "" : x) + (y == null ? "" : y);
        }
    };

    /**
     * A monoid for Iterables based on concatination
     *
     * @param <A> the element type of the monoid
     * @return the Iterable monoid
     */
    public static <A> Monoid<Iterable<A>> iterableMonoid() {
        return new Monoid<Iterable<A>>() {
            @Override
            public Iterable<A> identity() {
                return Collections.emptyList();
            }

            @Override
            public Iterable<A> apply(Iterable<A> x, Iterable<A> y) {
                return () -> new Iterator<A>() {
                    private Iterator<A> xIterator = x.iterator();
                    private Iterator<A> yIterator = y.iterator();

                    @Override
                    public boolean hasNext() {
                        return xIterator.hasNext() || yIterator.hasNext();
                    }

                    @Override
                    public A next() {
                        return xIterator.hasNext() ? xIterator.next() : yIterator.next();
                    }
                };
            }
        };
    }

    public static <A> Monoid<List<A>> listMonoid() {
        return new Monoid<List<A>>() {
            @Override
            public List<A> identity() {
                return Collections.emptyList();
            }

            @Override
            public List<A> apply(List<A> x, List<A> y) {
                List<A> result = new ArrayList<>(x.size() + y.size());
                result.addAll(x);
                result.addAll(y);
                return result;
            }
        };
    }

    public static <A> Monoid<Set<A>> setMonoid() {
        return new Monoid<Set<A>>() {
            @Override
            public Set<A> identity() {
                return Collections.emptySet();
            }

            @Override
            public Set<A> apply(Set<A> x, Set<A> y) {
                Set<A> result = new HashSet<>(x);
                result.addAll(y);
                return result;
            }
        };
    }

    public static <K, V> Monoid<Map<K, V>> mapMonoid(BinaryOperator<V> op) {
        return new Monoid<Map<K, V>>() {
            @Override
            public Map<K, V> identity() {
                return Collections.emptyMap();
            }

            @Override
            public Map<K, V> apply(Map<K, V> x, Map<K, V> y) {
                Map<K, V> result = new HashMap<>(x);
                for (Map.Entry<K, V> entry : y.entrySet()) {
                    result.merge(entry.getKey(), entry.getValue(), op);
                }
                return result;
            }
        };
    }


    public static Monoid<Integer> integerSum = new Monoid<Integer>() {

        @Override
        public Integer identity() {
            return 0;
        }

        @Override
        public Integer apply(Integer x, Integer y) {
            return x + y;
        }
    };

    public static Monoid<Long> longSum = new Monoid<Long>() {

        @Override
        public Long identity() {
            return 0L;
        }

        @Override
        public Long apply(Long x, Long y) {
            return x + y;
        }
    };

    public static Monoid<BigInteger> bigIntegerSum = new Monoid<BigInteger>() {

        @Override
        public BigInteger identity() {
            return BigInteger.ZERO;
        }

        @Override
        public BigInteger apply(BigInteger x, BigInteger y) {
            return x.add(y);
        }
    };


    public static Monoid<Double> doubleSum = new Monoid<Double>() {

        @Override
        public Double identity() {
            return 0.0;
        }

        @Override
        public Double apply(Double x, Double y) {
            return x + y;
        }
    };

    public static Monoid<Integer> integerProduct = new Monoid<Integer>() {

        @Override
        public Integer identity() {
            return 1;
        }

        @Override
        public Integer apply(Integer x, Integer y) {
            return x * y;
        }
    };

    public static Monoid<Long> longProduct = new Monoid<Long>() {

        @Override
        public Long identity() {
            return 1L;
        }

        @Override
        public Long apply(Long x, Long y) {
            return x * y;
        }
    };

    public static Monoid<BigInteger> bigIntegerProduct = new Monoid<BigInteger>() {

        @Override
        public BigInteger identity() {
            return BigInteger.ONE;
        }

        @Override
        public BigInteger apply(BigInteger x, BigInteger y) {
            return x.multiply(y);
        }
    };

    public static Monoid<Double> doubleProduct = new Monoid<Double>() {

        @Override
        public Double identity() {
            return 1.0;
        }

        @Override
        public Double apply(Double x, Double y) {
            return x * y;
        }
    };

    public static Monoid<Boolean> booleanAnd = new Monoid<Boolean>() {
        @Override
        public Boolean identity() {
            return true;
        }

        @Override
        public Boolean apply(Boolean x, Boolean y) {
            return x && y;
        }
    };

    public static Monoid<Integer> integerAnd = new Monoid<Integer>() {
        @Override
        public Integer identity() {
            return -1;
        }

        @Override
        public Integer apply(Integer x, Integer y) {
            return x & y;
        }
    };

    public static Monoid<Long> longAnd = new Monoid<Long>() {
        @Override
        public Long identity() {
            return -1L;
        }

        @Override
        public Long apply(Long x, Long y) {
            return x & y;
        }
    };

    public static Monoid<BigInteger> bigIntegerAnd = new Monoid<BigInteger>() {
        @Override
        public BigInteger identity() {
            return BigInteger.ONE.negate();
        }

        @Override
        public BigInteger apply(BigInteger x, BigInteger y) {
            return x.and(y);
        }
    };

    public static Monoid<Boolean> booleanOr = new Monoid<Boolean>() {
        @Override
        public Boolean identity() {
            return false;
        }

        @Override
        public Boolean apply(Boolean x, Boolean y) {
            return x || y;
        }
    };

    public static Monoid<Integer> integerOr = new Monoid<Integer>() {
        @Override
        public Integer identity() {
            return 0;
        }

        @Override
        public Integer apply(Integer x, Integer y) {
            return x | y;
        }
    };

    public static Monoid<Long> longOr = new Monoid<Long>() {
        @Override
        public Long identity() {
            return 0L;
        }

        @Override
        public Long apply(Long x, Long y) {
            return x & y;
        }
    };

    public static Monoid<BigInteger> bigIntegerOr = new Monoid<BigInteger>() {
        @Override
        public BigInteger identity() {
            return BigInteger.ZERO;
        }

        @Override
        public BigInteger apply(BigInteger x, BigInteger y) {
            return x.or(y);
        }
    };

    /**
     * A monoid for UnaryOperators based on function composition
     *
     * @param <A> the element type of the monoid
     * @return the UnaryOperator monoid
     */
    public static <A> Monoid<UnaryOperator<A>> endoMonoid() {
        return new Monoid<UnaryOperator<A>>() {
            @Override
            public UnaryOperator<A> identity() {
                return UnaryOperator.identity();
            }

            @Override
            public UnaryOperator<A> apply(UnaryOperator<A> x, UnaryOperator<A> y) {
                return a -> y.apply(x.apply(a));
            }
        };
    }

    /**
     * A monoid for Optional values
     *
     * @param <A> the element type of the monoid
     * @return the Optional monoid
     */
    public static <A> Monoid<Optional<A>> optionalMonoid(BinaryOperator<A> operator) {
        return new Monoid<Optional<A>>() {
            @Override
            public Optional<A> identity() {
                return Optional.empty();
            }

            @Override
            public Optional<A> apply(Optional<A> x, Optional<A> y) {
                return x.flatMap(xValue -> y.map(yValue -> operator.apply(xValue, yValue)));
            }
        };
    }

    public static <A, B> Monoid<Pair<A, B>> pairMonoid(
            Monoid<A> monoidA, Monoid<B> monoidB) {
        return new Monoid<Pair<A, B>>() {
            @Override
            public Pair<A, B> identity() {
                return Pair.of(
                        monoidA.identity(),
                        monoidB.identity());
            }

            @Override
            public Pair<A, B> apply(Pair<A, B> x, Pair<A, B> y) {
                return Pair.of(
                        monoidA.apply(x.get1(), y.get1()),
                        monoidB.apply(x.get2(), y.get2()));
            }
        };
    }

    public static <A, B, C> Monoid<Triple<A, B, C>> tripleMonoid(
            Monoid<A> monoidA, Monoid<B> monoidB, Monoid<C> monoidC) {
        return new Monoid<Triple<A, B, C>>() {
            @Override
            public Triple<A, B, C> identity() {
                return Triple.of(
                        monoidA.identity(),
                        monoidB.identity(),
                        monoidC.identity());
            }

            @Override
            public Triple<A, B, C> apply(Triple<A, B, C> x, Triple<A, B, C> y) {
                return Triple.of(
                        monoidA.apply(x.get1(), y.get1()),
                        monoidB.apply(x.get2(), y.get2()),
                        monoidC.apply(x.get3(), y.get3()));
            }
        };
    }

    public static <A, B, C, D> Monoid<Quad<A, B, C, D>> quadMonoid(
            Monoid<A> monoidA, Monoid<B> monoidB, Monoid<C> monoidC, Monoid<D> monoidD) {
        return new Monoid<Quad<A, B, C, D>>() {
            @Override
            public Quad<A, B, C, D> identity() {
                return Quad.of(
                        monoidA.identity(),
                        monoidB.identity(),
                        monoidC.identity(),
                        monoidD.identity());
            }

            @Override
            public Quad<A, B, C, D> apply(Quad<A, B, C, D> x, Quad<A, B, C, D> y) {
                return Quad.of(
                        monoidA.apply(x.get1(), y.get1()),
                        monoidB.apply(x.get2(), y.get2()),
                        monoidC.apply(x.get3(), y.get3()),
                        monoidD.apply(x.get4(), y.get4()));
            }
        };
    }

}
