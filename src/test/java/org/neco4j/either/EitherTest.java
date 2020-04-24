package org.neco4j.either;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class EitherTest {

    @Test
    public void testLeftOf() {
        Either<Integer, String> left = Either.leftOf(42);
        assertThat(left.leftOrFail()).isEqualTo(42);
    }

    @Test
    public void testRightOf() {
        Either<Integer, String> right = Either.rightOf("foo");
        assertThat(right.rightOrFail()).isEqualTo("foo");
    }

    @Test
    public void testIsLeft() {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertThat(left.isLeft()).isTrue();

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertThat(right.isLeft()).isFalse();
    }

    @Test
    public void testIsRight() {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertThat(left.isRight()).isFalse();

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertThat(right.isRight()).isTrue();
    }

    @Test
    public void testLeftOnLeft() {
        Either<Integer, String> left = Either.leftOf(42);
        assertThat(left.leftOrFail()).isEqualTo(42);
    }

    @Test
    public void testLeftOnRight() {
        Either<Integer, String> right = Either.rightOf("foo");
        assertThatThrownBy(right::leftOrFail).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void testRightOnLeft() {
        Either<Integer, String> left = Either.leftOf(42);
        assertThatThrownBy(left::rightOrFail).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void testRightOnRight() {
        Either<Integer, String> right = Either.rightOf("foo");
        assertThat(right.rightOrFail()).isEqualTo("foo");
    }

    @Test
    public void testLeftOrElse() {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertThat(left.leftOrElse(4711)).isEqualTo(42);

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertThat(right.leftOrElse(4711)).isEqualTo(4711);
    }

    @Test
    public void testRightOrElse() {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertThat(left.rightOrElse("bar")).isEqualTo("bar");

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertThat(right.rightOrElse("bar")).isEqualTo("foo");
    }

    @Test
    public void testLeftOrElseGet() {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertThat(left.leftOrElseGet(() -> 4711)).isEqualTo(42);

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertThat(right.leftOrElseGet(() -> 4711)).isEqualTo(4711);
    }

    @Test
    public void testRightOrElseGet() {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertThat(left.rightOrElseGet(() -> "bar")).isEqualTo("bar");

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertThat(right.rightOrElseGet(() -> "bar")).isEqualTo("foo");
    }

    @Test
    public void testLeftOpt() {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertThat(left.leftOpt().getOrFail()).isEqualTo(42);

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertThat(right.leftOpt().isEmpty()).isTrue();
    }

    @Test
    public void testRightOpt() {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        assertThat(left.rightOpt().isEmpty()).isTrue();

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        assertThat(right.rightOpt().getOrFail()).isEqualTo("foo");
    }

    @Test
    public void testMapLeft() {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        Either<String, String> mappedLeft = left.mapLeft(x -> "answer" + x);
        assertThat(mappedLeft.leftOrFail()).isEqualTo("answer42");

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        Either<String, String> mappedRight = right.mapLeft(x -> "answer" + x);
        assertThat(mappedRight.rightOrFail()).isEqualTo("foo");
    }

    @Test
    public void testMapRight() {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        Either<Integer, Integer> mappedLeft = left.mapRight(String::length);
        assertThat(mappedLeft.leftOrFail()).isEqualTo(42);

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        Either<Integer, Integer> mappedRight = right.mapRight(String::length);
        assertThat(mappedRight.rightOrFail()).isEqualTo(3);
    }

    @Test
    public void testBimap() {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        Either<String, Integer> mappedLeft = left.bimap(x -> "answer" + x, String::length);
        assertThat(mappedLeft.leftOrFail()).isEqualTo("answer42");

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        Either<String, Integer> mappedRight = right.bimap(x -> "answer" + x, String::length);
        assertThat(mappedRight.rightOrFail()).isEqualTo(3);
    }

    @Test
    public void testEither() {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        int valueLeft = left.either(x -> x / 2, String::length);
        assertThat(valueLeft).isEqualTo(21);

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        int valueRight = right.either(x -> x / 2, String::length);
        assertThat(valueRight).isEqualTo(3);
    }

    @Test
    public void testSwap() {
        //Left
        Either<Integer, String> left = Either.leftOf(42);
        Either<String, Integer> swappedLeft = left.swap();
        assertThat(swappedLeft.rightOrFail()).isEqualTo(42);

        //Right
        Either<Integer, String> right = Either.rightOf("foo");
        Either<String, Integer> swappedRight = right.swap();
        assertThat(swappedRight.leftOrFail()).isEqualTo("foo");
    }

    @Test
    public void testEquals() {
        //between two Lefts
        Either<Integer, String> left42a = Either.leftOf(42);
        Either<Integer, String> left42b = Either.leftOf(42);
        Either<Integer, String> left43 = Either.leftOf(43);
        assertThat(left42a.equals(left42a)).isTrue();
        assertThat(left42a.equals(left42b)).isTrue();
        assertThat(left42a.equals(left43)).isFalse();

        //between two Rights
        Either<Integer, String> rightFoo1 = Either.rightOf("foo");
        Either<Integer, String> rightFoo2 = Either.rightOf("foo");
        Either<Integer, String> rightBar = Either.rightOf("bar");
        assertThat(rightFoo1.equals(rightFoo1)).isTrue();
        assertThat(rightFoo1.equals(rightFoo2)).isTrue();
        assertThat(rightFoo1.equals(rightBar)).isFalse();

        //between a Left and a Right
        assertThat(left43.equals(rightBar)).isFalse();
        Either<String, String> leftBar = Either.leftOf("bar");
        assertThat(leftBar.equals(rightBar)).isFalse();
    }

    @Test
    public void testHashCode() {
        //between two Lefts
        Either<Integer, String> left42a = Either.leftOf(42);
        Either<Integer, String> left42b = Either.leftOf(42);
        Either<Integer, String> left43 = Either.leftOf(43);
        assertThat(left42a.hashCode() == left42a.hashCode()).isTrue();
        assertThat(left42a.hashCode() == left42b.hashCode()).isTrue();
        assertThat(left42a.hashCode() == left43.hashCode()).isFalse();

        //between two Rights
        Either<Integer, String> rightFoo1 = Either.rightOf("foo");
        Either<Integer, String> rightFoo2 = Either.rightOf("foo");
        Either<Integer, String> rightBar = Either.rightOf("bar");
        assertThat(rightFoo1.hashCode() == rightFoo1.hashCode()).isTrue();
        assertThat(rightFoo1.hashCode() == rightFoo2.hashCode()).isTrue();
        assertThat(rightFoo1.hashCode() == rightBar.hashCode()).isFalse();

        //between Left and Right
        assertThat(left42a.hashCode() == rightFoo1.hashCode()).isFalse();
    }

    @Test
    public void testToString() {
        Either<Integer, String> left = Either.leftOf(42);
        assertThat(left.toString()).isEqualTo("Left(42)");
        Either<Integer, String> right = Either.rightOf("foo");
        assertThat(right.toString()).isEqualTo("Right(foo)");
    }
}
