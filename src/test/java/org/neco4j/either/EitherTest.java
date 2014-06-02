package org.neco4j.either;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class EitherTest {

    @Test
    public void testLeft() throws Exception {
        Left<Integer, String> left = Either.left(42);
        assertEquals(Integer.valueOf(42), left.getLeft());
    }

    @Test
    public void testLazyLeft() throws Exception {
        boolean[] array = {false};
        Left<Integer, String> left = Either.lazyLeft(() -> {
            array[0] = true;
            return 42;
        });
        assertFalse(array[0]); //check that nothing was calculated yet
        assertEquals(Integer.valueOf(42), left.getLeft());
        assertTrue(array[0]);
    }

    @Test
    public void testRight() throws Exception {
        Right<Integer, String> right = Either.right("foo");
        assertEquals("foo", right.getRight());
    }

    @Test
    public void testLazyRight() throws Exception {
        boolean[] array = {false};
        Right<Integer, String> right = Either.lazyRight(() -> {
            array[0] = true;
            return "foo";
        });
        assertFalse(array[0]); //check that nothing was calculated yet
        assertEquals("foo", right.getRight());
        assertTrue(array[0]);
    }

    @Test
    public void testIsLeft() throws Exception {
        //Left
        Either<Integer, String> left = Either.left(42);
        assertTrue(left.isLeft());

        //Right
        Either<Integer, String> right = Either.right("foo");
        assertFalse(right.isLeft());
    }

    @Test
    public void testIsRight() throws Exception {
        //Left
        Either<Integer, String> left = Either.left(42);
        assertFalse(left.isRight());

        //Right
        Either<Integer, String> right = Either.right("foo");
        assertTrue(right.isRight());
    }

    @Test
    public void testGetLeftOnLeft() throws Exception {
        Either<Integer, String> left = Either.left(42);
        assertEquals(Integer.valueOf(42), left.getLeft());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetLeftOnRight() throws Exception {
        Either<Integer, String> right = Either.right("foo");
        right.getLeft();
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetRightOnLeft() throws Exception {
        Either<Integer, String> left = Either.left(42);
        left.getRight();
    }

    @Test
    public void testGetRightOnRight() throws Exception {
        Either<Integer, String> right = Either.right("foo");
        assertEquals("foo", right.getRight());
    }

    @Test
    public void testLeftOrElse() throws Exception {
        //Left
        Either<Integer, String> left = Either.left(42);
        assertEquals(Integer.valueOf(42), left.leftOrElse(4711));

        //Right
        Either<Integer, String> right = Either.right("foo");
        assertEquals(Integer.valueOf(4711), right.leftOrElse(4711));
    }

    @Test
    public void testRightOrElse() throws Exception {
        //Left
        Either<Integer, String> left = Either.left(42);
        assertEquals("bar", left.rightOrElse("bar"));

        //Right
        Either<Integer, String> right = Either.right("foo");
        assertEquals("foo", right.rightOrElse("bar"));
    }

    @Test
    public void testLeftOrElseGet() throws Exception {
        //Left
        Either<Integer, String> left = Either.left(42);
        assertEquals(Integer.valueOf(42), left.leftOrElseGet(() -> 4711));

        //Right
        Either<Integer, String> right = Either.right("foo");
        assertEquals(Integer.valueOf(4711), right.leftOrElseGet(() -> 4711));
    }

    @Test
    public void testRightOrElseGet() throws Exception {
        //Left
        Either<Integer, String> left = Either.left(42);
        assertEquals("bar", left.rightOrElseGet(() -> "bar"));

        //Right
        Either<Integer, String> right = Either.right("foo");
        assertEquals("foo", right.rightOrElseGet(() -> "bar"));
    }

    @Test
    public void testGetLeftOpt() throws Exception {
        //Left
        Either<Integer, String> left = Either.left(42);
        assertEquals(Integer.valueOf(42), left.getLeftOpt().get());

        //Right
        Either<Integer, String> right = Either.right("foo");
        assertFalse(right.getLeftOpt().isPresent());
    }

    @Test
    public void testGetRightOpt() throws Exception {
        //Left
        Either<Integer, String> left = Either.left(42);
        assertFalse(left.getRightOpt().isPresent());

        //Right
        Either<Integer, String> right = Either.right("foo");
        assertEquals("foo", right.getRightOpt().get());
    }

    @Test
    public void testMapLeft() throws Exception {
        //Left
        Either<Integer, String> left = Either.left(42);
        Either<String, String> mappedLeft = left.mapLeft(x -> "answer" + x);
        assertEquals("answer42", mappedLeft.getLeft());

        //Right
        Either<Integer, String> right = Either.right("foo");
        Either<String, String> mappedRight = right.mapLeft(x -> "answer" + x);
        assertEquals("foo", mappedRight.getRight());
    }

    @Test
    public void testMapRight() throws Exception {
        //Left
        Either<Integer, String> left = Either.left(42);
        Either<Integer, Integer> mappedLeft = left.mapRight(String::length);
        assertEquals(Integer.valueOf(42), mappedLeft.getLeft());

        //Right
        Either<Integer, String> right = Either.right("foo");
        Either<Integer, Integer> mappedRight = right.mapRight(String::length);
        assertEquals(Integer.valueOf(3), mappedRight.getRight());
    }

    @Test
    public void testBimap() throws Exception {
        //Left
        Either<Integer, String> left = Either.left(42);
        Either<String, Integer> mappedLeft = left.bimap(x -> "answer" + x, String::length);
        assertEquals("answer42", mappedLeft.getLeft());

        //Right
        Either<Integer, String> right = Either.right("foo");
        Either<String, Integer> mappedRight = right.bimap(x -> "answer" + x, String::length);
        assertEquals(Integer.valueOf(3), mappedRight.getRight());
    }

    @Test
    public void testEither() throws Exception {
        //Left
        Either<Integer, String> left = Either.left(42);
        int valueLeft = left.either(x -> x / 2, String::length);
        assertEquals(21, valueLeft);

        //Right
        Either<Integer, String> right = Either.right("foo");
        int valueRight = right.either(x -> x / 2, String::length);
        assertEquals(3, valueRight);
    }

    @Test
    public void testSwap() throws Exception {
        //Left
        Either<Integer, String> left = Either.left(42);
        Either<String, Integer> swappedLeft = left.swap();
        assertEquals(Integer.valueOf(42), swappedLeft.getRight());

        //Right
        Either<Integer, String> right = Either.right("foo");
        Either<String, Integer> swappedRight = right.swap();
        assertEquals("foo", swappedRight.getLeft());
    }

    @Test
    public void testEquals() throws Exception {
        //between two Lefts
        Either<Integer, String> left42a = Either.left(42);
        Either<Integer, String> left42b = Either.left(42);
        Either<Integer, String> left43 = Either.left(43);
        assertTrue(left42a.equals(left42a));
        assertTrue(left42a.equals(left42b));
        assertFalse(left42a.equals(left43));

        //between two Rights
        Either<Integer, String> rightFoo1 = Either.right("foo");
        Either<Integer, String> rightFoo2 = Either.right("foo");
        Either<Integer, String> rightBar = Either.right("bar");
        assertTrue(rightFoo1.equals(rightFoo1));
        assertTrue(rightFoo1.equals(rightFoo2));
        assertFalse(rightFoo1.equals(rightBar));

        //between a Left and a Right
        assertFalse(left43.equals(rightBar));
        Either<String, String> leftBar = Either.left("bar");
        assertFalse(leftBar.equals(rightBar));
    }

    @Test
    public void testHashCode() throws Exception {
        //between two Lefts
        Either<Integer, String> left42a = Either.left(42);
        Either<Integer, String> left42b = Either.left(42);
        Either<Integer, String> left43 = Either.left(43);
        assertTrue(left42a.hashCode() == left42a.hashCode());
        assertTrue(left42a.hashCode() == left42b.hashCode());
        assertFalse(left42a.hashCode() == left43.hashCode());

        //between two Rights
        Either<Integer, String> rightFoo1 = Either.right("foo");
        Either<Integer, String> rightFoo2 = Either.right("foo");
        Either<Integer, String> rightBar = Either.right("bar");
        assertTrue(rightFoo1.hashCode() == rightFoo1.hashCode());
        assertTrue(rightFoo1.hashCode() == rightFoo2.hashCode());
        assertFalse(rightFoo1.hashCode() == rightBar.hashCode());

        //between a Left and a Right
        assertFalse(left43.hashCode() == rightBar.hashCode());
        Either<String, String> leftBar = Either.left("bar");
        assertFalse(leftBar.hashCode() == rightBar.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        Either<Integer, String> left = Either.left(42);
        assertEquals("Left(42)", left.toString());
        Either<Integer, String> right = Either.right("foo");
        assertEquals("Right(foo)", right.toString());
    }

}
