package org.neco4j.collect.set;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HashSetTest {

    @Test
    public void addOpt() {
        assertThat(HashSet.empty().addOpt("foo").getOrFail()).containsExactly("foo");
        assertThat(HashSet.of("foo","bar","baz").addOpt("foo").isEmpty()).isTrue();
        assertThat(HashSet.of("foo","bar","baz").addOpt("quux").getOrFail()).containsOnly("foo","bar","baz","quux");
    }

    @Test
    public void iterator() {
        assertThat(HashSet.empty()).isEmpty();
        assertThat(HashSet.of("foo","bar","baz")).containsOnly("foo","bar","baz");
    }

    @Test
    public void put() {
        assertThat(HashSet.empty().put("foo")).containsExactly("foo");
        assertThat(HashSet.of("foo","bar","baz").put("foo")).containsOnly("foo","bar","baz");
        assertThat(HashSet.of("foo","bar","baz").put("quux")).containsOnly("foo","bar","baz","quux");
    }

    @Test
    public void contains() {
        assertThat(HashSet.empty().contains("foo")).isFalse();
        assertThat(HashSet.of("foo","bar","baz").contains("foo")).isTrue();
        assertThat(HashSet.of("foo","bar","baz").contains("quux")).isFalse();
    }

    @Test
    public void removeOpt() {
        assertThat(HashSet.empty().removeOpt("foo").isEmpty()).isTrue();
        assertThat(HashSet.of("foo","bar","baz").removeOpt("foo").getOrFail()).containsOnly("bar","baz");
        assertThat(HashSet.of("foo","bar","baz").removeOpt("quux").isEmpty()).isTrue();
    }

    @Test
    public void size() {
        assertThat(HashSet.empty().size()).isEqualTo(0L);
        assertThat(HashSet.of("foo","bar","baz").size()).isEqualTo(3);
        assertThat(HashSet.of("foo","bar","baz","bar","foo").size()).isEqualTo(3);
    }
}