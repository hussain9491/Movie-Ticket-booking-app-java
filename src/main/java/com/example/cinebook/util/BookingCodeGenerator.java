package com.example.cinebook.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class BookingCodeGenerator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;

    private BookingCodeGenerator() {
    }

    public static String nextCode() {
        int value = ThreadLocalRandom.current().nextInt(1000, 10000);
        return "CB-" + LocalDate.now().format(FORMATTER) + "-" + value;
    }
}

