package org.neco4j.collect.deque;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class DequeTest {

    @Test
    public void addFirst() {
        assertThat(Deque.<String>empty().addFirst("foo")).containsExactly("foo");
        assertThat(Deque.of("bar", "baz").addFirst("foo")).containsExactly("foo", "bar", "baz");
    }

    @Test
    public void addLast() {
        assertThat(Deque.<String>empty().addLast("foo")).containsExactly("foo");
        assertThat(Deque.of("bar", "baz").addLast("foo")).containsExactly("bar", "baz", "foo");
    }

    @Test
    public void putFirstOpt() {
        assertThat(Deque.<String>empty().putFirstOpt("foo")).isEmpty();
        assertThat(Deque.of("bar", "baz").putFirstOpt("foo").getOrFail()).containsExactly("foo", "baz");
    }

    @Test
    public void putLastOpt() {
        assertThat(Deque.<String>empty().putLastOpt("foo")).isEmpty();
        assertThat(Deque.of("bar", "baz").putLastOpt("foo").getOrFail()).containsExactly("bar", "foo");
    }

    @Test
    public void getFirstOpt() {
        assertThat(Deque.<String>empty().getFirstOpt()).isEmpty();
        assertThat(Deque.of("foo", "bar", "baz").getFirstOpt()).containsExactly("foo");
    }

    @Test
    public void getLastOpt() {
        assertThat(Deque.<String>empty().getLastOpt()).isEmpty();
        assertThat(Deque.of("foo", "bar", "baz").getLastOpt()).containsExactly("baz");
    }

    @Test
    public void removeFirstOpt() {
        assertThat(Deque.<String>empty().removeFirstOpt()).isEmpty();
        assertThat(Deque.of("foo", "bar", "baz").removeFirstOpt().getOrFail()).containsExactly("bar", "baz");
    }

    @Test
    public void removeLastOpt() {
        assertThat(Deque.<String>empty().removeLastOpt()).isEmpty();
        assertThat(Deque.of("foo", "bar", "baz").removeLastOpt().getOrFail()).containsExactly("foo", "bar");
    }

    @Test
    public void isEmpty() {
        assertThat(Deque.<String>empty().isEmpty()).isTrue();
        assertThat(Deque.of("foo", "bar", "baz").isEmpty()).isFalse();
    }

    @Test
    public void size() {
        assertThat(Deque.<String>empty().size()).isEqualTo(0);
        assertThat(Deque.of("foo", "bar", "baz").size()).isEqualTo(3);
    }

    @Test
    public void reverse() {
        assertThat(Deque.<String>empty().reverse()).isEmpty();
        assertThat(Deque.of("foo", "bar", "baz").reverse()).containsExactly("baz", "bar", "foo");
    }

    @Test
    public void singleton() {
        assertThat(Deque.singleton("foo")).containsExactly("foo");
    }

    @Test
    public void of() {
        assertThat(Deque.of()).isEmpty();
        assertThat(Deque.of("foo")).containsExactly("foo");
        assertThat(Deque.of("foo", "bar", "baz")).containsExactly("foo", "bar", "baz");
    }

    @Test
    public void testToString() {
        assertThat(Deque.of().toString()).isEqualTo("Deque[]");
        assertThat(Deque.of("foo").toString()).isEqualTo("Deque[foo]");
        assertThat(Deque.of("foo", "bar", "baz").toString()).isEqualTo("Deque[foo, bar, baz]");
    }
}