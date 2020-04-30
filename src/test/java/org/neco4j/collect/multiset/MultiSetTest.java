package org.neco4j.collect.multiset;

import org.junit.Test;
import org.neco4j.tuple.Pair;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class MultiSetTest {

    @Test
    public void empty() {
        MultiSet<String> ms = MultiSet.empty();
        assertThat(ms.size()).isEqualTo(0L);
        assertThat(ms.get("foo")).isEqualTo(0);
    }

    @Test
    public void of1() {
        MultiSet<String> ms = MultiSet.of("foo", 42);
        assertThat(ms.size()).isEqualTo(1L);
        assertThat(ms.get("foo")).isEqualTo(42);
        assertThat(ms.get("boink")).isEqualTo(0);
    }

    @Test
    public void of2() {
        MultiSet<String> ms = MultiSet.of("foo", 42, "bar", 23);
        assertThat(ms.size()).isEqualTo(2L);
        assertThat(ms.get("foo")).isEqualTo(42);
        assertThat(ms.get("bar")).isEqualTo(23);
        assertThat(ms.get("boink")).isEqualTo(0);
    }

    @Test
    public void of3() {
        MultiSet<String> ms = MultiSet.of("foo", 42, "bar", 23, "baz", 12);
        assertThat(ms.size()).isEqualTo(3L);
        assertThat(ms.get("foo")).isEqualTo(42);
        assertThat(ms.get("bar")).isEqualTo(23);
        assertThat(ms.get("baz")).isEqualTo(12);
        assertThat(ms.get("boink")).isEqualTo(0);
    }

    @Test
    public void of4() {
        MultiSet<String> ms = MultiSet.of("foo", 42, "bar", 23, "baz", 12, "quux", 2);
        assertThat(ms.size()).isEqualTo(4L);
        assertThat(ms.get("foo")).isEqualTo(42);
        assertThat(ms.get("bar")).isEqualTo(23);
        assertThat(ms.get("baz")).isEqualTo(12);
        assertThat(ms.get("quux")).isEqualTo(2);
        assertThat(ms.get("boink")).isEqualTo(0);
    }

    @Test
    public void of5() {
        MultiSet<String> ms = MultiSet.of("foo", 42, "bar", 23, "baz", 12, "quux", 2, "ping", 4);
        assertThat(ms.size()).isEqualTo(5L);
        assertThat(ms.get("foo")).isEqualTo(42);
        assertThat(ms.get("bar")).isEqualTo(23);
        assertThat(ms.get("baz")).isEqualTo(12);
        assertThat(ms.get("quux")).isEqualTo(2);
        assertThat(ms.get("ping")).isEqualTo(4);
        assertThat(ms.get("boink")).isEqualTo(0);
    }

    @Test
    public void of6() {
        MultiSet<String> ms = MultiSet.of("foo", 42, "bar", 23, "baz", 12, "quux", 2, "ping", 4, "pong", 5);
        assertThat(ms.size()).isEqualTo(6L);
        assertThat(ms.get("foo")).isEqualTo(42);
        assertThat(ms.get("bar")).isEqualTo(23);
        assertThat(ms.get("baz")).isEqualTo(12);
        assertThat(ms.get("quux")).isEqualTo(2);
        assertThat(ms.get("ping")).isEqualTo(4);
        assertThat(ms.get("pong")).isEqualTo(5);
        assertThat(ms.get("boink")).isEqualTo(0);
    }

    @Test
    public void ofAll() {
        MultiSet<String> ms = MultiSet.ofAll(Arrays.asList("foo", "bar", "baz", "bar"));
        assertThat(ms.size()).isEqualTo(3L);
        assertThat(ms.asKeyValuePairs()).containsExactlyInAnyOrder(
            Pair.of("foo", 1), Pair.of("bar", 2), Pair.of("baz", 1));
    }

    @Test
    public void addOpt() {
        MultiSet<String> ms = MultiSet.empty();
        assertThat(ms.addOpt("foo", 0).getOrFail().asKeyValuePairs()).isEmpty();
        assertThat(ms.addOpt("foo", 42).getOrFail().asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 42));
        assertThat(ms.addOpt("foo", -1)).isEmpty();

        ms = MultiSet.of("foo", 42, "bar", 23);
        assertThat(ms.addOpt("foo", 0).getOrFail().asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 42), Pair.of("bar", 23));
        assertThat(ms.addOpt("foo", 2).getOrFail().asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 44), Pair.of("bar", 23));
        assertThat(ms.addOpt("foo", -2).getOrFail().asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 40), Pair.of("bar", 23));
        assertThat(ms.addOpt("foo", -42).getOrFail().asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("bar", 23));
        assertThat(ms.addOpt("foo", -43)).isEmpty();

        assertThat(ms.addOpt("baz", 0).getOrFail().asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 42), Pair.of("bar", 23));
        assertThat(ms.addOpt("baz", 13).getOrFail().asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 42), Pair.of("bar", 23), Pair.of("baz", 13));
        assertThat(ms.addOpt("baz", -1)).isEmpty();
    }

    @Test
    public void containsKey() {
        assertThat(MultiSet.empty().containsKey("foo")).isFalse();
        assertThat(MultiSet.of("foo", 42, "bar", 23).containsKey("foo")).isTrue();
        assertThat(MultiSet.of("foo", 0, "bar", 23).containsKey("foo")).isFalse();
        assertThat(MultiSet.of("foo", 42, "bar", 23).containsKey("baz")).isFalse();
    }

    @Test
    public void asKeyValuePairs() {
        assertThat(MultiSet.empty().asKeyValuePairs()).isEmpty();
        assertThat(MultiSet.of("foo", 42, "bar", 23, "baz", 13).asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 42), Pair.of("bar", 23), Pair.of("baz", 13));
    }

    @Test
    public void get() {
        assertThat(MultiSet.empty().get("foo")).isEqualTo(0);
        assertThat(MultiSet.of("foo", 42, "bar", 23).get("foo")).isEqualTo(42);
        assertThat(MultiSet.of("foo", 0, "bar", 23).get("foo")).isEqualTo(0);
        assertThat(MultiSet.of("foo", 42, "bar", 23).get("baz")).isEqualTo(0);
    }

    @Test
    public void stream() {
        assertThat(MultiSet.empty().stream()).isEmpty();
        assertThat(MultiSet.of("foo", 42, "bar", 23, "baz", 13).stream())
            .containsExactlyInAnyOrder(Pair.of("foo", 42), Pair.of("bar", 23), Pair.of("baz", 13));
    }

    @Test
    public void removeOpt() {
        assertThat(MultiSet.empty().removeOpt("foo")).isEmpty();
        assertThat(MultiSet.of("foo", 42, "bar", 23).removeOpt("baz")).isEmpty();
        assertThat(MultiSet.of("foo", 42, "bar", 23).removeOpt("foo").getOrFail().asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("bar", 23));

    }

    @Test
    public void size() {
        assertThat(MultiSet.empty().size()).isEqualTo(0L);

        MultiSet<String> ms = MultiSet.of("foo", 42, "bar", 23, "baz", 12, "quux", 2, "ping", 4, "pong", 5);
        assertThat(ms.size()).isEqualTo(6L);
        assertThat(ms.removeIfPossible("foo").size()).isEqualTo(5L);
        assertThat(ms.addIfPossible("foo", 13).size()).isEqualTo(6L);
        assertThat(ms.addIfPossible("dong", 13).size()).isEqualTo(7L);
    }

    @Test
    public void put() {
        MultiSet<String> mse = MultiSet.empty();
        assertThat(mse.put("foo", 0).asKeyValuePairs()).isEmpty();
        assertThat(mse.put("foo", 42).asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 42));
        assertThatThrownBy(() -> mse.put("foo", -1)).isInstanceOf(IllegalArgumentException.class);

        MultiSet<String> ms = MultiSet.of("foo", 42, "bar", 23);
        assertThat(ms.put("foo", 0).asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("bar", 23));
        assertThat(ms.put("foo", 2).asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 2), Pair.of("bar", 23));
        assertThatThrownBy(() -> ms.put("foo", -2)).isInstanceOf(IllegalArgumentException.class);

        assertThat(ms.put("baz", 0).asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 42), Pair.of("bar", 23));
        assertThat(ms.put("baz", 13).asKeyValuePairs())
            .containsExactlyInAnyOrder(Pair.of("foo", 42), Pair.of("bar", 23), Pair.of("baz", 13));
        assertThatThrownBy(() -> ms.put("baz", -1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void keys() {
        assertThat(MultiSet.empty().keys()).isEmpty();
        assertThat(MultiSet.of("foo", 42, "bar", 23, "baz", 13).keys())
            .containsExactlyInAnyOrder("foo", "bar", "baz");
    }

    @Test
    public void values() {
        assertThat(MultiSet.empty().values()).isEmpty();
        assertThat(MultiSet.of("foo", 42, "bar", 23, "baz", 23).values())
            .containsExactlyInAnyOrder(42, 23, 23);
    }

    @Test
    public void testEquals() {
        MultiSet<String> ms1 = MultiSet.empty();
        MultiSet<String> ms2 = MultiSet.of("foo", 42, "bar", 23, "baz", 13);
        MultiSet<String> ms3 = MultiSet.of("bar", 23, "foo", 42, "baz", 13);
        MultiSet<String> ms4 = MultiSet.of("foo", 42, "bar", 23, "baz", 23);
        MultiSet<String> ms5 = MultiSet.of("foo", 42, "bar", 23, "bax", 13);
        MultiSet<String> ms6 = MultiSet.of("foo", 42, "bar", 23, "baz", 13, "quux", 4);

        assertThat(ms1).isNotEqualTo(ms2);
        assertThat(ms1).isNotEqualTo(ms3);
        assertThat(ms1).isNotEqualTo(ms4);
        assertThat(ms1).isNotEqualTo(ms5);
        assertThat(ms1).isNotEqualTo(ms6);

        assertThat(ms2).isEqualTo(ms3);
        assertThat(ms2).isNotEqualTo(ms4);
        assertThat(ms2).isNotEqualTo(ms5);
        assertThat(ms2).isNotEqualTo(ms6);
    }

    @Test
    public void testHashCode() {
        int hc1 = MultiSet.empty().hashCode();
        int hc2 = MultiSet.of("foo", 42, "bar", 23, "baz", 13).hashCode();
        int hc3 = MultiSet.of("bar", 23, "foo", 42, "baz", 13).hashCode();
        int hc4 = MultiSet.of("foo", 42, "bar", 23, "baz", 23).hashCode();
        int hc5 = MultiSet.of("foo", 42, "bar", 23, "bax", 13).hashCode();
        int hc6 = MultiSet.of("foo", 42, "bar", 23, "baz", 13, "quux", 4).hashCode();

        assertThat(hc1).isNotEqualTo(hc2);
        assertThat(hc1).isNotEqualTo(hc3);
        assertThat(hc1).isNotEqualTo(hc4);
        assertThat(hc1).isNotEqualTo(hc5);
        assertThat(hc1).isNotEqualTo(hc6);

        assertThat(hc2).isEqualTo(hc3);
        assertThat(hc2).isNotEqualTo(hc4);
        assertThat(hc2).isNotEqualTo(hc5);
        assertThat(hc2).isNotEqualTo(hc6);
    }

    @Test
    public void testToString() {
        assertThat(MultiSet.empty().toString()).isEqualTo("MultiSet[]");
        assertThat(MultiSet.of("foo", 42, "bar", 23).toString())
            .isIn("MultiSet[foo:42, bar:23]", "MultiSet[bar:23, foo:42]");
    }
}