package org.neco4j.collect.unitkey;

import org.junit.Test;
import org.neco4j.collect.unitkey.Stack;

import static org.assertj.core.api.Assertions.*;

public class StackTest {

    @Test
    public void isEmpty() {
        assertThat(Stack.empty().isEmpty()).isTrue();
        assertThat(Stack.singleton("foo").isEmpty()).isFalse();
        assertThat(Stack.of("foo", "bar", "baz").isEmpty()).isFalse();
    }

    @Test
    public void size() {
        assertThat(Stack.empty().size()).isEqualTo(0L);
        assertThat(Stack.singleton("foo").size()).isEqualTo(1L);
        assertThat(Stack.of("foo", "bar", "baz").size()).isEqualTo(3L);
    }

    @Test
    public void empty() {
        assertThat(Stack.empty().isEmpty()).isTrue();
    }

    @Test
    public void singleton() {
        assertThat(Stack.singleton("foo")).containsExactly("foo");
    }

    @Test
    public void of() {
        assertThat(Stack.of("foo", "bar", "baz")).containsExactly("baz", "bar", "foo");
    }

    @Test
    public void add() {
        assertThat(Stack.empty().add("foo").add("bar").add("baz")).containsExactly("baz", "bar", "foo");
    }

    @Test
    public void putOpt() {
        assertThat(Stack.empty().putOpt("foo").isEmpty()).isTrue();
        assertThat(Stack.of("bar","baz").putOpt("foo").getOrFail()).containsExactly("foo", "bar");
    }

    @Test
    public void getOpt() {
        assertThat(Stack.empty().getOpt().isEmpty()).isTrue();
        assertThat(Stack.singleton("foo").getOpt().getOrFail()).isEqualTo("foo");
        assertThat(Stack.of("foo", "bar", "baz").getOpt().getOrFail()).isEqualTo("baz");
    }

    @Test
    public void lastOpt() {
        assertThat(Stack.empty().lastOpt().isEmpty()).isTrue();
        assertThat(Stack.of("foo", "bar", "baz").lastOpt().getOrFail()).isEqualTo("foo");
    }

    @Test
    public void reverse() {
        assertThat(Stack.of("foo", "bar", "baz").reverse()).containsExactly("foo", "bar", "baz");
    }

    @Test
    public void removeOpt() {
        assertThat(Stack.empty().removeOpt().isEmpty()).isTrue();
        assertThat(Stack.of("foo", "bar", "baz").removeOpt().getOrFail()).containsExactly("bar", "foo");
    }

    @Test
    public void toString_() {
        assertThat(Stack.empty().toString()).isEqualTo("Stack[]");
        assertThat(Stack.of("foo", "bar", "baz").toString()).isEqualTo("Stack[baz, bar, foo]");
    }
}