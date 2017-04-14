package org.neco4j.collect;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QueueTest {
    @Test
    public void add() {
        Queue<Object> queue = Queue.empty().add("foo").add("bar").add("baz");
        assertThat(queue).containsExactly("foo","bar","baz");
    }

    @Test
    public void putOpt() {
        assertThat(Queue.<String>empty().putOpt("foo").isEmpty()).isTrue();
        assertThat(Queue.of("bar", "baz").putOpt("foo").getOrFail()).containsExactly("bar", "foo");
    }

    @Test
    public void getOpt() {
        assertThat(Queue.empty().getOpt().isEmpty()).isTrue();
        assertThat(Queue.of("foo", "bar", "baz").getOpt().getOrFail()).isEqualTo("foo");
    }

    @Test
    public void removeOpt() {
        assertThat(Queue.empty().removeOpt().isEmpty()).isTrue();
        assertThat(Queue.singleton("foo").removeOpt().getOrFail().isEmpty());
        assertThat(Queue.of("foo", "bar", "baz").removeOpt().getOrFail()).containsExactly("bar", "baz");
    }

    @Test
    public void isEmpty() {
        assertThat(Queue.empty().isEmpty()).isTrue();
        assertThat(Queue.singleton("foo").isEmpty()).isFalse();
        Queue<Object> queue = Queue.empty().add("foo").add("bar").add("baz");
        assertThat(queue.isEmpty()).isFalse();
    }

    @Test
    public void size() {
        assertThat(Queue.empty().size()).isEqualTo(0L);
        assertThat(Queue.singleton("foo").size()).isEqualTo(1L);
        assertThat(Queue.of("foo", "bar", "baz").size()).isEqualTo(3L);
    }

    @Test
    public void reverse() {
        assertThat(Queue.empty().reverse().isEmpty()).isTrue();
        assertThat(Queue.singleton("foo").reverse()).containsExactly("foo");
        assertThat(Queue.of("foo", "bar", "baz").reverse()).containsExactly("baz", "bar", "foo");
    }

    @Test
    public void empty() {
        assertThat(Queue.empty().isEmpty()).isTrue();
    }

    @Test
    public void singleton() {
        assertThat(Queue.singleton("foo")).containsExactly("foo");
    }

    @Test
    public void of() {
        assertThat(Queue.of("foo", "bar", "baz")).containsExactly("foo", "bar", "baz");
    }

    @Test
    public void toString_() {
        assertThat(Queue.empty().toString()).isEqualTo("Queue[]");
        assertThat(Queue.singleton("foo").toString()).isEqualTo("Queue[foo]");
        assertThat(Queue.of("foo", "bar", "baz").toString()).isEqualTo("Queue[foo, bar, baz]");
    }

}