package org.neco4j.function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TriFunctionTest {
    @Test
    public void testAndThen() {
        TriFunction<String, String, String, String> fn = (t, u, v) -> t + u + v;
        assertThat(fn.apply("a", "b", "c")).isEqualTo("abc");
        TriFunction<String, String, String, String> fnAndThen = fn.andThen(s -> s + "x");
        assertThat(fnAndThen.apply("a", "b", "c")).isEqualTo("abcx");
    }
}