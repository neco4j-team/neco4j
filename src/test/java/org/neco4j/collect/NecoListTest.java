package org.neco4j.collect;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class NecoListTest {
    private final NecoList<Character> sut = NecoList.of('a', 'b', 'c', 'd');

    @Test
    public void testHeadOnCons() throws Exception {
        assertThat(sut.head(), is('a'));
    }

    @Test(expected = NoSuchElementException.class)
    public void testHeadOnNil() throws Exception {
        NecoList.empty().head();
    }

    @Test
    public void testHeadOpt() throws Exception {
        assertThat(sut.headOpt(), is(Optional.of('a')));
        assertThat(NecoList.empty().headOpt(), is(Optional.empty()));
    }

    @Test
    public void testTailOnCons() throws Exception {
        assertThat(sut.tail(), is(NecoList.of('b', 'c', 'd')));
    }

    @Test(expected = NoSuchElementException.class)
    public void testTailOnNil() throws Exception {
        NecoList.empty().tail();
    }

    @Test
    public void testTailOpt() throws Exception {
        assertThat(sut.tailOpt(), is(Optional.of(NecoList.of('b', 'c', 'd'))));
        assertThat(NecoList.<Character>empty().tailOpt(), is(Optional.<NecoList<Character>>empty()));
    }

    @Test
    public void testLastOnCons() throws Exception {
        assertThat(sut.last(), is('d'));
    }

    @Test(expected = NoSuchElementException.class)
    public void testLastOnNil() throws Exception {
        NecoList.empty().last();
    }

    @Test
    public void testLastOpt() throws Exception {
        assertThat(sut.lastOpt(), is(Optional.of('d')));
        assertThat(NecoList.empty().lastOpt(), is(Optional.empty()));
    }

    @Test
    public void testInitOnCons() throws Exception {
        assertThat(sut.init(), is(NecoList.of('a', 'b', 'c')));
    }

    @Test(expected = NoSuchElementException.class)
    public void testInitOnNil() throws Exception {
        NecoList.empty().init();
    }

    @Test
    public void testInitOpt() throws Exception {
        assertThat(sut.initOpt(), is(Optional.of(NecoList.of('a', 'b', 'c'))));
        assertThat(NecoList.<Character>empty().initOpt(), is(Optional.<NecoList<Character>>empty()));
    }

    @Test
    public void testGetOnCons() throws Exception {
        assertThat(sut.get(0), is('a'));
        assertThat(sut.get(1), is('b'));
        assertThat(sut.get(3), is('d'));
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
        NecoList.empty().get(0);
    }

    @Test
    public void testGetOptOnCons() throws Exception {
        assertThat(sut.getOpt(0), is(Optional.of('a')));
        assertThat(sut.getOpt(1), is(Optional.of('b')));
        assertThat(sut.getOpt(3), is(Optional.of('d')));
    }

    @Test
    public void testGetOptOnConsWithNegativeIndex() throws Exception {
        assertThat(sut.getOpt(-1), is(Optional.<Character>empty()));
    }

    @Test
    public void testGetOptOnConsOutOfBounds() throws Exception {
        assertThat(sut.getOpt(4), is(Optional.<Character>empty()));
    }

    @Test
    public void testGetOptOnNil() throws Exception {
        assertThat(NecoList.empty().getOpt(0), is(Optional.empty()));
    }

    @Test
    public void testSize() throws Exception {
        assertThat(sut.size(), is(4));
        assertThat(NecoList.empty().size(), is(0));
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertFalse(sut.isEmpty());
        assertTrue(NecoList.empty().isEmpty());
    }

    @Test
    public void testWithOnCons() throws Exception {
        assertThat(sut.with(2, 'x'), is(NecoList.of('a', 'b', 'x', 'd')));
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
        NecoList.empty().with(0, 0);
    }

    @Test
    public void testConcat() throws Exception {
        assertThat(sut.concat(NecoList.of('e', 'f')), is(NecoList.of('a', 'b', 'c', 'd', 'e', 'f')));
        assertThat(NecoList.<Character>empty().concat(sut), is(sut));
        assertThat(sut.concat(NecoList.empty()), is(sut));
        assertThat(NecoList.empty().concat(NecoList.empty()), is(NecoList.empty()));
    }

    @Test
    public void testTake() throws Exception {
        assertThat(sut.take(2), is(NecoList.of('a', 'b')));
    }

    @Test
    public void testTakeMoreThanInList() throws Exception {
        assertThat(sut.take(10), is(NecoList.of('a', 'b', 'c', 'd')));
        assertThat(NecoList.empty().take(1), is(NecoList.empty()));
    }

    @Test
    public void testTakeWhile() throws Exception {
        NecoList<Integer> intList = NecoList.of(5, 8, 6, 13, 9);
        assertThat(intList.takeWhile(i -> i < 10), is(NecoList.of(5, 8, 6)));
    }

    @Test
    public void testDrop() throws Exception {
        assertThat(sut.drop(2), is(NecoList.of('c', 'd')));
    }

    @Test
    public void testDropMoreThanInList() throws Exception {
        assertThat(sut.drop(10), is(NecoList.<Character>empty()));
        assertThat(NecoList.empty().drop(1), is(NecoList.empty()));
    }

    @Test
    public void testDropWhile() throws Exception {
        NecoList<Integer> intList = NecoList.of(5, 8, 6, 13, 9);
        assertThat(intList.dropWhile(i -> i < 10), is(NecoList.of(13, 9)));
    }

    @Test
    public void testReverse() throws Exception {
        assertThat(sut.reverse(), is(NecoList.of('d', 'c', 'b', 'a')));
        assertThat(NecoList.empty().reverse(), is(NecoList.empty()));
    }

    @Test
    public void testMap() throws Exception {
        NecoList<Integer> intList = NecoList.of(5, 8, 6, 13, 9);
        assertThat(intList.map(i -> i + 1), is(NecoList.of(6, 9, 7, 14, 10)));
    }

    @Test
    public void testFlatMap() throws Exception {
        NecoList<Integer> intList = NecoList.of(2, 4, 5);
        assertThat(intList.flatMap(x -> NecoList.<Integer>of(x - 1, x, x + 1)), is(NecoList.of(1, 2, 3, 3, 4, 5, 4, 5, 6)));
    }

    @Test
    public void testFilter() throws Exception {
        NecoList<Integer> intList = NecoList.of(5, 8, 6, 13, 9);
        assertThat(intList.filter(i -> i < 7), is(NecoList.of(5, 6)));
    }

    @Test
    public void testAll() throws Exception {
        NecoList<Integer> intList = NecoList.of(5, 8, 6, 13, 9);
        assertTrue(intList.all(i -> i >= 5 && i <= 13));
        assertFalse(intList.all(i -> i % 2 == 0));
    }

    @Test
    public void testAny() throws Exception {
        NecoList<Integer> intList = NecoList.of(5, 8, 6, 13, 9);
        assertFalse(intList.any(i -> i < 5));
        assertTrue(intList.any(i -> i % 2 == 0));
    }

    @Test
    public void testFoldLeft() throws Exception {
        NecoList<Integer> intList = NecoList.of(5, 8, 6, 13, 9);
        // (((((((((2 + 1) * 5) + 1) * 8) + 1) * 6) + 1) * 13) + 1) * 9 = 90.684
        assertThat(intList.foldLeft(2, (a, b) -> (a + 1) * b), is(90684));
    }

    @Test
    public void testFoldRight() throws Exception {
        NecoList<Integer> intList = NecoList.of(5, 8, 6, 13, 9);
        // (5 + 1) * ((8 + 1) * ((6 + 1) * ((13 + 1) * ((9 + 1) * 2)))) = 105.840
        assertThat(intList.foldRight((a, b) -> (a + 1) * b, 2), is(105840));
    }

    @Test
    public void testIterator() throws Exception {
        Iterator<Character> it = sut.iterator();
        assertThat(it.next(), is('a'));
        assertThat(it.next(), is('b'));
        assertThat(it.next(), is('c'));
        assertThat(it.next(), is('d'));
        assertFalse(it.hasNext());
        assertFalse(NecoList.empty().iterator().hasNext());
    }

    @Test
    public void testToString() throws Exception {
        assertThat(sut.toString(), is("[a,b,c,d]"));
        assertThat(NecoList.empty().toString(), is("[]"));
    }

    @Test
    public void testEquals() throws Exception {
        assertTrue(sut.equals(NecoList.of('a', 'b', 'c', 'd')));
        assertFalse(sut.equals(NecoList.of('a', 'b', 'c')));
        assertFalse(NecoList.of('a', 'b', 'c').equals(sut));
        assertFalse(NecoList.empty().equals(sut));
        assertFalse(sut.equals(NecoList.empty()));
        assertTrue(NecoList.empty().equals(NecoList.empty()));
    }
}
