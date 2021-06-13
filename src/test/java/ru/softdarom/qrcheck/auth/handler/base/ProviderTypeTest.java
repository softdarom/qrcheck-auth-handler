package ru.softdarom.qrcheck.auth.handler.base;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.test.tag.UnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
@DisplayName("ProviderType Unit Test")
class ProviderTypeTest {

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("typeOf(): returns 'GOOGLE' when 'google'")
    void successfulTypeOfAndroid() {
        assertEquals(ProviderType.GOOGLE, ProviderType.typeOf("google"));
    }

    @Test
    @DisplayName("toString(): returns a valid string when 'GOOGLE'")
    void successfulToStringAndroid() {
        var expected = "google";
        assertEquals(expected, ProviderType.GOOGLE.toString());
    }

}