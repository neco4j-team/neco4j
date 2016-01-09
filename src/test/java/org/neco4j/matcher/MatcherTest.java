package org.neco4j.matcher;


import org.junit.Test;
import org.neco4j.matcher.extractors.ClassExtractors;

import static org.junit.Assert.assertEquals;

public class MatcherTest {
    @Test
    public void testClassExtractor() {
        Object o = "dföldkf";
        String s = Matcher.match(o,
                ClassExtractors.isInstanceOf(Integer.class, i -> "i " + i),
                ClassExtractors.isInstanceOf(String.class, t -> "s " + t),
                Extractor.useDefault("hä?"));
        assertEquals(s, "s dföldkf");
    }
}