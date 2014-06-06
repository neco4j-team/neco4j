package org.neco4j.collect;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;

public class ListTest {
    private final List<Character> sut = List.of('a', 'b', 'c', 'd');

    @Test
    public void testHeadOnCons() throws Exception {
        assertEquals(Character.valueOf('a'), sut.head());
    }

    @Test(expected = NoSuchElementException.class)
    public void testHeadOnNil() throws Exception {
        List.empty().head();
    }

    @Test
    public void testHeadOpt() throws Exception {
        assertEquals(Optional.of('a'), sut.headOpt());
        assertEquals(Optional.empty(), List.empty().headOpt());
    }

    @Test
    public void testTail() throws Exception {
        assertEquals(List.of('b', 'c', 'd'), sut.tail());
        assertEquals(List.empty(), List.empty().tail());
    }

    @Test
    public void testLastOnCons() throws Exception {
        assertEquals(Character.valueOf('d'), sut.last());
    }

    @Test(expected = NoSuchElementException.class)
    public void testLastOnNil() throws Exception {
        List.empty().last();
    }

    @Test
    public void testLastOpt() throws Exception {
        assertEquals(Optional.of('d'), sut.lastOpt());
        assertEquals(Optional.empty(), List.empty().lastOpt());
    }

    @Test
    public void testInit() throws Exception {
        assertEquals(List.of('a', 'b', 'c'), sut.init());
        assertEquals(List.empty(), List.empty().init());
    }

    @Test
    public void testGetOnCons() throws Exception {
        assertEquals(Character.valueOf('a'), sut.get(0));
        assertEquals(Character.valueOf('b'), sut.get(1));
        assertEquals(Character.valueOf('d'), sut.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOnConsWithNegativeIndex() throws Exception {
        sut.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOnConsOutOfBounds() throws Exception {
        sut.get(4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOnNil() throws Exception {
        List.empty().get(0);
    }

    @Test
    public void testGetOptOnCons() throws Exception {
        assertEquals(Optional.of('a'), sut.getOpt(0));
        assertEquals(Optional.of('b'), sut.getOpt(1));
        assertEquals(Optional.of('d'), sut.getOpt(3));
    }

    @Test
    public void testGetOptOnConsWithNegativeIndex() throws Exception {
        assertEquals(Optional.<Character>empty(), sut.getOpt(-1));
    }

    @Test
    public void testGetOptOnConsOutOfBounds() throws Exception {
        assertEquals(Optional.<Character>empty(), sut.getOpt(4));
    }

    @Test
    public void testGetOptOnNil() throws Exception {
        assertEquals(Optional.empty(), List.empty().getOpt(0));
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(4, sut.size());
        assertEquals(0, List.empty().size());
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertFalse(sut.isEmpty());
        assertTrue(List.empty().isEmpty());
    }

    @Test
    public void testWithOnCons() throws Exception {
        assertEquals(List.of('a', 'b', 'x', 'd'), sut.with(2, 'x'));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWithOnConsOutOfBounds() throws Exception {
        sut.with(5, 'x');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWithOnConsWithNegativeIndex() throws Exception {
        sut.with(-1, 'x');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWithOnNil() throws Exception {
        List.empty().with(0, 0);
    }

    @Test
    public void testConcat() throws Exception {
        assertEquals(List.of('a', 'b', 'c', 'd', 'e', 'f'), sut.concat(List.of('e', 'f')));
        assertEquals(sut, List.<Character>empty().concat(sut));
        assertEquals(sut, sut.concat(List.empty()));
        assertEquals(List.empty(), List.empty().concat(List.empty()));
    }

    @Test
    public void testTake() throws Exception {
        assertEquals(List.of('a', 'b'), sut.take(2));
    }

    @Test
    public void testTakeMoreThanInList() throws Exception {
        assertEquals(List.of('a', 'b', 'c', 'd'), sut.take(10));
        assertEquals(List.empty(), List.empty().take(1));
    }

    @Test
    public void testTakeWhile() throws Exception {
        List<Integer> intList = List.of(5, 8, 6, 13, 9);
        assertEquals(List.of(5, 8, 6), intList.takeWhile(i -> i < 10));
    }

    @Test
    public void testDrop() throws Exception {
        assertEquals(List.of('c', 'd'), sut.drop(2));
    }

    @Test
    public void testDropMoreThanInList() throws Exception {
        assertEquals(List.<Character>empty(), sut.drop(10));
        assertEquals(List.empty(), List.empty().drop(1));
    }

    @Test
    public void testDropWhile() throws Exception {
        List<Integer> intList = List.of(5, 8, 6, 13, 9);
        assertEquals(List.of(13, 9), intList.dropWhile(i -> i < 10));
    }

    @Test
    public void testReverse() throws Exception {
        assertEquals(List.of('d', 'c', 'b', 'a'), sut.reverse());
        assertEquals(List.empty(), List.empty());
    }

    @Test
    public void testMap() throws Exception {
        List<Integer> intList = List.of(5, 8, 6, 13, 9);
        assertEquals(List.of(6, 9, 7, 14, 10), intList.map(i -> i + 1));
    }

    @Test
    public void testFlatMap() throws Exception {
        List<Integer> intList = List.of(2, 4, 5);
        assertEquals(List.of(1, 2, 3, 3, 4, 5, 4, 5, 6), intList.flatMap(x -> List.of(x - 1, x, x + 1)));
    }

    @Test
    public void testFilter() throws Exception {
        List<Integer> intList = List.of(5, 8, 6, 13, 9);
        assertEquals(List.of(5, 6), intList.filter(i -> i < 7));
    }

    @Test
    public void testFoldLeft() throws Exception {
        List<Integer> intList = List.of(5, 8, 6, 13, 9);
        // (((((((((2 + 1) * 5) + 1) * 8) + 1) * 6) + 1) * 13) + 1) * 9 = 90.684
        assertEquals(Integer.valueOf(90684), intList.foldLeft(2, (a, b) -> (a + 1) * b));
    }

    @Test
    public void testFoldRight() throws Exception {
        List<Integer> intList = List.of(5, 8, 6, 13, 9);
        // (5 + 1) * ((8 + 1) * ((6 + 1) * ((13 + 1) * ((9 + 1) * 2)))) = 105.840
        assertEquals(Integer.valueOf(105840), intList.foldRight((a, b) -> (a + 1) * b, 2));
    }

    @Test
    public void testIterator() throws Exception {
        Iterator<Character> it = sut.iterator();
        assertEquals(Character.valueOf('a'), it.next());
        assertEquals(Character.valueOf('b'), it.next());
        assertEquals(Character.valueOf('c'), it.next());
        assertEquals(Character.valueOf('d'), it.next());
        assertFalse(it.hasNext());
        assertFalse(List.empty().iterator().hasNext());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("[a,b,c,d]", sut.toString());
        assertEquals("[]", List.empty().toString());
    }
}
