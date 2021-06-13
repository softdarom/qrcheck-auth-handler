package ru.softdarom.qrcheck.auth.handler.base.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.base.converter.ProviderTypeConverter;
import ru.softdarom.qrcheck.auth.handler.test.tag.UnitTest;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
@DisplayName("ProviderTypeConverter Unit Test")
class ProviderTypeConverterTest {

    private ProviderTypeConverter providerTypeConverter;

    @BeforeEach
    void setup() {
        providerTypeConverter = new ProviderTypeConverter();
    }

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("convertToDatabaseColumn(): returns all valid values")
    void successfulConvertToDatabaseColumn() {
        var expected = Set.of("google", "facebook", "vkontakte", "qrcheck");
        var actual = EnumSet.allOf(ProviderType.class)
                .stream()
                .map(providerTypeConverter::convertToDatabaseColumn)
                .collect(Collectors.toSet());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("convertToEntityAttribute(): returns all valid values")
    void successfulConvertToEntityAttribute() {
        var expected = Set.of(ProviderType.GOOGLE, ProviderType.FACEBOOK, ProviderType.VKONTAKTE, ProviderType.QRCHECK);
        var actual = Set.of("google", "facebook", "vkontakte", "qrcheck")
                .stream()
                .map(providerTypeConverter::convertToEntityAttribute)
                .collect(Collectors.toSet());
        assertEquals(expected, actual);
    }
}