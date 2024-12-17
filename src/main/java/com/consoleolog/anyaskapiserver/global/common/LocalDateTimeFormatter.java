package com.consoleolog.anyaskapiserver.global.common;

import org.springframework.lang.NonNull;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {
    @Override
    public @NonNull LocalDateTime parse(@NonNull String text, @NonNull Locale locale) throws ParseException {
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @Override
    public @NonNull String print(@NonNull LocalDateTime object, @NonNull Locale locale) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(object);
    }
}
