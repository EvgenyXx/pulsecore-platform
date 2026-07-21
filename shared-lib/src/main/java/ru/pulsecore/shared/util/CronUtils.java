package ru.pulsecore.shared.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CronUtils {

    public static String everyMinutes(int minutes) {
        return String.format("0 */%d * * * *", minutes);
    }
}
