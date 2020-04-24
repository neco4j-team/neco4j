package org.neco4j.collect.unitkey;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OptTest {
    @Test
    public void none() {
        Opt<String> none = Opt.none();
        assertThat(none.isEmpty()).isTrue();
    }

    @Test
    public void some() {
        Opt<String> some = Opt.some("foo");
        assertThat(some.isEmpty()).isFalse();
        assertThat(some.getOrFail()).isEqualTo("foo");
    }

    @Test
    public void getOpt() {
        Opt<String> none = Opt.none();
        assertThat(none.getOpt()).isEqualTo(none);
        Opt<String> some = Opt.some("foo");
        assertThat(some.getOpt()).isEqualTo(some);
    }

    @Test
    public void from() {
        Opt<String> none = Opt.from(null);
        assertThat(none.isEmpty()).isTrue();
        Opt<String> some = Opt.from("foo");
        assertThat(some.getOrFail()).isEqualTo("foo");
    }

    @Test
    public void getOrFail() {
        Opt<String> none = Opt.none();
        assertThatThrownBy(none::getOrFail).isInstanceOf(NoSuchElementException.class);
        Opt<String> some = Opt.some("foo");
        assertThat(some.getOrFail()).isEqualTo("foo");
    }

    @Test
    public void getOrElse() {
        Opt<String> none = Opt.none();
        assertThat(none.getOrElse("bar")).isEqualTo("bar");
        Opt<String> some = Opt.some("foo");
        assertThat(some.getOrElse("bar")).isEqualTo("foo");
    }

    @Test
    public void or() {
        assertThat(Opt.none().or(Opt.none()).isEmpty()).isTrue();
        assertThat(Opt.some("foo").or(Opt.none()).getOrFail()).isEqualTo("foo");
        assertThat(Opt.none().or(Opt.some("bar")).getOrFail()).isEqualTo("bar");
        assertThat(Opt.some("foo").or(Opt.some("bar")).getOrFail()).isEqualTo("foo");
    }

    @Test
    public void hashCode_() {
        assertThat(Opt.none().hashCode()).isEqualTo(Opt.none().hashCode());
        assertThat(Opt.some("foo").hashCode()).isEqualTo(Opt.some("foo").hashCode());

        assertThat(Opt.some("foo").hashCode()).isNotEqualTo(Opt.some("bar").hashCode());
        assertThat(Opt.none().hashCode()).isNotEqualTo(Opt.some("bar").hashCode());
        assertThat(Opt.some("foo").hashCode()).isNotEqualTo(Opt.none().hashCode());
    }

    @Test
    public void equals() {
        assertThat(Opt.none()).isEqualTo(Opt.none());
        assertThat(Opt.some("foo")).isEqualTo(Opt.some("foo"));

        assertThat(Opt.some("foo")).isNotEqualTo(Opt.some("bar"));
        assertThat(Opt.none()).isNotEqualTo(Opt.some("bar"));
        assertThat(Opt.some("foo")).isNotEqualTo(Opt.none());
    }

    @Test
    public void toString_() {
        assertThat(Opt.none().toString()).isEqualTo("None");
        assertThat(Opt.some("foo").toString()).isEqualTo("Some(foo)");
    }

    @Test
    public void fromOptional() {
        assertThat(Opt.fromOptional(Optional.empty()).isEmpty()).isTrue();
        assertThat(Opt.fromOptional(Optional.of("foo")).getOrFail()).isEqualTo("foo");
    }

    @Test
    public void toOptional() {
        assertThat(Opt.none().toOptional().isPresent()).isFalse();
        assertThat(Opt.some("foo").toOptional().get()).isEqualTo("foo");
    }

    @Test
    public void addOpt() {
        assertThat(Opt.<String>none().addOpt("foo").getOrFail().getOrFail()).isEqualTo("foo");
        assertThat(Opt.some("bar").addOpt("foo").isEmpty()).isTrue();
    }

    @Test
    public void put() {
        assertThat(Opt.<String>none().put("foo")).contains("foo");
        assertThat(Opt.some("bar").put("foo")).contains("foo");
    }

    @Test
    public void map() {
        assertThat(Opt.<String>none().map(String::length).isEmpty()).isTrue();
        assertThat(Opt.some("bar").map(String::length).getOrFail()).isEqualTo(3);
    }

    @Test
    public void flatMap() {
        Function<Integer, Opt<Integer>> halfIfEven = n -> n % 2 == 0 ? Opt.some(n / 2) : Opt.none();
        assertThat(Opt.<Integer>none().flatMap(halfIfEven).isEmpty()).isTrue();
        assertThat(Opt.some(4).flatMap(halfIfEven).getOrFail()).isEqualTo(2);
        assertThat(Opt.some(3).flatMap(halfIfEven).isEmpty()).isTrue();
    }

}