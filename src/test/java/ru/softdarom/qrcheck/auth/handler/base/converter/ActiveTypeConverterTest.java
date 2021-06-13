package ru.softdarom.qrcheck.auth.handler.base.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.converter.ActiveTypeConverter;
import ru.softdarom.qrcheck.auth.handler.test.tag.UnitTest;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
@DisplayName("ActiveTypeConverter Unit Test")
class ActiveTypeConverterTest {

    private ActiveTypeConverter activeTypeConverter;

    @BeforeEach
    void setup() {
        activeTypeConverter = new ActiveTypeConverter();
    }

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("convertToDatabaseColumn(): returns all valid values")
    void successfulConvertToDatabaseColumn() {
        var expected = Set.of(Boolean.TRUE, Boolean.FALSE);
        var actual = EnumSet.allOf(ActiveType.class)
                .stream()
                .map(activeTypeConverter::convertToDatabaseColumn)
                .collect(Collectors.toSet());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("convertToEntityAttribute(): returns all valid values")
    void successfulConvertToEntityAttribute() {
        var expected = Set.of(ActiveType.ENABLED, ActiveType.DISABLED);
        var actual = Set.of(Boolean.TRUE, Boolean.FALSE)
                .stream()
                .map(activeTypeConverter::convertToEntityAttribute)
                .collect(Collectors.toSet());
        assertEquals(expected, actual);
    }
}