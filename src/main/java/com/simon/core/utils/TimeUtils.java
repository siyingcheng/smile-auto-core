package com.simon.core.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUtils {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
