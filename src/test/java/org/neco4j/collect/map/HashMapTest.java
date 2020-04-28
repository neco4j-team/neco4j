package org.neco4j.collect.map;

import org.junit.Test;
import org.neco4j.tuple.Pair;

import static org.assertj.core.api.Assertions.assertThat;


public class HashMapTest {

    @Test
    public void empty() {
        HashMap<String, Integer> hm = HashMap.empty();
        assertThat(hm.isEmpty()).isTrue();
        assertThat(hm.size()).isEqualTo(0);
        assertThat(hm.asKeyValuePairs()).isEmpty();
    }

    @Test
    public void of1() {
        HashMap<String, Integer> hm = HashMap.of("foo", 1);
        assertThat(hm.isEmpty()).isFalse();
        assertThat(hm.size()).isEqualTo(1);
        assertThat(hm.getOpt("foo")).containsExactly(1);
        assertThat(hm.getOpt("boink")).isEmpty();
    }

    @Test
    public void of2() {
        HashMap<String, Integer> hm = HashMap.of("foo", 1, "bar", 2);
        assertThat(hm.isEmpty()).isFalse();
        assertThat(hm.size()).isEqualTo(2);
        assertThat(hm.getOpt("foo")).containsExactly(1);
        assertThat(hm.getOpt("bar")).containsExactly(2);
        assertThat(hm.getOpt("boink")).isEmpty();
    }

    @Test
    public void of3() {
        HashMap<String, Integer> hm = HashMap.of("foo", 1, "bar", 2, "baz", 3);
        assertThat(hm.isEmpty()).isFalse();
        assertThat(hm.size()).isEqualTo(3);
        assertThat(hm.getOpt("foo")).containsExactly(1);
        assertThat(hm.getOpt("bar")).containsExactly(2);
        assertThat(hm.getOpt("baz")).containsExactly(3);
        assertThat(hm.getOpt("boink")).isEmpty();
    }

    @Test
    public void of4() {
        HashMap<String, Integer> hm = HashMap.of("foo", 1, "bar", 2, "baz", 3, "quux", 4);
        assertThat(hm.isEmpty()).isFalse();
        assertThat(hm.size()).isEqualTo(4);
        assertThat(hm.getOpt("foo")).containsExactly(1);
        assertThat(hm.getOpt("bar")).containsExactly(2);
        assertThat(hm.getOpt("baz")).containsExactly(3);
        assertThat(hm.getOpt("quux")).containsExactly(4);
        assertThat(hm.getOpt("boink")).isEmpty();
    }

    @Test
    public void of5() {
        HashMap<String, Integer> hm = HashMap.of("foo", 1, "bar", 2, "baz", 3, "quux", 4, "biff", 5);
        assertThat(hm.isEmpty()).isFalse();
        assertThat(hm.size()).isEqualTo(5);
        assertThat(hm.getOpt("foo")).containsExactly(1);
        assertThat(hm.getOpt("bar")).containsExactly(2);
        assertThat(hm.getOpt("baz")).containsExactly(3);
        assertThat(hm.getOpt("quux")).containsExactly(4);
        assertThat(hm.getOpt("biff")).containsExactly(5);
        assertThat(hm.getOpt("boink")).isEmpty();
    }

    @Test
    public void of6() {
        HashMap<String, Integer> hm = HashMap.of("foo", 1, "bar", 2, "baz", 3, "quux", 4, "biff", 5, "baff", 6);
        assertThat(hm.isEmpty()).isFalse();
        assertThat(hm.size()).isEqualTo(6);
        assertThat(hm.getOpt("foo")).containsExactly(1);
        assertThat(hm.getOpt("bar")).containsExactly(2);
        assertThat(hm.getOpt("baz")).containsExactly(3);
        assertThat(hm.getOpt("quux")).containsExactly(4);
        assertThat(hm.getOpt("biff")).containsExactly(5);
        assertThat(hm.getOpt("baff")).containsExactly(6);
        assertThat(hm.getOpt("boink")).isEmpty();
    }

