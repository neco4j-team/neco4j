package org.neco4j.tuple;

import org.junit.Test;

import static java.lang.Math.PI;
import static org.assertj.core.api.Assertions.assertThat;


public class TripleTest {

    @Test
    public void testGet1() {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, PI);
        assertThat(triple.get1()).isEqualTo("foo");
    }

    @Test
    public void testGet2() {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, PI);
        assertThat(triple.get2()).isEqualTo(42);
    }

    @Test
    public void testGet3() {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, PI);
        assertThat(triple.get3()).isEqualTo(PI);
    }

    @Test
    public void testWith1() {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, PI);
        Triple<Character, Integer, Double> tripleWith = triple.with1('c');
        assertTripleEquals(tripleWith, 'c', 42, PI);
    }

    @Test
    public void testWith2() {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, PI);
        Triple<String, Character, Double> tripleWith = triple.with2('c');
        assertTripleEquals(tripleWith, "foo", 'c', PI);
    }

    @Test
    public void testWith3() {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, PI);
        Triple<String, Integer, Character> tripleWith = triple.with3('c');
        assertTripleEquals(tripleWith, "foo", 42, 'c');
    }

    @Test
    public void testMap1() {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, PI);
        Triple<Integer, Integer, Double> tripleMap = triple.map1(String::length);
        assertTripleEquals(tripleMap, 3, 42, PI);
    }

    @Test
    public void testMap2() {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, PI);
        Triple<String, String, Double> tripleMap = triple.map2(n -> "number " + n);
        assertTripleEquals(tripleMap, "foo", "number 42", PI);
    }

    @Test
    public void testMap3() {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, PI);
        Triple<String, Integer, Double> tripleMap = triple.map3(d -> -d);
        assertTripleEquals(tripleMap, "foo", 42, -PI);
    }

    @Test
    public void testTrimap() {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, PI);
        Triple<Integer, String, Double> tripleMap = triple.trimap(
            s -> s.length(), n -> "number " + n, d -> -d);
        assertTripleEquals(tripleMap, 3, "number 42", -PI);
    }

    @Test
    public void testCollapse() {
        Triple<String, Integer, Double> triple = Triple.of("foobar", 3, 0.5);
        String collapsed = triple.collapse((s, n, d) -> s.substring(n) + d);
        assertThat(collapsed).isEqualTo("bar0.5");
    }

    @Test
    public void testToString() {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, 0.5);
        assertThat(triple.toString()).isEqualTo("(foo,42,0.5)");
    }

    @Test
    public void testHashCode() {
        Triple<String, Integer, Double> triple1 = Triple.of("foo", 4711, PI);
        Triple<String, Integer, Double> triple2 = Triple.of("foo", 4711, PI);
        assertThat(triple1.hashCode() == triple2.hashCode()).isTrue();
    }

    @Test
    public void testEquals() {
        Triple<String, Integer, Double> triple1 = Triple.of("foo", 4711, PI);
        Triple<String, Integer, Double> triple2 = Triple.of("foo", 4711, PI);
        Triple<String, Integer, Double> triple3 = Triple.of("bar", 4711, PI);
        Triple<String, Integer, Double> triple4 = Triple.of("foo", 42, PI);
        Triple<String, Integer, Double> triple5 = Triple.of("foo", 4711, Math.E);
        assertThat(triple1.equals(triple2)).isTrue();
        assertThat(triple1.equals(triple3)).isFalse();
        assertThat(triple1.equals(triple4)).isFalse();
        assertThat(triple3.equals(triple4)).isFalse();
        assertThat(triple1.equals(triple5)).isFalse();
        assertThat(triple2.equals(triple5)).isFalse();
        assertThat(triple3.equals(triple5)).isFalse();
        assertThat(triple4.equals(triple5)).isFalse();
    }

    private static <A, B, C> void assertTripleEquals(Triple<A, B, C> triple, A a, B b, C c) {
        assertThat(triple.get1()).isEqualTo(a);
        assertThat(triple.get2()).isEqualTo(b);
        assertThat(triple.get3()).isEqualTo(c);
    }
}
