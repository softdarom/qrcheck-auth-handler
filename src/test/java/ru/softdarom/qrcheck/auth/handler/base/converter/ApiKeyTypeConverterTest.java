package ru.softdarom.qrcheck.auth.handler.base.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;
import ru.softdarom.qrcheck.auth.handler.model.base.converter.ApiKeyTypeConverter;
import ru.softdarom.qrcheck.auth.handler.test.tag.UnitTest;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
@DisplayName("ApiKeyTypeConverter Unit Test")
class ApiKeyTypeConverterTest {

    private ApiKeyTypeConverter apiKeyTypeConverter;

    @BeforeEach
    void setup() {
        apiKeyTypeConverter = new ApiKeyTypeConverter();
    }

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("convertToDatabaseColumn(): returns all valid values")
    void successfulConvertToDatabaseColumn() {
        var expected = Set.of("incoming", "outgoing");
        var actual = EnumSet.allOf(ApiKeyType.class)
                .stream()
                .map(apiKeyTypeConverter::convertToDatabaseColumn)
                .collect(Collectors.toSet());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("convertToEntityAttribute(): returns all valid values")
    void successfulConvertToEntityAttribute() {
        var expected = Set.of(ApiKeyType.INCOMING, ApiKeyType.OUTGOING);
        var actual = Stream.of("incoming", "outgoing")
                .map(apiKeyTypeConverter::convertToEntityAttribute)
                .collect(Collectors.toSet());
        assertEquals(expected, actual);
    }
}