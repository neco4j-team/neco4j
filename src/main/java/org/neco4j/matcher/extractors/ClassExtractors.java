package org.neco4j.matcher.extractors;

import org.neco4j.matcher.Extractor;

import java.util.function.Function;

public final class ClassExtractors {
     private ClassExtractors() { throw new UnsupportedOperationException(); }

     @SuppressWarnings("unchecked")
     public static <A, R> Extractor<Object, R> isInstanceOf(Class<A> clazz, Function<? super A, ? extends R> fn) {
         return Extractor.create(clazz::isInstance, obj -> fn.apply((A) obj));
     }
}
