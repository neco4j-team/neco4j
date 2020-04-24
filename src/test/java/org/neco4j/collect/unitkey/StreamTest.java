package org.neco4j.collect.unitkey;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StreamTest {
    @Test
    public void add() {
        assertThat(Stream.constant("foo").add("bar")).startsWith("bar", "foo", "foo");
    }

    @Test
    public void put() {
        assertThat(Stream.constant("foo").add("bar").put("baz")).startsWith("baz", "foo", "foo");
    }

    @Test
    public void constant() {
        assertThat(Stream.constant("foo")).startsWith("foo", "foo", "foo");
    }

    @Test
    public void get() {
        assertThat(Stream.constant("foo").get()).isEqualTo("foo");
        assertThat(Stream.constant("foo").add("bar").get()).isEqualTo("bar");
    }

    @Test
    public void iterate() {
        assertThat(Stream.iterate(1, x -> x + 1)).startsWith(1, 2, 3, 4, 5, 6);
    }

    @Test
    public void remove() {
        assertThat(Stream.iterate(1, x -> x + 1).remove()).startsWith(2, 3, 4);
    }

    @Test
    public void toString_() {
        assertThat(Stream.iterate(1, x -> x + 1).toString()).isEqualTo("Stream[1, 2, 3, 4, 5, 6, 7, 8, 9, 10...]");
    }

    @Test
    public void map() {
        assertThat(Stream.iterate(1, x -> x + 1).map(n -> n * n).toString())
            .isEqualTo("Stream[1, 4, 9, 16, 25, 36, 49, 64, 81, 100...]");
    }

    @Test
    public void take() {
        Stream<Integer> stream = Stream.iterate(1, x -> x + 1);
        assertThat(stream.take(-5)).isEmpty();
        assertThat(stream.take(0)).isEmpty();
        assertThat(stream.take(1)).containsExactly(1);
        assertThat(stream.take(5)).containsExactly(1, 2, 3, 4, 5);
    }
}