    @Test
    public void addOpt() {
        assertThat(HashMap.empty().addOpt("baz", 3).getOrFail().asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("baz", 3));
        assertThat(HashMap.of("foo", 1, "bar", 2).addOpt("baz", 3).getOrFail().asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 1), Pair.of("bar", 2), Pair.of("baz", 3));
        assertThat(HashMap.of("foo", 1, "bar", 2).addOpt("bar", 3)).isEmpty();
    }

    @Test
    public void getOpt() {
        assertThat(HashMap.empty().getOpt("quux")).isEmpty();
        assertThat(HashMap.of("foo", 1, "bar", 2, "baz", 3).getOpt("quux")).isEmpty();
        assertThat(HashMap.of("foo", 1, "bar", 2, "baz", 3).getOrFail("bar")).isEqualTo(2);
    }

    @Test
    public void asKeyValuePairs() {
        assertThat(HashMap.empty().asKeyValuePairs()).isEmpty();
        assertThat(HashMap.of("foo", 1, "bar", 2, "baz", 3).asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 1), Pair.of("bar", 2), Pair.of("baz", 3));
    }

    @Test
    public void removeOpt() {
        assertThat(HashMap.empty().removeOpt("baz")).isEmpty();
        assertThat(HashMap.of("foo", 1, "bar", 2).removeOpt("baz")).isEmpty();
        assertThat(HashMap.of("foo", 1, "bar", 2).removeOpt("bar").getOrFail().asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 1));
    }

    @Test
    public void size() {
        assertThat(HashMap.empty().size()).isEqualTo(0);
        assertThat(HashMap.of("foo", 1, "bar", 2).size()).isEqualTo(2);
    }

    @Test
    public void put() {
        assertThat(HashMap.empty().put("baz", 3).asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("baz", 3));
        assertThat(HashMap.of("foo", 1, "bar", 2).put("baz", 3).asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 1), Pair.of("bar", 2), Pair.of("baz", 3));
        assertThat(HashMap.of("foo", 1, "bar", 2).put("bar", 3).asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 1), Pair.of("bar", 3));
    }

    @Test
    public void keys() {
        assertThat(HashMap.empty().keys()).isEmpty();
        assertThat(HashMap.of("foo", 1, "bar", 2).keys()).containsExactlyInAnyOrder("foo", "bar");
    }

    @Test
    public void values() {
        assertThat(HashMap.empty().values()).isEmpty();
        assertThat(HashMap.of("foo", 1, "bar", 2).values()).containsExactlyInAnyOrder(1, 2);
    }

    @Test
    public void containsKey() {
        HashMap<String, Integer> hm = HashMap.of("foo", 1, "bar", 2, "baz", 3, "quux", 4, "biff", 5, "baff", 6);

        assertThat(hm.containsKey("baz")).isTrue();
        assertThat(hm.containsKey("bäng")).isFalse();
    }

    @Test
    public void testHashCode() {
        int h0 = HashMap.empty().hashCode();
        int h1 = HashMap.of("foo", 1).hashCode();
        int h2 = HashMap.of("foo", 1, "bar", 2, "baz", 3).hashCode();
        int h3 = HashMap.of("bar", 2, "baz", 3, "foo", 1).hashCode();
        int h4 = HashMap.of("foo", 1, "bar", 2, "baz", 4).hashCode();
        int h5 = HashMap.of("foo", 1.0, "bar", 2.0, "baz", 3.0).hashCode();

        assertThat(h0).isNotEqualTo(h1);
        assertThat(h0).isNotEqualTo(h2);
        assertThat(h0).isNotEqualTo(h3);
        assertThat(h0).isNotEqualTo(h4);
        assertThat(h0).isNotEqualTo(h5);

        assertThat(h1).isNotEqualTo(h2);
        assertThat(h1).isNotEqualTo(h3);
        assertThat(h1).isNotEqualTo(h4);
        assertThat(h1).isNotEqualTo(h5);

        assertThat(h2).isEqualTo(h3);
        assertThat(h2).isNotEqualTo(h4);
        assertThat(h2).isNotEqualTo(h5);
    }

    @Test
    public void testEquals() {
        HashMap<String, Integer> h0 = HashMap.empty();
        HashMap<String, Integer> h1 = HashMap.of("foo", 1);
        HashMap<String, Integer> h2 = HashMap.of("foo", 1, "bar", 2, "baz", 3);
        HashMap<String, Integer> h3 = HashMap.of("bar", 2, "baz", 3, "foo", 1);
        HashMap<String, Integer> h4 = HashMap.of("foo", 1, "bar", 2, "baz", 4);
        HashMap<String, Double> h5 = HashMap.of("foo", 1.0, "bar", 2.0, "baz", 3.0);

        assertThat(h0).isNotEqualTo(h1);
        assertThat(h0).isNotEqualTo(h2);
        assertThat(h0).isNotEqualTo(h3);
        assertThat(h0).isNotEqualTo(h4);
        assertThat(h0).isNotEqualTo(h5);

        assertThat(h1).isNotEqualTo(h2);
        assertThat(h1).isNotEqualTo(h3);
        assertThat(h1).isNotEqualTo(h4);
        assertThat(h1).isNotEqualTo(h5);

        assertThat(h2).isEqualTo(h3);
        assertThat(h2).isNotEqualTo(h4);
        assertThat(h2).isNotEqualTo(h5);
    }

    @Test
    public void testToString() {
        assertThat(HashMap.empty().toString()).isEqualTo("HashMap[]");
        assertThat(HashMap.of(1, "foo", 2, "bar", 3, "baz").toString())
            .isEqualTo("HashMap[1:foo, 2:bar, 3:baz]");

    }
}