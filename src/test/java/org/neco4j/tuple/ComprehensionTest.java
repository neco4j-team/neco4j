package org.neco4j.tuple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.neco4j.tuple.Comprehension.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Test;
import org.neco4j.function.QuadFunction;
import org.neco4j.function.TriFunction;

public class ComprehensionTest {

    @Test
    public void combine2Test() throws Exception {
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Integer> ints = Arrays.asList(10, 20);
        List<Pair<String, Integer>> expected = Arrays.asList(
                Pair.of("a", 10), Pair.of("a", 20),
                Pair.of("b", 10), Pair.of("b", 20),
                Pair.of("c", 10), Pair.of("c", 20)
        );
        assertEquals(expected, it2list(combine(strings, ints)));
        assertTrue(it2list(combine(strings, Collections.emptyList())).isEmpty());
        assertTrue(it2list(combine(Collections.emptyList(), ints)).isEmpty());
        assertTrue(it2list(combine(Collections.emptyList(), Collections.emptyList())).isEmpty());
    }

    @Test
    public void combine3Test() throws Exception {
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Integer> ints = Arrays.asList(10, 20);
        List<Long> longs = Arrays.asList(1L, 2L);
        List<Triple<String, Integer, Long>> expected = Arrays.asList(
                Triple.of("a", 10, 1L), Triple.of("a", 10, 2L), Triple.of("a", 20, 1L), Triple.of("a", 20, 2L),
                Triple.of("b", 10, 1L), Triple.of("b", 10, 2L), Triple.of("b", 20, 1L), Triple.of("b", 20, 2L),
                Triple.of("c", 10, 1L), Triple.of("c", 10, 2L), Triple.of("c", 20, 1L), Triple.of("c", 20, 2L)
        );
        assertEquals(expected, it2list(combine(strings, ints, longs)));
        assertTrue(it2list(combine(strings, Collections.emptyList(), longs)).isEmpty());
        assertTrue(it2list(combine(Collections.emptyList(), ints, longs)).isEmpty());
        assertTrue(it2list(combine(Collections.emptyList(), Collections.emptyList(), longs)).isEmpty());
        assertTrue(it2list(combine(strings, Collections.emptyList(), Collections.emptyList())).isEmpty());
        assertTrue(it2list(combine(Collections.emptyList(), ints, Collections.emptyList())).isEmpty());
        assertTrue(it2list(combine(Collections.emptyList(), Collections.emptyList(), Collections.emptyList())).isEmpty());
    }

