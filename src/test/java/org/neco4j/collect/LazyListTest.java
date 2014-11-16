package org.neco4j.collect;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class LazyListTest {

    @Test
    public void testOfHeadTail() throws Exception {
        LazyList<String> list = LazyList.of("foo", LazyList.<String>of("bar"));
        assertEquals("foo", list.head());
        assertEquals("bar", list.tail().head());
        assertTrue(list.tail().tail().isEmpty());
    }

    @Test
    public void testOfHeadTailSupplier() throws Exception {
        LazyList<String> list = LazyList.of("foo", () -> LazyList.of("bar"));
        assertEquals("foo", list.head());
        assertEquals("bar", list.tail().head());
        assertTrue(list.tail().tail().isEmpty());

        //test laziness
        LazyList<String> lazyList = LazyList.of("foo", () -> { throw new RuntimeException(); });
        assertEquals("foo", lazyList.head());

        //test memoization
        int[] count = {0};
        LazyList<String> memoList = LazyList.of("foo", () -> { count[0] ++;  return LazyList.of("bar"); });
        assertEquals("bar", memoList.tail().head());
        assertEquals("bar", memoList.tail().head());
        assertEquals("bar", memoList.tail().head());
        assertEquals(1, count[0]);
    }

    @Test
    public void testOfHeadSupplierTail() throws Exception {
        LazyList<String> list = LazyList.<String>of(() -> "foo", LazyList.of("bar"));
        assertEquals("foo", list.head());
        assertEquals("bar", list.tail().head());
        assertTrue(list.tail().tail().isEmpty());

        //test laziness
        LazyList<String> lazyList = LazyList.<String>of(() -> { throw new RuntimeException(); }, LazyList.of("bar"));
        assertEquals("bar", lazyList.tail().head());

        //test memoization
        int[] count = {0};
        LazyList<String> memoList = LazyList.<String>of(() -> { count[0] ++;  return "foo"; }, LazyList.of("bar"));
        assertEquals("foo", memoList.head());
        assertEquals("foo", memoList.head());
        assertEquals("foo", memoList.head());
        assertEquals(1, count[0]);

    }

    @Test
    public void testOfHeadSupplierTailSupplier() throws Exception {
        LazyList<String> list = LazyList.<String>of(() -> "foo", () -> LazyList.of("bar"));
        assertEquals("foo", list.head());
        assertEquals("bar", list.tail().head());
        assertTrue(list.tail().tail().isEmpty());

        //test laziness
        LazyList<String> lazyTailList = LazyList.<String>of(() -> "foo", () -> { throw new RuntimeException(); });
        assertEquals("foo", lazyTailList.head());
        LazyList<String> lazyHeadList = LazyList.<String>of(() -> { throw new RuntimeException(); }, () -> LazyList.of("bar"));
        assertEquals("bar", lazyHeadList.tail().head());

        //test memoization

        int[] headCount = {0};
        LazyList<String> memoHeadList = LazyList.<String>of(() -> { headCount[0] ++;  return "foo"; }, LazyList.of("bar"));
        assertEquals("foo", memoHeadList.head());
        assertEquals("foo", memoHeadList.head());
        assertEquals("foo", memoHeadList.head());
        assertEquals(1, headCount[0]);

        int[] tailCount = {0};
        LazyList<String> memoTailList = LazyList.<String>of(() -> "foo", () -> { tailCount[0] ++;  return LazyList.of("bar"); });
        assertEquals("bar", memoTailList.tail().head());
        assertEquals("bar", memoTailList.tail().head());
        assertEquals("bar", memoTailList.tail().head());
        assertEquals(1, tailCount[0]);
    }

    @Test
    public void testOfVarArgs() throws Exception {
        LazyList<String> list0 = LazyList.<String>of();
        assertTrue(list0.isEmpty());

        LazyList<String> list3 = LazyList.of("foo","bar","baz");
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
        LazyList<String> list2 = LazyList.of("foo","bar");
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
        LazyList<String> list2 = LazyList.of("foo","bar");
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
        LazyList<String> list2 = LazyList.of("foo","bar","baz");
        assertEquals("baz", list2.last());
    }

    @Test
    public void testInit() throws Exception {
        LazyList<String> list1 = LazyList.of("foo");
        assertTrue(list1.init().isEmpty());
        LazyList<String> list2 = LazyList.of("foo","bar");
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
        LazyList<String> list = LazyList.of("foo", () -> LazyList.<String>of("bar", () -> {throw new RuntimeException();} ));
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
        LazyList<String> list2 = LazyList.of("foo","bar");
        assertFalse(list2.isEmpty());
    }

    @Test
    public void testSize() throws Exception {

    }

    @Test
    public void testInits() throws Exception {

    }

    @Test
    public void testTails() throws Exception {

    }

    @Test
    public void testMap() throws Exception {

    }

    @Test
    public void testFlatMap() throws Exception {

    }

    @Test
    public void testFlatten() throws Exception {
        LazyList<LazyList<String>> list = LazyList.of(LazyList.of("a", "b"), LazyList.<String>empty(), LazyList.of("c"));
        LazyList<String> flattenedList = LazyList.flatten(list);
        assertEquals(3, flattenedList.size());
        assertEquals("a", flattenedList.head());
        assertEquals("b", flattenedList.tail().head());
        assertEquals("c", flattenedList.tail().tail().head());
    }

    @Test
    public void testDropWhile() throws Exception {

    }

    @Test
    public void testDropUntil() throws Exception {

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