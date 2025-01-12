package com.tutorials.stripe.utils;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class UnixDateConverter {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss z");

    public static String convert(long unixTime) {

        if (unixTime < 0 || unixTime > Instant.MAX.getEpochSecond()) {
            throw new DateTimeException("Invalid unix time: " + unixTime);
        }

        return Instant.ofEpochSecond(unixTime)
                .atZone(ZoneId.systemDefault())
                .format(FORMATTER);

    }
}
