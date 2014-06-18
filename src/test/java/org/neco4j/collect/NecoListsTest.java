package org.neco4j.collect;

import org.junit.Test;
import org.neco4j.either.Either;
import org.neco4j.tuple.Pair;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class NecoListsTest {
    @Test
    public void testLefts() {
        NecoList<Either<Integer, String>> mixed = NecoList.of(
                Either.<Integer, String>rightOf("foo"),
                Either.<Integer, String>leftOf(2),
                Either.<Integer, String>rightOf("bar"),
                Either.<Integer, String>leftOf(5));
        assertThat(NecoLists.lefts(mixed), is(NecoList.of(2, 5)));

        NecoList<Either<Integer, String>> leftsOnly = NecoList.of(
                Either.<Integer, String>leftOf(2),
                Either.<Integer, String>leftOf(5));
        assertThat(NecoLists.lefts(leftsOnly), is(NecoList.of(2, 5)));

        NecoList<Either<Integer, String>> rightsOnly = NecoList.of(
                Either.<Integer, String>rightOf("foo"),
                Either.<Integer, String>rightOf("bar"));
        assertThat(NecoLists.lefts(rightsOnly), is(NecoList.empty()));

        assertThat(NecoLists.lefts(NecoList.empty()), is(NecoList.empty()));
    }

    @Test
    public void testRights() {
        NecoList<Either<Integer, String>> mixed = NecoList.of(
                Either.<Integer, String>rightOf("foo"),
                Either.<Integer, String>leftOf(2),
                Either.<Integer, String>rightOf("bar"),
                Either.<Integer, String>leftOf(5));
        assertThat(NecoLists.rights(mixed), is(NecoList.of("foo", "bar")));

        NecoList<Either<Integer, String>> leftsOnly = NecoList.of(
                Either.<Integer, String>leftOf(2),
                Either.<Integer, String>leftOf(5));
        assertThat(NecoLists.rights(leftsOnly), is(NecoList.empty()));

        NecoList<Either<Integer, String>> rightsOnly = NecoList.of(
                Either.<Integer, String>rightOf("foo"),
                Either.<Integer, String>rightOf("bar"));
        assertThat(NecoLists.rights(rightsOnly), is(NecoList.of("foo", "bar")));

        assertThat(NecoLists.rights(NecoList.empty()), is(NecoList.empty()));
    }

    @Test
    public void testLeftsRights() {
        NecoList<Either<Integer, String>> mixed = NecoList.of(
                Either.<Integer, String>rightOf("foo"),
                Either.<Integer, String>leftOf(2),
                Either.<Integer, String>rightOf("bar"),
                Either.<Integer, String>leftOf(5));
        assertThat(NecoLists.leftsRights(mixed), is(Pair.of(NecoList.of(2, 5), NecoList.of("foo", "bar"))));

        NecoList<Either<Integer, String>> leftsOnly = NecoList.of(
                Either.<Integer, String>leftOf(2),
                Either.<Integer, String>leftOf(5));
        assertThat(NecoLists.leftsRights(leftsOnly), is(Pair.of(NecoList.of(2, 5), NecoList.<String>empty())));

        NecoList<Either<Integer, String>> rightsOnly = NecoList.of(
                Either.<Integer, String>rightOf("foo"),
                Either.<Integer, String>rightOf("bar"));
        assertThat(NecoLists.leftsRights(rightsOnly), is(Pair.of(NecoList.empty(), NecoList.<String>of("foo", "bar"))));

        assertThat(NecoLists.leftsRights(NecoList.empty()), is(Pair.of(NecoList.empty(), NecoList.empty())));
    }

    @Test
    public void testUnzip() {
        NecoList<Pair<Integer, String>> list = NecoList.of(Pair.of(2, "foo"), Pair.of(5, "bar"));
        assertThat(NecoLists.unzip(list), is(Pair.of(NecoList.of(2, 5), NecoList.of("foo", "bar"))));

        assertThat(NecoLists.unzip(NecoList.empty()), is(Pair.of(NecoList.empty(), NecoList.empty())));
    }

}