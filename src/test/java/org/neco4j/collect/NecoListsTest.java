package org.neco4j.collect;

import org.junit.Test;
import org.neco4j.collect.old.NecoList;
import org.neco4j.collect.old.NecoLists;
import org.neco4j.either.Either;
import org.neco4j.tuple.Pair;

import java.io.Serializable;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NecoListsTest {

    private final NecoList<? extends Serializable> sut = NecoList.of('a', 'b', 'c', 'd');

    @Test
    public void testFromIterable() throws Exception {
        Iterable<Integer> it = Arrays.asList(1, 2, 3, 4, 5, 6);
        assertThat(NecoLists.fromIterable(it), is(NecoList.of(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void testEithers() {
        NecoList<Either<Integer, String>> mixed = NecoList.of(
                Either.<Integer, String>rightOf("foo"),
                Either.<Integer, String>leftOf(2),
                Either.<Integer, String>rightOf("bar"),
                Either.<Integer, String>leftOf(5));
        assertThat(NecoLists.eithers(mixed), is(Pair.of(NecoList.of(2, 5), NecoList.of("foo", "bar"))));

        NecoList<Either<Integer, String>> leftsOnly = NecoList.of(
                Either.<Integer, String>leftOf(2),
                Either.<Integer, String>leftOf(5));
        assertThat(NecoLists.eithers(leftsOnly), is(Pair.of(NecoList.of(2, 5), NecoList.<String>empty())));

        NecoList<Either<Integer, String>> rightsOnly = NecoList.of(
                Either.<Integer, String>rightOf("foo"),
                Either.<Integer, String>rightOf("bar"));
        assertThat(NecoLists.eithers(rightsOnly), is(Pair.of(NecoList.empty(), NecoList.<String>of("foo", "bar"))));

        assertThat(NecoLists.eithers(NecoList.empty()), is(Pair.of(NecoList.empty(), NecoList.empty())));
    }

    @Test
    public void testUnzip() {
        NecoList<Pair<Integer, String>> list = NecoList.of(Pair.of(2, "foo"), Pair.of(5, "bar"));
        assertThat(NecoLists.unzip(list), is(Pair.of(NecoList.of(2, 5), NecoList.of("foo", "bar"))));

        assertThat(NecoLists.unzip(NecoList.empty()), is(Pair.of(NecoList.empty(), NecoList.empty())));
    }

    @Test
    public void testZip() throws Exception {
        NecoList<Pair<Character, Integer>> expectedList = NecoList.of(Pair.of('a', 1), Pair.of('b', 2),
                Pair.of('c', 3), Pair.of('d', 4));
        assertThat(NecoLists.zip(sut, NecoList.of(1, 2, 3, 4)), is(expectedList));
        assertThat(NecoLists.zip(NecoList.empty(), NecoList.empty()), is(NecoList.<Pair<Object, Object>>empty()));
    }

    @Test
    public void testZipWithDifferentLengths() throws Exception {
        NecoList<Pair<Character, Integer>> expectedList = NecoList.of(Pair.of('a', 1), Pair.of('b', 2),
                Pair.of('c', 3));
        assertThat(NecoLists.zip(sut, NecoList.of(1, 2, 3)), is(expectedList));
        expectedList = NecoList.of(Pair.of('a', 1), Pair.of('b', 2),
                Pair.of('c', 3), Pair.of('d', 4));
        assertThat(NecoLists.zip(sut, NecoList.of(1, 2, 3, 4, 5)), is(expectedList));
        assertThat(NecoLists.zip(NecoList.empty(), sut), is(NecoList.<Pair<Object, Character>>empty()));
        assertThat(NecoLists.zip(sut, NecoList.empty()), is(NecoList.<Pair<Character, Object>>empty()));
    }

    @Test
    public void testStrictZip() throws Exception {
        NecoList<Pair<Character, Integer>> expectedList = NecoList.of(Pair.of('a', 1), Pair.of('b', 2),
                Pair.of('c', 3), Pair.of('d', 4));
        assertThat(NecoLists.strictZip(sut, NecoList.of(1, 2, 3, 4)), is(expectedList));
        assertThat(NecoLists.strictZip(NecoList.empty(), NecoList.empty()), is(NecoList.<Pair<Object, Object>>empty()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStrictZipOnNilAndCons() throws Exception {
        NecoLists.strictZip(NecoList.empty(), sut);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStrictZipOnConsAndNil() throws Exception {
        NecoLists.strictZip(sut, NecoList.empty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStrictZipWithDifferentLengths() throws Exception {
        NecoLists.strictZip(sut, NecoList.of(1, 2, 3));
    }
}
