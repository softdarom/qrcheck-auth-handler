package ru.softdarom.qrcheck.auth.handler.util;

import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.softdarom.qrcheck.auth.handler.test.tag.UnitTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@UnitTest
@DisplayName("JsonHelper Unit Test")
class JsonHelperTest {

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("asJson(): returns a valid json")
    void successfulAsJson() {
        var expected = "{\"string\":\"string\",\"integer\":1,\"localDateTime\":\"01.01.2021 00:00:00\"}";
        var actual = JsonHelper.asJson(new ObjectForJson("string", 1, LocalDateTime.of(2021, 1, 1, 0, 0)));
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("asJson(): returns 'null' when object is null")
    void successfulAsJsonNull() {
        var expected = "null";
        var actual = JsonHelper.asJson(null);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    //  -----------------------   fail tests   -------------------------

    @Test
    @DisplayName("asJson(): returns 'unknown' when JsonProcessingException")
    void failAsJson() {
        var expected = "unknown";
        JsonHelper.getMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
        var actual = JsonHelper.asJson(new EmptyObject());
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Getter
    @RequiredArgsConstructor
    private static class ObjectForJson {

        private final String string;
        private final Integer integer;
        private final LocalDateTime localDateTime;

    }

    private static class EmptyObject {

    }
}