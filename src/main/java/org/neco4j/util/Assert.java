package org.neco4j.util;

public final class Assert {

    private Assert() {
    }

    public static void notNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object o) {
        notNull(o, "null value not allowed");
    }
}
