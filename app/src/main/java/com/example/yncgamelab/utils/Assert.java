package io.github.bbodin.yncgamelab.utils;

public class Assert {
    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
    public static void notNull(Object o, String message) {
        if (o == null) {
            throw new AssertionError(message);
        }
    }

}