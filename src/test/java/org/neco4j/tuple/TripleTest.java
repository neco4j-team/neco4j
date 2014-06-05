package org.neco4j.tuple;

import org.junit.Test;

import static org.junit.Assert.*;

public class TripleTest {

    @Test
    public void testGet1() throws Exception {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, Math.PI);
        assertEquals("foo", triple.get1());
    }

    @Test
    public void testGet2() throws Exception {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, Math.PI);
        assertEquals(Integer.valueOf(42), triple.get2());
    }

    @Test
    public void testGet3() throws Exception {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, Math.PI);
        assertEquals(Double.valueOf(Math.PI), triple.get3());
    }    

    @Test
    public void testWith1() throws Exception {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, Math.PI);
        Triple<Character, Integer, Double> tripleWith = triple.with1('c');
        assertEquals(Character.valueOf('c'), tripleWith.get1());
        assertEquals(Integer.valueOf(42), tripleWith.get2());
        assertEquals(Double.valueOf(Math.PI), triple.get3());
    }

    @Test
    public void testWith2() throws Exception {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, Math.PI);
        Triple<String, Character, Double> tripleWith = triple.with2('c');
        assertEquals("foo", tripleWith.get1());
        assertEquals(Character.valueOf('c'), tripleWith.get2());
        assertEquals(Double.valueOf(Math.PI), triple.get3());
    }

    @Test
    public void testWith3() throws Exception {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, Math.PI);
        Triple<String, Integer, Character> tripleWith = triple.with3('c');
        assertEquals("foo", tripleWith.get1());
        assertEquals(Integer.valueOf(42), tripleWith.get2());
        assertEquals(Character.valueOf('c'), tripleWith.get3());
    }

    @Test
    public void testMap1() throws Exception {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, Math.PI);
        Triple<Integer, Integer, Double> tripleMap = triple.map1(String::length);
        assertEquals(Integer.valueOf(3), tripleMap.get1());
        assertEquals(Integer.valueOf(42), tripleMap.get2());
        assertEquals(Double.valueOf(Math.PI), tripleMap.get3());
    }

    @Test
    public void testMap2() throws Exception {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, Math.PI);
        Triple<String, String, Double> tripleMap = triple.map2(n -> "number " + n);
        assertEquals("foo", tripleMap.get1());
        assertEquals("number 42", tripleMap.get2());
        assertEquals(Double.valueOf(Math.PI), tripleMap.get3());
    }

    @Test
    public void testMap3() throws Exception {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, Math.PI);
        Triple<String, Integer, Double> tripleMap = triple.map3(d -> -d);
        assertEquals("foo", tripleMap.get1());
        assertEquals(Integer.valueOf(42), tripleMap.get2());
        assertEquals(Double.valueOf(-Math.PI), tripleMap.get3());
    }

    @Test
    public void testTrimap() throws Exception {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, Math.PI);
        Triple<Integer, String, Double> tripleMap = triple.trimap(
                s -> s.length(), n -> "number " + n, d -> -d);
        assertEquals(Integer.valueOf(3), tripleMap.get1());
        assertEquals("number 42", tripleMap.get2());
        assertEquals(Double.valueOf(-Math.PI), tripleMap.get3());
    }

    @Test
    public void testFold() throws Exception {
        Triple<String, Integer, Double> triple = Triple.of("foobar", 3, 0.5);
        String folded = triple.fold((s, n, d) -> s.substring(n) + d);
        assertEquals("bar0.5", folded);
    }

    @Test
    public void testTestAnd() throws Exception {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, Math.PI);
        assertTrue(triple.testAnd(s -> s.startsWith("f"), n -> n % 2 == 0, d -> d > 3));
        assertFalse(triple.testAnd(s -> s.startsWith("f"), n -> n % 2 == 1, d -> d > 3));
        assertFalse(triple.testAnd(s -> s.startsWith("b"), n -> n % 2 == 0, d -> d > 3));
        assertFalse(triple.testAnd(s -> s.startsWith("b"), n -> n % 2 == 1, d -> d > 3));
        assertFalse(triple.testAnd(s -> s.startsWith("f"), n -> n % 2 == 0, d -> d < 3));
        assertFalse(triple.testAnd(s -> s.startsWith("f"), n -> n % 2 == 1, d -> d < 3));
        assertFalse(triple.testAnd(s -> s.startsWith("b"), n -> n % 2 == 0, d -> d < 3));
        assertFalse(triple.testAnd(s -> s.startsWith("b"), n -> n % 2 == 1, d -> d < 3));
    }

    @Test
    public void testTestOr() throws Exception {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, Math.PI);
        assertTrue(triple.testOr(s -> s.startsWith("f"), n -> n % 2 == 0, d -> d > 3));
        assertTrue(triple.testOr(s -> s.startsWith("f"), n -> n % 2 == 1, d -> d > 3));
        assertTrue(triple.testOr(s -> s.startsWith("b"), n -> n % 2 == 0, d -> d > 3));
        assertTrue(triple.testOr(s -> s.startsWith("b"), n -> n % 2 == 1, d -> d > 3));
        assertTrue(triple.testOr(s -> s.startsWith("f"), n -> n % 2 == 0, d -> d < 3));
        assertTrue(triple.testOr(s -> s.startsWith("f"), n -> n % 2 == 1, d -> d < 3));
        assertTrue(triple.testOr(s -> s.startsWith("b"), n -> n % 2 == 0, d -> d < 3));
        assertFalse(triple.testOr(s -> s.startsWith("b"), n -> n % 2 == 1, d -> d < 3));
    }

    @Test
    public void testToString() throws Exception {
        Triple<String, Integer, Double> triple = Triple.of("foo", 42, 0.5);
        assertEquals("(foo,42,0.5)", triple.toString());
    }

    @Test
    public void testHashCode() throws Exception {
        Triple<String, Integer, Double> triple1 = Triple.of("foo", 4711, Math.PI);
        Triple<String, Integer, Double> triple2 = Triple.of("foo", 4711, Math.PI);
        assertTrue(triple1.hashCode() == triple2.hashCode());
    }

    @Test
    public void testEquals() throws Exception {
        Triple<String, Integer, Double> triple1 = Triple.of("foo", 4711, Math.PI);
        Triple<String, Integer, Double> triple2 = Triple.of("foo", 4711, Math.PI);
        Triple<String, Integer, Double> triple3 = Triple.of("bar", 4711, Math.PI);
        Triple<String, Integer, Double> triple4 = Triple.of("foo", 42, Math.PI);
        Triple<String, Integer, Double> triple5 = Triple.of("foo", 4711, Math.E);
        assertTrue(triple1.equals(triple2));
        assertFalse(triple1.equals(triple3));
        assertFalse(triple1.equals(triple4));
        assertFalse(triple3.equals(triple4));
        assertFalse(triple1.equals(triple5));
        assertFalse(triple2.equals(triple5));
        assertFalse(triple3.equals(triple5));
        assertFalse(triple4.equals(triple5));
    }
}
