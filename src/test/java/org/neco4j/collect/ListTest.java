package org.neco4j.collect;

import org.junit.Test;
import org.neco4j.either.Either;
import org.neco4j.tuple.Pair;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ListTest {
    private final List<Character> sut = List.of('a', 'b', 'c', 'd');

    @Test
    public void testHeadOnCons() throws Exception {
        assertThat(sut.head(), is('a'));
    }

    @Test(expected = NoSuchElementException.class)
    public void testHeadOnNil() throws Exception {
        List.empty().head();
    }

    @Test
    public void testHeadOpt() throws Exception {
        assertThat(sut.headOpt(), is(Optional.of('a')));
        assertThat(List.empty().headOpt(), is(Optional.empty()));
    }

    @Test
    public void testTailOnCons() throws Exception {
        assertThat(sut.tail(), is(List.of('b', 'c', 'd')));
    }

    @Test(expected = NoSuchElementException.class)
    public void testTailOnNil() throws Exception {
        List.empty().tail();
    }

    @Test
    public void testTailOpt() throws Exception {
        assertThat(sut.tailOpt(), is(Optional.of(List.of('b', 'c', 'd'))));
        assertThat(List.<Character>empty().tailOpt(), is(Optional.<List<Character>>empty()));
    }

    @Test
    public void testLastOnCons() throws Exception {
        assertThat(sut.last(), is('d'));
    }

    @Test(expected = NoSuchElementException.class)
    public void testLastOnNil() throws Exception {
        List.empty().last();
    }

    @Test
    public void testLastOpt() throws Exception {
        assertThat(sut.lastOpt(), is(Optional.of('d')));
        assertThat(List.empty().lastOpt(), is(Optional.empty()));
    }

    @Test
    public void testInitOnCons() throws Exception {
        assertThat(sut.init(), is(List.of('a', 'b', 'c')));
    }

    @Test(expected = NoSuchElementException.class)
    public void testInitOnNil() throws Exception {
        List.empty().init();
    }

    @Test
    public void testInitOpt() throws Exception {
        assertThat(sut.initOpt(), is(Optional.of(List.of('a', 'b', 'c'))));
        assertThat(List.<Character>empty().initOpt(), is(Optional.<List<Character>>empty()));
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
        List.empty().get(0);
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
        assertThat(List.empty().getOpt(0), is(Optional.empty()));
    }

    @Test
    public void testSize() throws Exception {
        assertThat(sut.size(), is(4));
        assertThat(List.empty().size(), is(0));
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertFalse(sut.isEmpty());
        assertTrue(List.empty().isEmpty());
    }

    @Test
    public void testWithOnCons() throws Exception {
        assertThat(sut.with(2, 'x'), is(List.of('a', 'b', 'x', 'd')));
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
        assertThat(sut.concat(List.of('e', 'f')), is(List.of('a', 'b', 'c', 'd', 'e', 'f')));
        assertThat(List.<Character>empty().concat(sut), is(sut));
        assertThat(sut.concat(List.empty()), is(sut));
        assertThat(List.empty().concat(List.empty()), is(List.empty()));
    }

    @Test
    public void testTake() throws Exception {
        assertThat(sut.take(2), is(List.of('a', 'b')));
    }

    @Test
    public void testTakeMoreThanInList() throws Exception {
        assertThat(sut.take(10), is(List.of('a', 'b', 'c', 'd')));
        assertThat(List.empty().take(1), is(List.empty()));
    }

    @Test
    public void testTakeWhile() throws Exception {
        List<Integer> intList = List.of(5, 8, 6, 13, 9);
        assertThat(intList.takeWhile(i -> i < 10), is(List.of(5, 8, 6)));
    }

    @Test
    public void testDrop() throws Exception {
        assertThat(sut.drop(2), is(List.of('c', 'd')));
    }

    @Test
    public void testDropMoreThanInList() throws Exception {
        assertThat(sut.drop(10), is(List.<Character>empty()));
        assertThat(List.empty().drop(1), is(List.empty()));
    }

    @Test
    public void testDropWhile() throws Exception {
        List<Integer> intList = List.of(5, 8, 6, 13, 9);
        assertThat(intList.dropWhile(i -> i < 10), is(List.of(13, 9)));
    }

    @Test
    public void testReverse() throws Exception {
        assertThat(sut.reverse(), is(List.of('d', 'c', 'b', 'a')));
        assertThat(List.empty().reverse(), is(List.empty()));
    }

    @Test
    public void testMap() throws Exception {
        List<Integer> intList = List.of(5, 8, 6, 13, 9);
        assertThat(intList.map(i -> i + 1), is(List.of(6, 9, 7, 14, 10)));
    }

    @Test
    public void testFlatMap() throws Exception {
        List<Integer> intList = List.of(2, 4, 5);
        assertThat(intList.flatMap(x -> List.<Integer>of(x - 1, x, x + 1)), is(List.of(1, 2, 3, 3, 4, 5, 4, 5, 6)));
    }

    @Test
    public void testFilter() throws Exception {
        List<Integer> intList = List.of(5, 8, 6, 13, 9);
        assertThat(intList.filter(i -> i < 7), is(List.of(5, 6)));
    }

    @Test
    public void testFoldLeft() throws Exception {
        List<Integer> intList = List.of(5, 8, 6, 13, 9);
        // (((((((((2 + 1) * 5) + 1) * 8) + 1) * 6) + 1) * 13) + 1) * 9 = 90.684
        assertThat(intList.foldLeft(2, (a, b) -> (a + 1) * b), is(90684));
    }

    @Test
    public void testFoldRight() throws Exception {
        List<Integer> intList = List.of(5, 8, 6, 13, 9);
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
        assertFalse(List.empty().iterator().hasNext());
    }

    @Test
    public void testZip() throws Exception {
        List<Pair<Character, Integer>> expectedList = List.of(Pair.of('a', 1), Pair.of('b', 2),
                Pair.of('c', 3), Pair.of('d', 4));
        assertThat(sut.zip(List.of(1, 2, 3, 4)), is(expectedList));
        assertThat(List.empty().zip(List.empty()), is(List.<Pair<Object, Object>>empty()));
    }

    @Test
    public void testZipWithDifferentLengths() throws Exception {
        List<Pair<Character, Integer>> expectedList = List.of(Pair.of('a', 1), Pair.of('b', 2),
                Pair.of('c', 3));
        assertThat(sut.zip(List.of(1, 2, 3)), is(expectedList));
        expectedList = List.of(Pair.of('a', 1), Pair.of('b', 2),
                Pair.of('c', 3), Pair.of('d', 4));
        assertThat(sut.zip(List.of(1, 2, 3, 4, 5)), is(expectedList));
        assertThat(List.empty().zip(sut), is(List.<Pair<Object, Character>>empty()));
        assertThat(sut.zip(List.empty()), is(List.<Pair<Character, Object>>empty()));
    }

    @Test
    public void testStrictZip() throws Exception {
        List<Pair<Character, Integer>> expectedList = List.of(Pair.of('a', 1), Pair.of('b', 2),
                Pair.of('c', 3), Pair.of('d', 4));
        assertThat(sut.strictZip(List.of(1, 2, 3, 4)), is(expectedList));
        assertThat(List.empty().strictZip(List.empty()), is(List.<Pair<Object, Object>>empty()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStrictZipOnNilAndCons() throws Exception {
        List.empty().strictZip(sut);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStrictZipOnConsAndNil() throws Exception {
        sut.strictZip(List.empty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStrictZipWithDifferentLengths() throws Exception {
        sut.strictZip(List.of(1, 2, 3));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(sut.toString(), is("[a,b,c,d]"));
        assertThat(List.empty().toString(), is("[]"));
    }

    @Test
    public void testEquals() throws Exception {
        assertTrue(sut.equals(List.of('a', 'b', 'c', 'd')));
        assertFalse(sut.equals(List.of('a', 'b', 'c')));
        assertFalse(List.of('a', 'b', 'c').equals(sut));
        assertFalse(List.empty().equals(sut));
        assertFalse(sut.equals(List.empty()));
        assertTrue(List.empty().equals(List.empty()));
    }

    @Test
    public void testLefts() {
        List<Either<Integer, String>> mixed = List.of(
                Either.<Integer, String>rightOf("foo"),
                Either.<Integer, String>leftOf(2),
                Either.<Integer, String>rightOf("bar"),
                Either.<Integer, String>leftOf(5));
        assertThat(List.lefts(mixed), is(List.of(2, 5)));

        List<Either<Integer, String>> leftsOnly = List.of(
                Either.<Integer, String>leftOf(2),
                Either.<Integer, String>leftOf(5));
        assertThat(List.lefts(leftsOnly), is(List.of(2, 5)));

        List<Either<Integer, String>> rightsOnly = List.of(
                Either.<Integer, String>rightOf("foo"),
                Either.<Integer, String>rightOf("bar"));
        assertThat(List.lefts(rightsOnly), is(List.empty()));

        assertThat(List.lefts(List.empty()), is(List.empty()));
    }

    @Test
    public void testRights() {
        List<Either<Integer, String>> mixed = List.of(
                Either.<Integer, String>rightOf("foo"),
                Either.<Integer, String>leftOf(2),
                Either.<Integer, String>rightOf("bar"),
                Either.<Integer, String>leftOf(5));
        assertThat(List.rights(mixed), is(List.of("foo", "bar")));

        List<Either<Integer, String>> leftsOnly = List.of(
                Either.<Integer, String>leftOf(2),
                Either.<Integer, String>leftOf(5));
        assertThat(List.rights(leftsOnly), is(List.empty()));

        List<Either<Integer, String>> rightsOnly = List.of(
                Either.<Integer, String>rightOf("foo"),
                Either.<Integer, String>rightOf("bar"));
        assertThat(List.rights(rightsOnly), is(List.of("foo", "bar")));

        assertThat(List.rights(List.empty()), is(List.empty()));
    }

    @Test
    public void testLeftsRights() {
        List<Either<Integer, String>> mixed = List.of(
                Either.<Integer, String>rightOf("foo"),
                Either.<Integer, String>leftOf(2),
                Either.<Integer, String>rightOf("bar"),
                Either.<Integer, String>leftOf(5));
        assertThat(List.leftsRights(mixed), is(Pair.of(List.of(2, 5), List.of("foo", "bar"))));

        List<Either<Integer, String>> leftsOnly = List.of(
                Either.<Integer, String>leftOf(2),
                Either.<Integer, String>leftOf(5));
        assertThat(List.leftsRights(leftsOnly), is(Pair.of(List.of(2, 5), List.<String>empty())));

        List<Either<Integer, String>> rightsOnly = List.of(
                Either.<Integer, String>rightOf("foo"),
                Either.<Integer, String>rightOf("bar"));
        assertThat(List.leftsRights(rightsOnly), is(Pair.of(List.empty(), List.<String>of("foo", "bar"))));

        assertThat(List.leftsRights(List.empty()), is(Pair.of(List.empty(), List.empty())));
    }

    @Test
    public void testUnzip() {
        List<Pair<Integer, String>> list = List.of(Pair.of(2, "foo"), Pair.of(5, "bar"));
        assertThat(List.unzip(list), is(Pair.of(List.of(2, 5), List.of("foo", "bar"))));

        assertThat(List.unzip(List.empty()), is(Pair.of(List.empty(), List.empty())));
    }
}
