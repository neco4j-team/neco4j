package org.neco4j.collect;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class LazyListTest {

    @Test
    public void testConsHeadTail() throws Exception {
        LazyList<String> list = LazyList.<String>of("bar").plusAll("foo");
        assertEquals("foo", list.head());
        assertEquals("bar", list.tail().head());
        assertTrue(list.tail().tail().isEmpty());
    }

    @Test
    public void testConsHeadTailSupplier() throws Exception {
        LazyList<String> list = LazyList.cons("foo", () -> LazyList.of("bar"));
        assertEquals("foo", list.head());
        assertEquals("bar", list.tail().head());
        assertTrue(list.tail().tail().isEmpty());

        //test laziness
        LazyList<String> lazyList = LazyList.cons("foo", () -> {
            throw new RuntimeException();
        });
        assertEquals("foo", lazyList.head());

        //test memoization
        int[] count = {0};
        LazyList<String> memoList = LazyList.cons("foo", () -> {
            count[0]++;
            return LazyList.of("bar");
        });
        assertEquals("bar", memoList.tail().head());
        assertEquals("bar", memoList.tail().head());
        assertEquals("bar", memoList.tail().head());
        assertEquals(1, count[0]);
    }

    @Test
    public void testConsHeadSupplierTail() throws Exception {
        LazyList<String> list = LazyList.<String>cons(() -> "foo", LazyList.of("bar"));
        assertEquals("foo", list.head());
        assertEquals("bar", list.tail().head());
        assertTrue(list.tail().tail().isEmpty());

        //test laziness
        LazyList<String> lazyList = LazyList.<String>cons(() -> {
            throw new RuntimeException();
        }, LazyList.of("bar"));
        assertEquals("bar", lazyList.tail().head());

        //test memoization
        int[] count = {0};
        LazyList<String> memoList = LazyList.<String>cons(() -> {
            count[0]++;
            return "foo";
        }, LazyList.of("bar"));
        assertEquals("foo", memoList.head());
        assertEquals("foo", memoList.head());
        assertEquals("foo", memoList.head());
        assertEquals(1, count[0]);

    }

    @Test
    public void testConsHeadSupplierTailSupplier() throws Exception {
        LazyList<String> list = LazyList.<String>cons(() -> "foo", () -> LazyList.of("bar"));
        assertEquals("foo", list.head());
        assertEquals("bar", list.tail().head());
        assertTrue(list.tail().tail().isEmpty());

        //test laziness
        LazyList<String> lazyTailList = LazyList.<String>cons(() -> "foo", () -> {
            throw new RuntimeException();
        });
        assertEquals("foo", lazyTailList.head());
        LazyList<String> lazyHeadList = LazyList.<String>cons(() -> {
            throw new RuntimeException();
        }, () -> LazyList.of("bar"));
        assertEquals("bar", lazyHeadList.tail().head());

        //test memoization

        int[] headCount = {0};
        LazyList<String> memoHeadList = LazyList.<String>cons(() -> {
            headCount[0]++;
            return "foo";
        }, LazyList.of("bar"));
        assertEquals("foo", memoHeadList.head());
        assertEquals("foo", memoHeadList.head());
        assertEquals("foo", memoHeadList.head());
        assertEquals(1, headCount[0]);

        int[] tailCount = {0};
        LazyList<String> memoTailList = LazyList.<String>cons(() -> "foo", () -> {
            tailCount[0]++;
            return LazyList.of("bar");
        });
        assertEquals("bar", memoTailList.tail().head());
        assertEquals("bar", memoTailList.tail().head());
        assertEquals("bar", memoTailList.tail().head());
        assertEquals(1, tailCount[0]);
    }

    @Test
    public void testOfVarArgs() throws Exception {
        LazyList<String> list0 = LazyList.<String>of();
        assertTrue(list0.isEmpty());

        LazyList<String> list3 = LazyList.of("foo", "bar", "baz");
        assertEquals("foo", list3.head());
        assertEquals("bar", list3.tail().head());
        assertEquals("baz", list3.tail().tail().head());
        assertTrue(list3.tail().tail().tail().isEmpty());
    }

    @Test
    public void testEmpty() throws Exception {
        LazyList<String> list0 = LazyList.<String>empty();
        assertTrue(list0.isEmpty());
    }

    @Test
    public void testHead() throws Exception {
        LazyList<String> list1 = LazyList.of("foo");
        assertEquals("foo", list1.head());
        LazyList<String> list2 = LazyList.of("foo", "bar");
        assertEquals("foo", list2.head());
    }

    @Test(expected = NoSuchElementException.class)
    public void testHeadException() throws Exception {
        LazyList<String> list = LazyList.<String>empty();
        list.head();
    }

    @Test
    public void testTail() throws Exception {
        LazyList<String> list1 = LazyList.of("foo");
        assertTrue(list1.tail().isEmpty());
        LazyList<String> list2 = LazyList.of("foo", "bar");
        assertEquals("bar", list2.tail().head());
    }

    @Test(expected = NoSuchElementException.class)
    public void testTailException() throws Exception {
        LazyList<String> list = LazyList.<String>empty();
        list.tail();
    }

    @Test
    public void testLast() throws Exception {
        LazyList<String> list1 = LazyList.of("foo");
        assertEquals("foo", list1.last());
        LazyList<String> list2 = LazyList.of("foo", "bar", "baz");
        assertEquals("baz", list2.last());
    }

    @Test
    public void testInit() throws Exception {
        LazyList<String> list1 = LazyList.of("foo");
        assertTrue(list1.init().isEmpty());
        LazyList<String> list2 = LazyList.of("foo", "bar");
        assertEquals("foo", list2.init().head());
        assertTrue(list2.init().tail().isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testInitException() throws Exception {
        LazyList<String> list = LazyList.<String>empty();
        list.init();
    }

    @Test
    public void testInitLaziness() throws Exception {
        LazyList<String> list = LazyList.cons("foo", () -> LazyList.<String>cons("bar", () -> {
            throw new RuntimeException();
        }));
        list.init().head();
    }

    @Test(expected = NoSuchElementException.class)
    public void testLastException() throws Exception {
        LazyList<String> list = LazyList.<String>empty();
        list.last();
    }

    @Test
    public void testIsEmpty() throws Exception {
        LazyList<String> list0 = LazyList.<String>empty();
        assertTrue(list0.isEmpty());
        LazyList<String> list2 = LazyList.of("foo", "bar");
        assertFalse(list2.isEmpty());
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(0, LazyList.<String>empty().size());
        assertEquals(3, LazyList.<String>of("foo", "bar", "baz").size());

        //test laziness
        LazyList<String> errors = LazyList.<String>cons(
                () -> {
                    throw new RuntimeException();
                },
                () -> LazyList.<String>cons(() -> {
                    throw new RuntimeException();
                }, LazyList.<String>empty()));
        assertEquals(2, errors.size());
    }

    @Test
    public void testInits() throws Exception {
        LazyList<List<String>> emptyInits = LazyList.<String>empty().inits();
        assertTrue(emptyInits.get(0).isEmpty());
        LazyList<List<String>> inits = LazyList.of("foo", "bar", "baz").inits();
        assertEquals(4, inits.size());
        assertTrue(inits.get(0).isEmpty());
        assertEquals(inits.get(1).foldLeft1((s, t) -> s + "#" + t), "foo");
        assertEquals(inits.get(2).foldLeft1((s, t) -> s + "#" + t), "foo#bar");
        assertEquals(inits.get(3).foldLeft1((s, t) -> s + "#" + t), "foo#bar#baz");
    }

    @Test
    public void testTails() throws Exception {
        LazyList<List<String>> emptyInits = LazyList.<String>empty().tails();
        assertTrue(emptyInits.get(0).isEmpty());
        LazyList<List<String>> inits = LazyList.of("foo", "bar", "baz").tails();
        assertEquals(4, inits.size());
        assertEquals(inits.get(0).foldLeft1((s, t) -> s + "#" + t), "foo#bar#baz");
        assertEquals(inits.get(1).foldLeft1((s, t) -> s + "#" + t), "bar#baz");
        assertEquals(inits.get(2).foldLeft1((s, t) -> s + "#" + t), "baz");
        assertTrue(inits.get(3).isEmpty());
    }

    @Test
    public void testMap() throws Exception {

    }

    @Test
    public void testFlatMap() throws Exception {

    }

    @Test
    public void testDropWhile() throws Exception {

    }

    @Test
    public void testReverse() throws Exception {

    }

    @Test
    public void testTake() throws Exception {

    }

    @Test
    public void testDrop() throws Exception {

    }

    @Test
    public void testFilter() throws Exception {

    }

    @Test
    public void testScanLeft() throws Exception {

    }

    @Test
    public void testScanLeft1() throws Exception {

    }

    @Test
    public void testIterator() throws Exception {

    }

    @Test
    public void testToString() throws Exception {

    }
}