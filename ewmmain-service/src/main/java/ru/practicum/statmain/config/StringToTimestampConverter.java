package ru.practicum.statmain.config;

import org.springframework.core.convert.converter.Converter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToTimestampConverter implements Converter<String, LocalDateTime> {

    private static final DateTimeFormatter DATETIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm[:ss]");

    @Override
    public LocalDateTime convert(String source) {
        try {
            String decoded = URLDecoder.decode(source, "UTF-8");
            LocalDateTime result = LocalDateTime.parse(decoded, DATETIME_PATTERN);
            return result;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Cannot decode string");
        }
    }

    @Override
    public <U> Converter<String, U> andThen(Converter<? super LocalDateTime, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
