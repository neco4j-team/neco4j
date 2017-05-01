package org.neco4j.collect.indexed;

import org.junit.Test;
import org.neco4j.collect.indexed.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListTest {

    @Test
    public void isEmpty() {
        assertThat(List.empty().isEmpty()).isTrue();
        assertThat(List.of("foo", "bar", "baz").isEmpty()).isFalse();
    }

    @Test
    public void empty() {
        assertThat(List.empty().isEmpty()).isTrue();
        assertThat(List.empty().size()).isEqualTo(0L);
    }

    @Test
    public void singleton() {
        assertThat(List.singleton("foo")).containsExactly("foo");
    }

    @Test
    public void of() {
        assertThat(List.of()).isEmpty();
        assertThat(List.of("foo")).containsExactly("foo");
        assertThat(List.of("foo", "bar", "baz")).containsExactly("foo", "bar", "baz");
    }

    @Test
    public void prepend() {
        assertThat(List.empty().prepend("foo")).containsExactly("foo");
        assertThat(List.of("bar", "baz").prepend("foo")).containsExactly("foo", "bar", "baz");
    }

    @Test
    public void putOpt() {
        assertThat(List.empty().putOpt(-1, "foo").isEmpty()).isTrue();
        assertThat(List.empty().putOpt(0, "foo").isEmpty()).isTrue();
        assertThat(List.empty().putOpt(1, "foo").isEmpty()).isTrue();

        assertThat(List.of("foo", "bar", "baz").putOpt(-1, "quux").isEmpty()).isTrue();
        assertThat(List.of("foo", "bar", "baz").putOpt(0, "quux").getOrFail()).containsExactly("quux", "bar", "baz");
        assertThat(List.of("foo", "bar", "baz").putOpt(1, "quux").getOrFail()).containsExactly("foo", "quux", "baz");
        assertThat(List.of("foo", "bar", "baz").putOpt(2, "quux").getOrFail()).containsExactly("foo", "bar", "quux");
        assertThat(List.of("foo", "bar", "baz").putOpt(3, "quux").isEmpty()).isTrue();
    }

    @Test
    public void addOpt() {
        assertThat(List.empty().addOpt(-1, "foo").isEmpty()).isTrue();
        assertThat(List.empty().addOpt(0, "foo").getOrFail()).containsExactly("foo");
        assertThat(List.empty().addOpt(1, "foo").isEmpty()).isTrue();

        assertThat(List.of("foo", "bar", "baz").addOpt(-1, "quux").isEmpty()).isTrue();
        assertThat(List.of("foo", "bar", "baz").addOpt(0, "quux").getOrFail()).containsExactly("quux", "foo", "bar", "baz");
        assertThat(List.of("foo", "bar", "baz").addOpt(1, "quux").getOrFail()).containsExactly("foo", "quux", "bar", "baz");
        assertThat(List.of("foo", "bar", "baz").addOpt(2, "quux").getOrFail()).containsExactly("foo", "bar", "quux", "baz");
        assertThat(List.of("foo", "bar", "baz").addOpt(3, "quux").getOrFail()).containsExactly("foo", "bar", "baz", "quux");
        assertThat(List.of("foo", "bar", "baz").addOpt(4, "quux").isEmpty()).isTrue();
    }

    @Test
    public void getOpt() {
        assertThat(List.empty().getOpt(-1).isEmpty()).isTrue();
        assertThat(List.empty().getOpt(0).isEmpty()).isTrue();
        assertThat(List.empty().getOpt(1).isEmpty()).isTrue();

        assertThat(List.of("foo", "bar", "baz").getOpt(-1).isEmpty()).isTrue();
        assertThat(List.of("foo", "bar", "baz").getOpt(0).getOrFail()).isEqualTo("foo");
        assertThat(List.of("foo", "bar", "baz").getOpt(1).getOrFail()).isEqualTo("bar");
        assertThat(List.of("foo", "bar", "baz").getOpt(2).getOrFail()).isEqualTo("baz");
        assertThat(List.of("foo", "bar", "baz").getOpt(3).isEmpty()).isTrue();
    }

    @Test
    public void removeOpt() {
        assertThat(List.empty().removeOpt(-1).isEmpty()).isTrue();
        assertThat(List.empty().removeOpt(0).isEmpty()).isTrue();
        assertThat(List.empty().removeOpt(1).isEmpty()).isTrue();

        assertThat(List.of("foo", "bar", "baz").removeOpt(-1).isEmpty()).isTrue();
        assertThat(List.of("foo", "bar", "baz").removeOpt(0).getOrFail()).containsExactly("bar", "baz");
        assertThat(List.of("foo", "bar", "baz").removeOpt(1).getOrFail()).containsExactly("foo", "baz");
        assertThat(List.of("foo", "bar", "baz").removeOpt(2).getOrFail()).containsExactly("foo", "bar");
        assertThat(List.of("foo", "bar", "baz").removeOpt(3).isEmpty()).isTrue();

        assertThat(List.of(0,1,2,3,4,5,6,7,8,9).removeOpt(7).getOrFail()).containsExactly(0,1,2,3,4,5,6,8,9);
    }

    @Test
    public void size() {
        assertThat(List.empty().size()).isEqualTo(0L);
        assertThat(List.singleton("foo").size()).isEqualTo(1L);
        assertThat(List.of("foo", "bar", "baz").size()).isEqualTo(3L);
    }

}