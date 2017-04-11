package org.neco4j.collect;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Daniel on 14.03.2016.
 */
public class ImmutableArrayTest {

    @Test
    public void testAppend() {
        ImmutableArray<String> first = ImmutableArray.of("foo","bar","baz");
        ImmutableArray<String> second = ImmutableArray.of("quux","foobar");
        ImmutableArray<String> array = ImmutableArray.append(first, second);
        String[] expected = {"foo","bar","baz","quux","foobar"};
        assertArrayEquals(expected, array.getArrayCopy());
    }

    @Test
    public void testMap() {
        ImmutableArray<String> strings = ImmutableArray.of("foo","bar","baz","quux","foobar");
        ImmutableArray<Integer> lengths = strings.map(String::length);
        Integer[] expected = {3,3,3,4,6};
        assertArrayEquals(expected, lengths.getArrayCopy());
    }
}