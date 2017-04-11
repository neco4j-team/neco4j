package org.neco4j.collect;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

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
    public void fromOptional() {
        assertThat(Opt.fromOptional(Optional.empty()).isEmpty()).isTrue();
        assertThat(Opt.fromOptional(Optional.of("foo")).getOrFail()).isEqualTo("foo");
    }

    @Test
    public void toOptional() {
        assertThat(Opt.none().toOptional().isPresent()).isFalse();
        assertThat(Opt.some("foo").toOptional().get()).isEqualTo("foo");
    }

}