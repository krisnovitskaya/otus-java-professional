package ru.otus.common;

import java.util.concurrent.TimeUnit;

public class GRPCNumberUtil {
    public static void sleep(long duration) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(duration));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
