package org.neco4j.tuple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QuadTest {

    @Test
    public void testGet1() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, Math.PI, 1L);
        assertEquals("foo", quad.get1());
    }

    @Test
    public void testGet2() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, Math.PI, 1L);
        assertEquals(Integer.valueOf(42), quad.get2());
    }

    @Test
    public void testGet3() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, Math.PI, 1L);
        assertEquals(Double.valueOf(Math.PI), quad.get3());
    }

    @Test
    public void testGet4() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, Math.PI, 1L);
        assertEquals(Long.valueOf(1L), quad.get4());
    }

    @Test
    public void testWith1() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, Math.PI, 1L);
        Quad<Character, Integer, Double, Long> quadWith = quad.with1('c');
        assertEquals(Character.valueOf('c'), quadWith.get1());
        assertEquals(Integer.valueOf(42), quadWith.get2());
        assertEquals(Double.valueOf(Math.PI), quad.get3());
        assertEquals(Long.valueOf(1L), quad.get4());
    }

    @Test
    public void testWith2() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, Math.PI, 1L);
        Quad<String, Character, Double, Long> quadWith = quad.with2('c');
        assertEquals("foo", quadWith.get1());
        assertEquals(Character.valueOf('c'), quadWith.get2());
        assertEquals(Double.valueOf(Math.PI), quad.get3());
        assertEquals(Long.valueOf(1L), quad.get4());
    }

    @Test
    public void testWith3() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, Math.PI, 1L);
        Quad<String, Integer, Character, Long> quadWith = quad.with3('c');
        assertEquals("foo", quadWith.get1());
        assertEquals(Integer.valueOf(42), quadWith.get2());
        assertEquals(Character.valueOf('c'), quadWith.get3());
        assertEquals(Long.valueOf(1L), quad.get4());
    }

    @Test
    public void testWith4() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, Math.PI, 1L);
        Quad<String, Integer, Double, Character> quadWith = quad.with4('c');
        assertEquals("foo", quadWith.get1());
        assertEquals(Integer.valueOf(42), quadWith.get2());
        assertEquals(Double.valueOf(Math.PI), quad.get3());
        assertEquals(Character.valueOf('c'), quadWith.get4());
    }

    @Test
    public void testMap1() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, Math.PI, 1L);
        Quad<Integer, Integer, Double, Long> quadMap = quad.map1(String::length);
        assertEquals(Integer.valueOf(3), quadMap.get1());
        assertEquals(Integer.valueOf(42), quadMap.get2());
        assertEquals(Double.valueOf(Math.PI), quadMap.get3());
        assertEquals(Long.valueOf(1L), quad.get4());
    }

    @Test
    public void testMap2() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, Math.PI, 1L);
        Quad<String, String, Double, Long> quadMap = quad.map2(n -> "number " + n);
        assertEquals("foo", quadMap.get1());
        assertEquals("number 42", quadMap.get2());
        assertEquals(Double.valueOf(Math.PI), quadMap.get3());
        assertEquals(Long.valueOf(1L), quad.get4());
    }

    @Test
    public void testMap3() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, Math.PI, 1L);
        Quad<String, Integer, Double, Long> quadMap = quad.map3(d -> -d);
        assertEquals("foo", quadMap.get1());
        assertEquals(Integer.valueOf(42), quadMap.get2());
        assertEquals(Double.valueOf(-Math.PI), quadMap.get3());
        assertEquals(Long.valueOf(1L), quad.get4());
    }

    @Test
    public void testMap4() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, Math.PI, 9L);
        Quad<String, Integer, Double, Double> quadMap = quad.map4(n -> Math.sqrt(n));
        assertEquals("foo", quadMap.get1());
        assertEquals(Integer.valueOf(42), quadMap.get2());
        assertEquals(Double.valueOf(Math.PI), quadMap.get3());
        assertEquals(Double.valueOf(3.0), quadMap.get4());
    }

    @Test
    public void testQuadmap() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, Math.PI, 9L);
        Quad<Integer, String, Double, Double> quadMap = quad.quadmap(
                s -> s.length(), n -> "number " + n, d -> -d, n -> Math.sqrt(n));
        assertEquals(Integer.valueOf(3), quadMap.get1());
        assertEquals("number 42", quadMap.get2());
        assertEquals(Double.valueOf(-Math.PI), quadMap.get3());
        assertEquals(Double.valueOf(3.0), quadMap.get4());
    }

    @Test
    public void testFold() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foobar", 3, 0.5, 9L);
        String folded = quad.fold((s, n, d, e) -> s.substring(n) + (d + Math.sqrt(e)));
        assertEquals("bar3.5", folded);
    }

    @Test
    public void testTestAnd() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, Math.PI, 1L);
        assertTrue(quad.testAnd(s -> s.startsWith("f"), n -> n % 2 == 0, d -> d > 3, e -> e > 0));
        assertFalse(quad.testAnd(s -> s.startsWith("f"), n -> n % 2 == 1, d -> d > 3, e -> e > 0));
        assertFalse(quad.testAnd(s -> s.startsWith("b"), n -> n % 2 == 0, d -> d > 3, e -> e > 0));
        assertFalse(quad.testAnd(s -> s.startsWith("b"), n -> n % 2 == 1, d -> d > 3, e -> e > 0));
        assertFalse(quad.testAnd(s -> s.startsWith("f"), n -> n % 2 == 0, d -> d < 3, e -> e > 0));
        assertFalse(quad.testAnd(s -> s.startsWith("f"), n -> n % 2 == 1, d -> d < 3, e -> e > 0));
        assertFalse(quad.testAnd(s -> s.startsWith("b"), n -> n % 2 == 0, d -> d < 3, e -> e > 0));
        assertFalse(quad.testAnd(s -> s.startsWith("b"), n -> n % 2 == 1, d -> d < 3, e -> e > 0));
        assertFalse(quad.testAnd(s -> s.startsWith("f"), n -> n % 2 == 0, d -> d > 3, e -> e < 0));
        assertFalse(quad.testAnd(s -> s.startsWith("f"), n -> n % 2 == 1, d -> d > 3, e -> e < 0));
        assertFalse(quad.testAnd(s -> s.startsWith("b"), n -> n % 2 == 0, d -> d > 3, e -> e < 0));
        assertFalse(quad.testAnd(s -> s.startsWith("b"), n -> n % 2 == 1, d -> d > 3, e -> e < 0));
        assertFalse(quad.testAnd(s -> s.startsWith("f"), n -> n % 2 == 0, d -> d < 3, e -> e < 0));
        assertFalse(quad.testAnd(s -> s.startsWith("f"), n -> n % 2 == 1, d -> d < 3, e -> e < 0));
        assertFalse(quad.testAnd(s -> s.startsWith("b"), n -> n % 2 == 0, d -> d < 3, e -> e < 0));
        assertFalse(quad.testAnd(s -> s.startsWith("b"), n -> n % 2 == 1, d -> d < 3, e -> e < 0));
    }

    @Test
    public void testTestOr() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, Math.PI, 1L);
        assertTrue(quad.testOr(s -> s.startsWith("f"), n -> n % 2 == 0, d -> d > 3, e -> e > 0));
        assertTrue(quad.testOr(s -> s.startsWith("f"), n -> n % 2 == 1, d -> d > 3, e -> e > 0));
        assertTrue(quad.testOr(s -> s.startsWith("b"), n -> n % 2 == 0, d -> d > 3, e -> e > 0));
        assertTrue(quad.testOr(s -> s.startsWith("b"), n -> n % 2 == 1, d -> d > 3, e -> e > 0));
        assertTrue(quad.testOr(s -> s.startsWith("f"), n -> n % 2 == 0, d -> d < 3, e -> e > 0));
        assertTrue(quad.testOr(s -> s.startsWith("f"), n -> n % 2 == 1, d -> d < 3, e -> e > 0));
        assertTrue(quad.testOr(s -> s.startsWith("b"), n -> n % 2 == 0, d -> d < 3, e -> e > 0));
        assertTrue(quad.testOr(s -> s.startsWith("b"), n -> n % 2 == 1, d -> d < 3, e -> e > 0));
        assertTrue(quad.testOr(s -> s.startsWith("f"), n -> n % 2 == 0, d -> d > 3, e -> e < 0));
        assertTrue(quad.testOr(s -> s.startsWith("f"), n -> n % 2 == 1, d -> d > 3, e -> e < 0));
        assertTrue(quad.testOr(s -> s.startsWith("b"), n -> n % 2 == 0, d -> d > 3, e -> e < 0));
        assertTrue(quad.testOr(s -> s.startsWith("b"), n -> n % 2 == 1, d -> d > 3, e -> e < 0));
        assertTrue(quad.testOr(s -> s.startsWith("f"), n -> n % 2 == 0, d -> d < 3, e -> e < 0));
        assertTrue(quad.testOr(s -> s.startsWith("f"), n -> n % 2 == 1, d -> d < 3, e -> e < 0));
        assertTrue(quad.testOr(s -> s.startsWith("b"), n -> n % 2 == 0, d -> d < 3, e -> e < 0));
        assertFalse(quad.testOr(s -> s.startsWith("b"), n -> n % 2 == 1, d -> d < 3, e -> e < 0));
    }

    @Test
    public void testToString() throws Exception {
        Quad<String, Integer, Double, Long> quad = Quad.of("foo", 42, 0.5, 9L);
        assertEquals("(foo,42,0.5,9)", quad.toString());
    }

    @Test
    public void testHashCode() throws Exception {
        Quad<String, Integer, Double, Long> quad1 = Quad.of("foo", 4711, Math.PI, 9L);
        Quad<String, Integer, Double, Long> quad2 = Quad.of("foo", 4711, Math.PI, 9L);
        assertTrue(quad1.hashCode() == quad2.hashCode());
    }

    @Test
    public void testEquals() throws Exception {
        Quad<String, Integer, Double, Long> quad1 = Quad.of("foo", 4711, Math.PI, 9L);
        Quad<String, Integer, Double, Long> quad2 = Quad.of("foo", 4711, Math.PI, 9L);
        Quad<String, Integer, Double, Long> quad3 = Quad.of("bar", 4711, Math.PI, 9L);
        Quad<String, Integer, Double, Long> quad4 = Quad.of("foo", 42, Math.PI, 9L);
        Quad<String, Integer, Double, Long> quad5 = Quad.of("foo", 4711, Math.E, 9L);
        Quad<String, Integer, Double, Long> quad6 = Quad.of("foo", 4711, Math.PI, 11L);
        assertTrue(quad1.equals(quad2));
        assertFalse(quad1.equals(quad3));
        assertFalse(quad1.equals(quad4));
        assertFalse(quad3.equals(quad4));
        assertFalse(quad1.equals(quad5));
        assertFalse(quad2.equals(quad5));
        assertFalse(quad3.equals(quad5));
        assertFalse(quad4.equals(quad5));
        assertFalse(quad1.equals(quad6));
    }
}
