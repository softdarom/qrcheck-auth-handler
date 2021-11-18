package ru.softdarom.qrcheck.auth.handler.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j(topic = "UTIL")
public final class JsonHelper {

    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATE_TIME_PATTERN = String.format("%s %s", DATE_PATTERN, TIME_PATTERN);

    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        defaultConfigure();
    }

    public static <T> String asJson(T object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error mapping to json! Return an 'unknown' value!", e);
            return "unknown";
        }
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    private static void defaultConfigure() {
        MAPPER.registerModule(defaultTimeModule());
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private static JavaTimeModule defaultTimeModule() {
        var module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DEFAULT_DATE_TIME_FORMATTER));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(DEFAULT_DATE_FORMATTER));
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(DEFAULT_TIME_FORMATTER));
        return module;
    }
}