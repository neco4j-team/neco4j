package org.neco4j.function;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class QuadFunctionTest {
    @Test
    public void testAndThen() throws Exception {
        QuadFunction<String, String, String, String, String> fn = (t, u, v, w) -> t + u + v + w;
        assertEquals("abcd", fn.apply("a", "b", "c", "d"));
        QuadFunction<String, String, String, String, String> fnAndThen = fn.andThen(s -> s + "x");
        assertEquals("abcdx", fnAndThen.apply("a", "b", "c", "d"));
    }
}
