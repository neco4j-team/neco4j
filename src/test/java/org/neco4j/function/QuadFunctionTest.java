package org.neco4j.function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuadFunctionTest {
    @Test
    public void testAndThen() {
        QuadFunction<String, String, String, String, String> fn = (t, u, v, w) -> t + u + v + w;
        assertThat(fn.apply("a", "b", "c", "d")).isEqualTo("abcd");
        QuadFunction<String, String, String, String, String> fnAndThen = fn.andThen(s -> s + "x");
        assertThat(fnAndThen.apply("a", "b", "c", "d")).isEqualTo("abcdx");
    }
}