    @Test
    public void combine4Test() throws Exception {
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Integer> ints = Arrays.asList(10, 20);
        List<Long> longs = Arrays.asList(1L, 2L);
        List<String> foobars = Arrays.asList("foo", "bar");
        List<Quad<String, Integer, Long, String>> expected = Arrays.asList(
                Quad.of("a", 10, 1L, "foo"), Quad.of("a", 10, 1L, "bar"),
                Quad.of("a", 10, 2L, "foo"), Quad.of("a", 10, 2L, "bar"),
                Quad.of("a", 20, 1L, "foo"), Quad.of("a", 20, 1L, "bar"),
                Quad.of("a", 20, 2L, "foo"), Quad.of("a", 20, 2L, "bar"),
                Quad.of("b", 10, 1L, "foo"), Quad.of("b", 10, 1L, "bar"),
                Quad.of("b", 10, 2L, "foo"), Quad.of("b", 10, 2L, "bar"),
                Quad.of("b", 20, 1L, "foo"), Quad.of("b", 20, 1L, "bar"),
                Quad.of("b", 20, 2L, "foo"), Quad.of("b", 20, 2L, "bar"),
                Quad.of("c", 10, 1L, "foo"), Quad.of("c", 10, 1L, "bar"),
                Quad.of("c", 10, 2L, "foo"), Quad.of("c", 10, 2L, "bar"),
                Quad.of("c", 20, 1L, "foo"), Quad.of("c", 20, 1L, "bar"),
                Quad.of("c", 20, 2L, "foo"), Quad.of("c", 20, 2L, "bar")
        );
        assertEquals(expected, it2list(combine(strings, ints, longs, foobars)));
        assertTrue(it2list(combine(strings, Collections.emptyList(), longs, foobars)).isEmpty());
        assertTrue(it2list(combine(Collections.emptyList(), ints, longs, foobars)).isEmpty());
        assertTrue(it2list(combine(Collections.emptyList(), Collections.emptyList(), longs, foobars)).isEmpty());
        assertTrue(it2list(combine(strings, Collections.emptyList(), Collections.emptyList(), foobars)).isEmpty());
        assertTrue(it2list(combine(Collections.emptyList(), ints, Collections.emptyList(), foobars)).isEmpty());
        assertTrue(it2list(combine(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), foobars)).isEmpty());
        assertTrue(it2list(combine(strings, Collections.emptyList(), longs, Collections.emptyList())).isEmpty());
        assertTrue(it2list(combine(Collections.emptyList(), ints, longs, Collections.emptyList())).isEmpty());
        assertTrue(it2list(combine(Collections.emptyList(), Collections.emptyList(), longs, Collections.emptyList())).isEmpty());
        assertTrue(it2list(combine(strings, Collections.emptyList(), Collections.emptyList(), Collections.emptyList())).isEmpty());
        assertTrue(it2list(combine(Collections.emptyList(), ints, Collections.emptyList(), Collections.emptyList())).isEmpty());
        assertTrue(it2list(combine(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList())).isEmpty());
    }

    @Test
    public void combineWith2Test() throws Exception {
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Integer> ints = Arrays.asList(10, 20);
        List<String> expected = Arrays.asList("a*10", "a*20", "b*10", "b*20", "c*10", "c*20");
        BiFunction<String, Integer, String> fn = (s, n) -> s + "*" + n;
        assertEquals(expected, it2list(combineWith(strings, ints, fn)));
        assertTrue(it2list(combineWith(strings, Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(combineWith(Collections.emptyList(), ints, fn)).isEmpty());
        assertTrue(it2list(combineWith(Collections.emptyList(), Collections.emptyList(), fn)).isEmpty());
    }

    @Test
    public void combineWith3Test() throws Exception {
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Integer> ints = Arrays.asList(10, 20);
        List<Long> longs = Arrays.asList(1L, 2L);
        List<String> expected = Arrays.asList(
                "a*10*1", "a*10*2", "a*20*1", "a*20*2",
                "b*10*1", "b*10*2", "b*20*1", "b*20*2",
                "c*10*1", "c*10*2", "c*20*1", "c*20*2"
        );
        TriFunction<String, Integer, Long, String> fn = (s, n, l) -> s + "*" + n + "*" + l;
        assertEquals(expected, it2list(combineWith(strings, ints, longs, fn)));
        assertTrue(it2list(combineWith(strings, Collections.emptyList(), longs, fn)).isEmpty());
        assertTrue(it2list(combineWith(Collections.emptyList(), ints, longs, fn)).isEmpty());
        assertTrue(it2list(combineWith(Collections.emptyList(), Collections.emptyList(), longs, fn)).isEmpty());
        assertTrue(it2list(combineWith(strings, Collections.emptyList(), Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(combineWith(Collections.emptyList(), ints, Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(combineWith(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), fn)).isEmpty());
    }

    @Test
    public void combineWith4Test() throws Exception {
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Integer> ints = Arrays.asList(10, 20);
        List<Long> longs = Arrays.asList(1L, 2L);
        List<String> foobars = Arrays.asList("foo", "bar");
        List<String> expected = Arrays.asList(
                "a*10*1*foo", "a*10*1*bar", "a*10*2*foo", "a*10*2*bar",
                "a*20*1*foo", "a*20*1*bar", "a*20*2*foo", "a*20*2*bar",
                "b*10*1*foo", "b*10*1*bar", "b*10*2*foo", "b*10*2*bar",
                "b*20*1*foo", "b*20*1*bar", "b*20*2*foo", "b*20*2*bar",
                "c*10*1*foo", "c*10*1*bar", "c*10*2*foo", "c*10*2*bar",
                "c*20*1*foo", "c*20*1*bar", "c*20*2*foo", "c*20*2*bar"
        );
        QuadFunction<String, Integer, Long, String, String> fn = (s, n, l, f) -> s + "*" + n + "*" + l + "*" + f;
        assertEquals(expected, it2list(combineWith(strings, ints, longs, foobars, fn)));
        assertTrue(it2list(combineWith(strings, Collections.emptyList(), longs, foobars, fn)).isEmpty());
        assertTrue(it2list(combineWith(Collections.emptyList(), ints, longs, foobars, fn)).isEmpty());
        assertTrue(it2list(combineWith(Collections.emptyList(), Collections.emptyList(), longs, foobars, fn)).isEmpty());
        assertTrue(it2list(combineWith(strings, Collections.emptyList(), Collections.emptyList(), foobars, fn)).isEmpty());
        assertTrue(it2list(combineWith(Collections.emptyList(), ints, Collections.emptyList(), foobars, fn)).isEmpty());
        assertTrue(it2list(combineWith(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), foobars, fn)).isEmpty());
        assertTrue(it2list(combineWith(strings, Collections.emptyList(), longs, Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(combineWith(Collections.emptyList(), ints, longs, Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(combineWith(Collections.emptyList(), Collections.emptyList(), longs, Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(combineWith(strings, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(combineWith(Collections.emptyList(), ints, Collections.emptyList(), Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(combineWith(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), fn)).isEmpty());
    }

    @Test
    public void zip2Test() throws Exception {
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Integer> ints = Arrays.asList(10, 20);
        List<Pair<String, Integer>> expected1 = Arrays.asList(Pair.of("a", 10), Pair.of("b", 20));
        List<Pair<String, String>> expected2 = Arrays.asList(Pair.of("a", "a"), Pair.of("b", "b"), Pair.of("c", "c"));

        assertEquals(expected1, it2list(zip(strings, ints)));
        assertEquals(expected2, it2list(zip(strings, strings)));
        assertTrue(it2list(zip(strings, Collections.emptyList())).isEmpty());
        assertTrue(it2list(zip(Collections.emptyList(), ints)).isEmpty());
        assertTrue(it2list(zip(Collections.emptyList(), Collections.emptyList())).isEmpty());
    }

    @Test
    public void zip3Test() throws Exception {
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Integer> ints = Arrays.asList(10, 20);
        List<Long> longs = Arrays.asList(1L, 2L);
        List<Triple<String, Integer, Long>> expected = Arrays.asList(Triple.of("a", 10, 1L), Triple.of("b", 20, 2L));

        assertEquals(expected, it2list(zip(strings, ints, longs)));
        assertTrue(it2list(zip(strings, Collections.emptyList(), longs)).isEmpty());
        assertTrue(it2list(zip(Collections.emptyList(), ints, longs)).isEmpty());
        assertTrue(it2list(zip(Collections.emptyList(), Collections.emptyList(), longs)).isEmpty());
        assertTrue(it2list(zip(strings, Collections.emptyList(), Collections.emptyList())).isEmpty());
        assertTrue(it2list(zip(Collections.emptyList(), ints, Collections.emptyList())).isEmpty());
        assertTrue(it2list(zip(Collections.emptyList(), Collections.emptyList(), Collections.emptyList())).isEmpty());
    }

    @Test
    public void zip4Test() throws Exception {
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Integer> ints = Arrays.asList(10, 20);
        List<Long> longs = Arrays.asList(1L, 2L);
        List<String> foobars = Arrays.asList("foo", "bar");
        List<Quad<String, Integer, Long, String>> expected = Arrays.asList(Quad.of("a", 10, 1L, "foo"), Quad.of("b", 20, 2L, "bar"));

        assertEquals(expected, it2list(zip(strings, ints, longs, foobars)));
        assertTrue(it2list(zip(strings, Collections.emptyList(), longs, foobars)).isEmpty());
        assertTrue(it2list(zip(Collections.emptyList(), ints, longs, foobars)).isEmpty());
        assertTrue(it2list(zip(Collections.emptyList(), Collections.emptyList(), longs, foobars)).isEmpty());
        assertTrue(it2list(zip(strings, Collections.emptyList(), Collections.emptyList(), foobars)).isEmpty());
        assertTrue(it2list(zip(Collections.emptyList(), ints, Collections.emptyList(), foobars)).isEmpty());
        assertTrue(it2list(zip(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), foobars)).isEmpty());
        assertTrue(it2list(zip(strings, Collections.emptyList(), longs, Collections.emptyList())).isEmpty());
        assertTrue(it2list(zip(Collections.emptyList(), ints, longs, Collections.emptyList())).isEmpty());
        assertTrue(it2list(zip(Collections.emptyList(), Collections.emptyList(), longs, Collections.emptyList())).isEmpty());
        assertTrue(it2list(zip(strings, Collections.emptyList(), Collections.emptyList(), Collections.emptyList())).isEmpty());
        assertTrue(it2list(zip(Collections.emptyList(), ints, Collections.emptyList(), Collections.emptyList())).isEmpty());
        assertTrue(it2list(zip(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList())).isEmpty());
    }

    @Test
    public void zipMapTest() throws Exception {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        List<Pair<String, Integer>> expected = Arrays.asList(Pair.of("one", 1), Pair.of("two", 2), Pair.of("three", 3));

        assertEquals(expected, it2list(zip(map)));
    }

    @Test
    public void zipWith2Test() throws Exception {
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Integer> ints = Arrays.asList(10, 20);
        List<String> expected = Arrays.asList("a*10", "b*20");
        BiFunction<String, Integer, String> fn = (s, n) -> s + "*" + n;
        assertEquals(expected, it2list(zipWith(strings, ints, fn)));
        assertTrue(it2list(zipWith(strings, Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(zipWith(Collections.emptyList(), ints, fn)).isEmpty());
        assertTrue(it2list(zipWith(Collections.emptyList(), Collections.emptyList(), fn)).isEmpty());
    }

    @Test
    public void zipWith3Test() throws Exception {
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Integer> ints = Arrays.asList(10, 20);
        List<Long> longs = Arrays.asList(1L, 2L);
        List<String> expected = Arrays.asList("a*10*1", "b*20*2");
        TriFunction<String, Integer, Long, String> fn = (s, n, l) -> s + "*" + n + "*" + l;
        assertEquals(expected, it2list(zipWith(strings, ints, longs, fn)));
        assertTrue(it2list(zipWith(strings, Collections.emptyList(), longs, fn)).isEmpty());
        assertTrue(it2list(zipWith(Collections.emptyList(), ints, longs, fn)).isEmpty());
        assertTrue(it2list(zipWith(Collections.emptyList(), Collections.emptyList(), longs, fn)).isEmpty());
        assertTrue(it2list(zipWith(strings, Collections.emptyList(), Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(zipWith(Collections.emptyList(), ints, Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(zipWith(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), fn)).isEmpty());
    }

    @Test
    public void zipWith4Test() throws Exception {
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Integer> ints = Arrays.asList(10, 20);
        List<Long> longs = Arrays.asList(1L, 2L);
        List<String> foobars = Arrays.asList("foo", "bar");
        List<String> expected = Arrays.asList("a*10*1*foo", "b*20*2*bar");
        QuadFunction<String, Integer, Long, String, String> fn = (s, n, l, f) -> s + "*" + n + "*" + l + "*" + f;
        assertEquals(expected, it2list(zipWith(strings, ints, longs, foobars, fn)));
        assertTrue(it2list(zipWith(strings, Collections.emptyList(), longs, foobars, fn)).isEmpty());
        assertTrue(it2list(zipWith(Collections.emptyList(), ints, longs, foobars, fn)).isEmpty());
        assertTrue(it2list(zipWith(Collections.emptyList(), Collections.emptyList(), longs, foobars, fn)).isEmpty());
        assertTrue(it2list(zipWith(strings, Collections.emptyList(), Collections.emptyList(), foobars, fn)).isEmpty());
        assertTrue(it2list(zipWith(Collections.emptyList(), ints, Collections.emptyList(), foobars, fn)).isEmpty());
        assertTrue(it2list(zipWith(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), foobars, fn)).isEmpty());
        assertTrue(it2list(zipWith(strings, Collections.emptyList(), longs, Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(zipWith(Collections.emptyList(), ints, longs, Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(zipWith(Collections.emptyList(), Collections.emptyList(), longs, Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(zipWith(strings, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(zipWith(Collections.emptyList(), ints, Collections.emptyList(), Collections.emptyList(), fn)).isEmpty());
        assertTrue(it2list(zipWith(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), fn)).isEmpty());
    }

    @Test
    public void zipWithMapTest() throws Exception {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        List<String> expected = Arrays.asList("one=1", "two=2", "three=3");

        assertEquals(expected, it2list(zipWith(map, (k, v) -> k + "=" + v)));
    }


    @Test
    public void flattenTest() throws Exception {
        List<List<String>> nestedStrings = Arrays.asList(
                Arrays.asList("a", "b", "c"),
                Arrays.asList(),
                Arrays.asList("d", "e")
        );
        List<String> expected = Arrays.asList("a", "b", "c", "d", "e");
        assertEquals(expected, it2list(flatten(nestedStrings)));
        assertTrue(it2list(flatten(Collections.emptyList())).isEmpty());
    }

    @Test
    public void flatMaptest() throws Exception {
        List<String> strings = Arrays.asList("a,b,c", "", "d,e");
        List<String> expected = Arrays.asList("a", "b", "c", "d", "e");
        Function<String, List<String>> fn = s -> s.isEmpty()
                ? Collections.emptyList() //hack because of stupid String.split behavior
                : Arrays.asList(s.split(","));
        assertEquals(expected, it2list(flatMap(strings, fn)));
        assertTrue(it2list(flatMap(Collections.<String>emptyList(), fn)).isEmpty());
    }

    @Test
    public void mapTest() {
        List<String> strings = Arrays.asList("a", "bb", "ccccc");
        List<Integer> expected = Arrays.asList(1, 2, 5);
        assertEquals(expected, it2list(map(strings, String::length)));
        assertTrue(it2list(map(Collections.emptyList(), String::length)).isEmpty());
    }

    @Test
    public void filterTest() {
        List<Integer> ints = Arrays.asList(1, 2, 2, 3, 5, 6);
        List<Integer> expected = Arrays.asList(2, 2, 6);
        assertEquals(expected, it2list(filter(ints, n -> n % 2 == 0)));
        assertTrue(it2list(map(Collections.emptyList(), String::length)).isEmpty());
    }

    @Test
    public void arrayTest() {
        List<Integer> expected = Arrays.asList(1, 2, 3);
        assertEquals(expected, it2list(array(1, 2, 3)));
        assertTrue(it2list(array()).isEmpty());
    }

    @Test
    public void optionalTest() {
        List<String> expected = Arrays.asList("foo");
        assertEquals(expected, it2list(optional(Optional.of("foo"))));
        assertTrue(it2list(optional(Optional.empty())).isEmpty());
    }

    @Test
    public void indexedTest() throws Exception {
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Pair<String, Integer>> expected = Arrays.asList(
                Pair.of("a", 0), Pair.of("b", 1), Pair.of("c", 2)
        );
        assertEquals(expected, it2list(indexed(strings)));
        assertTrue(it2list(indexed(Collections.emptyList())).isEmpty());
    }

    @Test
    public void limitTest() throws Exception {
        List<String> strings = Arrays.asList("a", "b", "c");
        List<String> expected = Arrays.asList("a", "b");
        assertTrue(it2list(limit(strings, 0)).isEmpty());
        assertEquals(expected, it2list(limit(strings, 2)));
        assertEquals(strings, it2list(limit(strings, 3)));
        assertEquals(strings, it2list(limit(strings, 300)));
    }

    private static <A> List<A> it2list(Iterable<A> it) {
        List<A> list = new ArrayList<>();
        for (A a : it) {
            list.add(a);
        }
        return list;
    }

}