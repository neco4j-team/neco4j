package org.neco4j.matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Matcher {
    private Matcher() {
        throw new UnsupportedOperationException();
    }

    @SafeVarargs
    public static <A, R> R match(A a, Extractor<? super A, ? extends R>... extractors) throws MatchException {
        for (Extractor<? super A, ? extends R> extractor : extractors) {
            Optional<? extends R> result = extractor.unapply(a);
            if (result.isPresent()) {
                return result.get();
            }
        }
        throw new MatchException();
    }

    @SafeVarargs
    public static <A, R> List<R> matchAll(A a, Extractor<? super A, ? extends R>... extractors) {
        List<R> list = new ArrayList<>();
        for (Extractor<? super A, ? extends R> extractor : extractors) {
            extractor.unapply(a).ifPresent(list::add);
        }
        return list;
    }

    @SafeVarargs
    public static <A, B, R> R biMatch(A a, B b, BiExtractor<? super A, ? super B, ? extends R>... biExtractors) throws MatchException {
        for (BiExtractor<? super A, ? super B, ? extends R> biExtractor : biExtractors) {
            Optional<? extends R> result = biExtractor.unapply(a, b);
            if (result.isPresent()) {
                return result.get();
            }
        }
        throw new MatchException();
    }

    @SafeVarargs
    public static <A, B, R> List<R> biMatchAll(A a, B b, BiExtractor<? super A, ? super B, ? extends R>... biExtractors) {
        List<R> list = new ArrayList<>();
        for (BiExtractor<? super A, ? super B, ? extends R> biExtractor : biExtractors) {
            biExtractor.unapply(a, b).ifPresent(list::add);
        }
        return list;
    }

}
