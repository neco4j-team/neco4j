package org.neco4j.either;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class EitherTest {

    @Test
    public void testLeftOf() throws Exception {
        Left<Integer, String> left = Either.leftOf(42);
        assertEquals(Integer.valueOf(42), left.left());
    }

    @Test
    public void testRightOf() throws Exception {
        Right<Integer, String> right = Either.rightOf("foo");
        assertEquals("foo", right.right());
    }

    @Test
    public void testIsLeft() throws Exception {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertTrue(left.isLeft());

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertFalse(right.isLeft());
    }

    @Test
    public void testIsRight() throws Exception {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertFalse(left.isRight());

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertTrue(right.isRight());
    }

    @Test
    public void testLeftOnLeft() throws Exception {
        Either<Integer, String> left = Either.leftOf(42);
        assertEquals(Integer.valueOf(42), left.left());
    }

    @Test(expected = NoSuchElementException.class)
    public void testLeftOnRight() throws Exception {
        Either<Integer, String> right = Either.rightOf("foo");
        right.left();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRightOnLeft() throws Exception {
        Either<Integer, String> left = Either.leftOf(42);
        left.right();
    }

    @Test
    public void testRightOnRight() throws Exception {
        Either<Integer, String> right = Either.rightOf("foo");
        assertEquals("foo", right.right());
    }

    @Test
    public void testLeftOrElse() throws Exception {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertEquals(Integer.valueOf(42), left.leftOrElse(4711));

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertEquals(Integer.valueOf(4711), right.leftOrElse(4711));
    }

    @Test
    public void testRightOrElse() throws Exception {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertEquals("bar", left.rightOrElse("bar"));

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertEquals("foo", right.rightOrElse("bar"));
    }

    @Test
    public void testLeftOrElseGet() throws Exception {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertEquals(Integer.valueOf(42), left.leftOrElseGet(() -> 4711));

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertEquals(Integer.valueOf(4711), right.leftOrElseGet(() -> 4711));
    }

    @Test
    public void testRightOrElseGet() throws Exception {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertEquals("bar", left.rightOrElseGet(() -> "bar"));

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertEquals("foo", right.rightOrElseGet(() -> "bar"));
    }

    @Test
    public void testLeftOpt() throws Exception {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertEquals(Integer.valueOf(42), left.leftOpt().getOrFail());

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertTrue(right.leftOpt().isEmpty());
    }

    @Test
    public void testRightOpt() throws Exception {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertTrue(left.rightOpt().isEmpty());

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertEquals("foo", right.rightOpt().getOrFail());
    }

    @Test
    public void testMapLeft() throws Exception {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        Either<String, String> mappedLeft = left.mapLeft(x -> "answer" + x);
        assertEquals("answer42", mappedLeft.left());

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        Either<String, String> mappedRight = right.mapLeft(x -> "answer" + x);
        assertEquals("foo", mappedRight.right());
    }

    @Test
    public void testMapRight() throws Exception {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        Either<Integer, Integer> mappedLeft = left.mapRight(String::length);
        assertEquals(Integer.valueOf(42), mappedLeft.left());

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        Either<Integer, Integer> mappedRight = right.mapRight(String::length);
        assertEquals(Integer.valueOf(3), mappedRight.right());
    }

    @Test
    public void testBimap() throws Exception {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        Either<String, Integer> mappedLeft = left.bimap(x -> "answer" + x, String::length);
        assertEquals("answer42", mappedLeft.left());

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        Either<String, Integer> mappedRight = right.bimap(x -> "answer" + x, String::length);
        assertEquals(Integer.valueOf(3), mappedRight.right());
    }

    @Test
    public void testEither() throws Exception {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        int valueLeft = left.either(x -> x / 2, String::length);
        assertEquals(21, valueLeft);

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        int valueRight = right.either(x -> x / 2, String::length);
        assertEquals(3, valueRight);
    }

    @Test
    public void testSwap() throws Exception {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        Either<String, Integer> swappedLeft = left.swap();
        assertEquals(Integer.valueOf(42), swappedLeft.right());

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        Either<String, Integer> swappedRight = right.swap();
        assertEquals("foo", swappedRight.left());
    }

    @Test
    public void testEquals() throws Exception {
        //between two Lefts
        Either<Integer, String> left42a = Either.leftOf(42);
        Either<Integer, String> left42b = Either.leftOf(42);
        Either<Integer, String> left43 = Either.leftOf(43);
        assertTrue(left42a.equals(left42a));
        assertTrue(left42a.equals(left42b));
        assertFalse(left42a.equals(left43));

        //between two Rights
        Either<Integer, String> rightFoo1 = Either.rightOf("foo");
        Either<Integer, String> rightFoo2 = Either.rightOf("foo");
        Either<Integer, String> rightBar = Either.rightOf("bar");
        assertTrue(rightFoo1.equals(rightFoo1));
        assertTrue(rightFoo1.equals(rightFoo2));
        assertFalse(rightFoo1.equals(rightBar));

        //between a Left and a Right
        assertFalse(left43.equals(rightBar));
        Either<String, String> leftBar = Either.leftOf("bar");
        assertFalse(leftBar.equals(rightBar));
    }

    @Test
    public void testHashCode() throws Exception {
        //between two Lefts
        Either<Integer, String> left42a = Either.leftOf(42);
        Either<Integer, String> left42b = Either.leftOf(42);
        assertTrue(left42a.hashCode() == left42a.hashCode());
        assertTrue(left42a.hashCode() == left42b.hashCode());

        //between two Rights
        Either<Integer, String> rightFoo1 = Either.rightOf("foo");
        Either<Integer, String> rightFoo2 = Either.rightOf("foo");
        assertTrue(rightFoo1.hashCode() == rightFoo1.hashCode());
        assertTrue(rightFoo1.hashCode() == rightFoo2.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        Either<Integer, String> left = Either.leftOf(42);
        assertEquals("Left(42)", left.toString());
        Either<Integer, String> right = Either.rightOf("foo");
        assertEquals("Right(foo)", right.toString());
    }
}
