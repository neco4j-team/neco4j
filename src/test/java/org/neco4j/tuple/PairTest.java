package org.neco4j.tuple;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class PairTest {

    @Test
    public void testGet1() {
        Pair<String, Integer> pair = Pair.of("foo", 42);
        assertThat(pair.get1()).isEqualTo("foo");
    }

    @Test
    public void testGet2() {
        Pair<String, Integer> pair = Pair.of("foo", 42);
        assertThat(pair.get2()).isEqualTo(42);
    }

    @Test
    public void testWith1() {
        Pair<String, Integer> pair = Pair.of("foo", 42);
        Pair<Character, Integer> pairWith = pair.with1('c');
        assertPairEquals(pairWith, 'c', 42);
    }

    @Test
    public void testWith2() {
        Pair<String, Integer> pair = Pair.of("foo", 42);
        Pair<String, Character> pairWith = pair.with2('c');
        assertPairEquals(pairWith, "foo", 'c');
    }

    @Test
    public void testMap1() {
        Pair<String, Integer> pair = Pair.of("foo", 42);
        Pair<Integer, Integer> pairMap = pair.map1(String::length);
        assertPairEquals(pairMap, 3, 42);
    }

    @Test
    public void testMap2() {
        Pair<String, Integer> pair = Pair.of("foo", 42);
        Pair<String, String> pairMap = pair.map2(n -> "number " + n);
        assertPairEquals(pairMap, "foo", "number 42");
    }

    @Test
    public void testBimap() {
        Pair<String, Integer> pair = Pair.of("foo", 42);
        Pair<Integer, String> pairMap = pair.bimap(String::length, n -> "number " + n);
        assertPairEquals(pairMap, 3, "number 42");
    }

    @Test
    public void testCollapse() {
        Pair<String, Integer> pair = Pair.of("foobar", 3);
        String collapsed = pair.collapse(String::substring);
        assertThat(collapsed).isEqualTo("bar");
    }

    @Test
    public void testSwap() {
        Pair<String, Integer> pair = Pair.of("foo", 42);
        Pair<Integer, String> pairSwap = pair.swap();
        assertPairEquals(pairSwap, 42, "foo");
    }

    @Test
    public void testToString() {
        Pair<String, Integer> pair = Pair.of("foo", 42);
        assertThat(pair.toString()).isEqualTo("(foo,42)");
    }

    @Test
    public void testHashCode() {
        Pair<String, Integer> pair1 = Pair.of("foo", 4711);
        Pair<String, Integer> pair2 = Pair.of("foo", 4711);
        assertThat(pair1.hashCode() == pair2.hashCode()).isTrue();
    }

    @Test
    public void testEquals() {
        Pair<String, Integer> pair1 = Pair.of("foo", 4711);
        Pair<String, Integer> pair2 = Pair.of("foo", 4711);
        Pair<String, Integer> pair3 = Pair.of("bar", 4711);
        Pair<String, Integer> pair4 = Pair.of("foo", 42);
        assertThat(pair1.equals(pair2)).isTrue();
        assertThat(pair1.equals(pair3)).isFalse();
        assertThat(pair1.equals(pair4)).isFalse();
        assertThat(pair3.equals(pair4)).isFalse();
    }

    private static <A, B> void assertPairEquals(Pair<A, B> pair, A a, B b) {
        assertThat(pair.get1()).isEqualTo(a);
        assertThat(pair.get2()).isEqualTo(b);
    }
}
