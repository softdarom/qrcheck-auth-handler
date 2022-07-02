package ru.softdarom.qrcheck.auth.handler.base;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.test.tag.UnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
@DisplayName("ApiKeyType Unit Test")
class ApiKeyTypeTest {

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("typeOf(): returns 'OUTGOING' when 'outgoing'")
    void successfulTypeOfOutgoing() {
        assertEquals(ApiKeyType.OUTGOING, ApiKeyType.typeOf("outgoing"));
    }

    @Test
    @DisplayName("typeOf(): returns 'INCOMING' when 'incoming'")
    void successfulTypeOfIncoming() {
        assertEquals(ApiKeyType.INCOMING, ApiKeyType.typeOf("incoming"));
    }

    @Test
    @DisplayName("toString(): returns a valid string when 'OUTGOING'")
    void successfulToStringOutgoing() {
        var expected = "outgoing";
        assertEquals(expected, ApiKeyType.OUTGOING.toString());
    }

    @Test
    @DisplayName("toString(): returns a valid string when 'INCOMING'")
    void successfulToStringIncoming() {
        var expected = "incoming";
        assertEquals(expected, ApiKeyType.INCOMING.toString());
    }
}