package org.neco4j.collect.set;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HashSetTest {

    @Test
    public void addOpt() {
        assertThat(HashSet.empty().addOpt("foo").getOrFail()).containsExactly("foo");
        assertThat(HashSet.of("foo", "bar", "baz").addOpt("foo").isEmpty()).isTrue();
        assertThat(HashSet.of("foo", "bar", "baz").addOpt("quux").getOrFail()).containsOnly("foo", "bar", "baz", "quux");
    }

    @Test
    public void iterator() {
        assertThat(HashSet.empty()).isEmpty();
        assertThat(HashSet.of("foo", "bar", "baz")).containsOnly("foo", "bar", "baz");
    }

    @Test
    public void put() {
        assertThat(HashSet.empty().put("foo")).containsExactly("foo");
        assertThat(HashSet.of("foo", "bar", "baz").put("foo")).containsOnly("foo", "bar", "baz");
        assertThat(HashSet.of("foo", "bar", "baz").put("quux")).containsOnly("foo", "bar", "baz", "quux");
    }

    @Test
    public void contains() {
        assertThat(HashSet.empty().contains("foo")).isFalse();
        assertThat(HashSet.of("foo", "bar", "baz").contains("foo")).isTrue();
        assertThat(HashSet.of("foo", "bar", "baz").contains("quux")).isFalse();
    }

    @Test
    public void removeOpt() {
        assertThat(HashSet.empty().removeOpt("foo").isEmpty()).isTrue();
        assertThat(HashSet.of("foo", "bar", "baz").removeOpt("foo").getOrFail()).containsOnly("bar", "baz");
        assertThat(HashSet.of("foo", "bar", "baz").removeOpt("quux").isEmpty()).isTrue();
    }

    @Test
    public void size() {
        assertThat(HashSet.empty().size()).isEqualTo(0L);
        assertThat(HashSet.of("foo", "bar", "baz").size()).isEqualTo(3);
        assertThat(HashSet.of("foo", "bar", "baz", "bar", "foo").size()).isEqualTo(3);
    }

    @Test
    public void testToString() {
        assertThat(HashSet.empty().toString()).isEqualTo("HashSet[]");
        assertThat(HashSet.of(1,2,3).toString()).isEqualTo("HashSet[1, 2, 3]");
    }

    @Test
    public void testHashCode() {
        int h0 = HashSet.empty().hashCode();
        int h1 = HashSet.of("foo", "bar", "baz").hashCode();
        int h2 = HashSet.of("bar", "foo", "bar", "baz", "bar", "foo").hashCode();
        int h3 = HashSet.of("foo", "bar", "quux").hashCode();

        assertThat(h0).isNotEqualTo(h1);
        assertThat(h0).isNotEqualTo(h2);
        assertThat(h0).isNotEqualTo(h3);

        assertThat(h1).isEqualTo(h2);
        assertThat(h1).isNotEqualTo(h3);
    }

    @Test
    public void testEquals() {
        HashSet<String> h0 = HashSet.empty();
        HashSet<String> h1 = HashSet.of("foo", "bar", "baz");
        HashSet<String> h2 = HashSet.of("bar", "foo", "bar", "baz", "bar", "foo");
        HashSet<String> h3 = HashSet.of("foo", "bar", "quux");
        HashSet<Integer> h4 = HashSet.of(1, 3, 5);

        assertThat(h0).isNotEqualTo(h1);
        assertThat(h0).isNotEqualTo(h2);
        assertThat(h0).isNotEqualTo(h3);
        assertThat(h0).isNotEqualTo(h4);

        assertThat(h1).isEqualTo(h2);
        assertThat(h1).isNotEqualTo(h3);
        assertThat(h1).isNotEqualTo(h4);

        assertThat(h3).isNotEqualTo(h4);
    }
}